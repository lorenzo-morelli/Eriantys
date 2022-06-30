package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.view.cli.cliController.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.pingThread.MotherThread;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Team;
import it.polimi.ingsw.server.model.characters.*;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.concurrent.TimeUnit;

/**
 * This state handles the movement of mother nature based on the number of steps chosen by the user
 * (and obviously the card that the user had previously chosen which determines the upper limit to
 * the number of moves that mother nature can make).
 * @author Ignazio Neto Dell'Acqua
 * @author Fernando Morea
 */
public class MotherPhase extends State {
    private final Event gameEnd, goToStudentPhase, goToCloudPhase, goToEndTurn;
    private Model model;
    private final ConnectionModel connectionModel;
    private final Gson json;
    private final ServerController serverController;
    private ParametersFromNetwork message;
    private boolean disconnected, fromPing;
    private boolean storekeeper;

    /**
     * Events callers
     *
     * @return different events in order to change to different phase
     */
    public Event gameEnd() {
        return gameEnd;
    }

    public Event goToStudentPhase() {
        return goToStudentPhase;
    }

    public Event GoToCloudPhase() {
        return goToCloudPhase;
    }

    public Event GoToEndTurn() {
        return goToEndTurn;
    }

    /**
     * The main constructor of the Mother phase
     *
     * @param serverController the main server controller
     */
    public MotherPhase(ServerController serverController) {
        super("[Move mother]");
        this.serverController = serverController;
        Controller controller = ServerController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        Event reset = new ClientDisconnection();
        reset.setStateEventListener(controller);
        gameEnd = new Event("end phase");
        gameEnd.setStateEventListener(controller);
        goToCloudPhase = new Event("go to cloud phase");
        goToCloudPhase.setStateEventListener(controller);
        goToStudentPhase = new Event("go to student phase");
        goToStudentPhase.setStateEventListener(controller);
        goToEndTurn = new Event("go to end turn");
        goToEndTurn.setStateEventListener(controller);
        json = new Gson();
        storekeeper = true;
    }

    /**
     * Sends a request to the view of the current player to choose where to move mother nature
     * and waits a response from the view.
     * Also handle the usage of card for expert mode.
     * Special code to handle disconnection was added.
     *
     * @param cause the event that caused the controller transition in this state
     * @return null event
     * @throws Exception input output or network related exceptions
     */
    @Override
    public IEvent entryAction(IEvent cause) throws Exception {

        while (storekeeper) {
            storekeeper = false;
            model = serverController.getModel();

            Player currentPlayer = model.getCurrentPlayer();
            disconnected = false;
            fromPing = false;

            if (currentPlayer.isDisconnected()) {

                if (model.getTable().getIslands().size() <= 3) {
                    gameEnd().fireStateEvent();
                    return super.entryAction(cause);
                } else if (model.isLastTurn()) {
                    if (model.getCurrentPlayer().equals(model.getPlayers().get(model.getPlayers().size() - 1))) {
                        gameEnd().fireStateEvent();
                    } else {
                        model.nextPlayer();
                        goToStudentPhase().fireStateEvent();
                    }
                    return super.entryAction(cause);
                } else if (model.getCurrentPlayer().equals(model.getPlayers().get(model.getPlayers().size() - 1))) {
                    GoToEndTurn().fireStateEvent();
                } else {
                    model.nextPlayer();
                    goToStudentPhase().fireStateEvent();
                }
                return super.entryAction(cause);
            }

            System.out.println(currentPlayer.getNickname());

            ClientModel currentPlayerData = connectionModel.findPlayer(currentPlayer.getNickname());
            currentPlayerData.setServerModel(model);
            currentPlayerData.setTypeOfRequest("CHOOSEWHERETOMOVEMOTHER");
            currentPlayerData.setPingMessage(false);
            currentPlayerData.setResponse(false);
            boolean checkDisconnection;
            try {
                checkDisconnection = Network.send(json.toJson(currentPlayerData));
            } catch (ConcurrentModificationException e) {
                checkDisconnection = Network.send(json.toJson(currentPlayerData));
            }

            if (!checkDisconnection) {
                return fireCorrectEvent(cause);
            }


            Thread ping = new MotherThread(this, currentPlayerData);
            ping.start();

            boolean responseReceived = false;
            while (!responseReceived) {

                if (!fromPing) {
                    message = new ParametersFromNetwork(1);
                    message.enable();
                }
                while (!message.parametersReceived()) {
                    message.waitParametersReceived(5);
                    if (disconnected) {
                        break;
                    }
                }
                if (disconnected || (json.fromJson(message.getParameter(0), ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity() && !json.fromJson(message.getParameter(0), ClientModel.class).isPingMessage())) {
                    responseReceived = true;
                    if (disconnected) {
                        currentPlayer.setDisconnected(true);
                        boolean check = true;
                        for (int j = 0; j < model.getTable().getClouds().size(); j++) {
                            if (model.getTable().getClouds().get(j).getStudentsAccumulator().size() == 0) {
                                model.getTable().getClouds().remove(j);
                                check = false;
                                break;
                            }
                        }
                        if (check) {
                            model.getTable().getClouds().remove(0);
                        }
                    } else {
                        ping.interrupt();
                    }
                }
                fromPing = false;
            }


            if (!currentPlayer.isDisconnected()) {

                currentPlayerData = json.fromJson(message.getParameter(0), ClientModel.class);
                String type = currentPlayerData.getTypeOfRequest();
                if (type.equals("MOTHER")) {
                    int moves = currentPlayerData.getChosenMoves();
                    model.getTable().mother(moves);
                    Island target = model.getTable().getIslands().get(model.getTable().getMotherNaturePosition());
                    if (!target.isBlocked()) {
                        if (model.getNumberOfPlayers() == 4) {
                            Team influenceTeam = target.teamInfluenceCalculator(model.getTeams(), model.getTable().getProfessors(), model.getTable().isCentaurEffect(), model.getTable().getMushroomColor(), model.getTable().getKnightEffect());
                            if (influenceTeam != null) {
                                if (target.getNumberOfTowers() == 0) {
                                    target.controlIsland(influenceTeam);
                                    target.placeTower();
                                } else if (!(target.getTowerColor().equals(influenceTeam.getPlayer1().getSchoolBoard().getTowerColor()))) {
                                    model.getTable().conquestIsland(model.getTable().getMotherNaturePosition(), model.getTeams(), influenceTeam);
                                }
                                if (influenceTeam.getPlayer1().getSchoolBoard().getNumOfTowers() == 0) {
                                    gameEnd().fireStateEvent();
                                    return super.entryAction(cause);
                                }
                            }
                        } else {
                            Player influencePlayer = target.playerInfluenceCalculator(model.getPlayers(), model.getTable().getProfessors(), model.getTable().isCentaurEffect(), model.getTable().getMushroomColor(), model.getTable().getKnightEffect());
                            if (influencePlayer != null) {
                                if (target.getNumberOfTowers() == 0) {
                                    target.controlIsland(influencePlayer);
                                    target.placeTower();
                                } else if (!(target.getTowerColor().equals(influencePlayer.getSchoolBoard().getTowerColor()))) {
                                    model.getTable().conquestIsland(model.getTable().getMotherNaturePosition(), model.getPlayers(), influencePlayer);
                                }
                                if (influencePlayer.getSchoolBoard().getNumOfTowers() == 0) {
                                    gameEnd().fireStateEvent();
                                    return super.entryAction(cause);
                                }
                            }
                        }
                        if (model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()) != null) {
                            if (model.getTable().getIslands().get((model.getTable().getMotherNaturePosition() + 1) % model.getTable().getIslands().size()).getTowerColor() != null && model.getTable().getIslands().get((model.getTable().getMotherNaturePosition() + 1) % model.getTable().getIslands().size()).getTowerColor().equals(model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()).getTowerColor())) {
                                model.getTable().mergeIsland(model.getTable().getMotherNaturePosition(), ((model.getTable().getMotherNaturePosition() + 1) % model.getTable().getIslands().size()));
                            }
                            Island merging;
                            if (model.getTable().getMotherNaturePosition() == 0) {
                                merging = model.getTable().getIslands().get(model.getTable().getIslands().size() - 1);
                            } else {
                                merging = model.getTable().getIslands().get((model.getTable().getMotherNaturePosition() - 1) % model.getTable().getIslands().size());
                            }
                            if (merging.getTowerColor() != null && merging.getTowerColor().equals(model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()).getTowerColor())) {
                                int mergingIndex;
                                if (model.getTable().getMotherNaturePosition() == 0) {
                                    mergingIndex = model.getTable().getIslands().size() - 1;
                                } else {
                                    mergingIndex = (model.getTable().getMotherNaturePosition() - 1) % model.getTable().getIslands().size();
                                }
                                model.getTable().mergeIsland(model.getTable().getMotherNaturePosition(), mergingIndex);
                            }
                        }
                    } else {
                        target.setBlocked(false);
                        for (int i = 0; i < model.getTable().getCharacters().size(); i++) {
                            if (model.getTable().getCharacters().get(i) instanceof Granny) {
                                ((Granny) model.getTable().getCharacters().get(i)).improveDivieti();
                                break;
                            }
                        }
                    }

                    model.getTable().setCentaurEffect(false);
                    model.getTable().setMushroomColor(null);
                    model.getTable().setKnightEffect(null);

                    return fireCorrectEvent(cause);
                } else {
                    storekeeper = true;
                    for (int j = 0; j < model.getTable().getCharacters().size(); j++) {
                        if (model.getTable().getCharacters().get(j).getName().equals(type)) {
                            switch (type) {
                                case "MUSHROOMHUNTER":
                                    ((MushroomHunter) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, currentPlayerData.getChosenColor(), model.getTable());
                                    break;
                                case "THIEF":
                                    ((Thief) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, model.getPlayers(), currentPlayerData.getChosenColor(), model.getTable());
                                    break;
                                case "CENTAUR":
                                    ((Centaur) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, model.getTable());
                                    break;
                                case "FARMER":
                                    ((Farmer) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, model.getTable(), model.getPlayers());
                                    break;
                                case "KNIGHT":
                                    ((Knight) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, model.getTable());
                                    break;
                                case "MINSTRELL":
                                    ((Minstrel) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, currentPlayerData.getColors2(), currentPlayerData.getColors1(), model.getTable(), model.getPlayers());
                                    break;
                                case "JESTER":
                                    ((Jester) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, currentPlayerData.getColors2(), currentPlayerData.getColors1());
                                    break;
                                case "POSTMAN":
                                    ((Postman) model.getTable().getCharacters().get(j)).useEffect(currentPlayer);
                                    break;
                                case "PRINCESS":
                                    ((Princess) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, currentPlayerData.getChosenColor(), model.getTable(), model.getPlayers());
                                    break;
                                case "GRANNY":
                                    ((Granny) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, currentPlayerData.getChosenIsland(), model.getTable());
                                    break;
                                case "MONK":
                                    ((Monk) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, currentPlayerData.getChosenColor(), currentPlayerData.getChosenIsland(), model.getTable());
                                    break;
                                case "HERALD":
                                    boolean check = ((Herald) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, currentPlayerData.getChosenIsland(), model);
                                    if (check) {
                                        gameEnd().fireStateEvent();
                                        return super.entryAction(cause);
                                    }

                                    break;
                            }
                            break;
                        }
                    }
                }
            } else {
                int check = 0;
                if (model.getNumberOfPlayers() == 4) {
                    for (Team team : model.getTeams()) {
                        if (!team.getPlayer1().isDisconnected() || !team.getPlayer2().isDisconnected()) {
                            check++;
                        }
                    }
                } else {
                    for (Player p : model.getPlayers()) {
                        if (!p.isDisconnected()) {
                            check++;
                        }
                    }
                }
                if (check <= 1) {
                    System.out.println("Minimum number of player not available, wait 40 second in order to make the player able to reconnect");
                    check = 0;
                    if (model.getNumberOfPlayers() == 4) {
                        for (Team team : model.getTeams()) {
                            if (!team.getPlayer1().isDisconnected() || !team.getPlayer2().isDisconnected()) {
                                check++;
                            }
                        }
                    } else {
                        for (Player p : model.getPlayers()) {
                            if (!p.isDisconnected()) {
                                check++;
                            }
                        }
                    }
                    if (check <= 1) {
                        System.out.println("Minimum number of players not available, I wait 40 seconds waiting for another player to reconnect");
                        for (Player p : model.getPlayers()) {
                            if (!p.isDisconnected()) {
                                ClientModel Data = connectionModel.findPlayer(p.getNickname());

                                Data.setTypeOfRequest("TRYTORECONNECT");
                                Data.setServerModel(model);
                                Data.setResponse(false);
                                Data.setPingMessage(false);

                                Network.send(json.toJson(Data));
                            }
                        }

                        model.setDisconnection(true);
                        TimeUnit.MILLISECONDS.sleep(40000);

                        if (model.isDisconnection()) {
                            gameEnd().fireStateEvent();
                            return super.entryAction(cause);
                        }
                        System.out.println("One player is reconnected, the game will continue...");
                    }
                }
                if (model.getTable().getIslands().size() <= 3) {
                    gameEnd().fireStateEvent();
                    return super.entryAction(cause);
                } else if (model.isLastTurn()) {
                    if (model.getCurrentPlayer().equals(model.getPlayers().get(model.getPlayers().size() - 1))) {
                        gameEnd().fireStateEvent();
                    } else {
                        model.nextPlayer();
                        goToStudentPhase().fireStateEvent();
                    }
                    return super.entryAction(cause);
                } else {
                    model.nextPlayer();
                    GoToCloudPhase().fireStateEvent();
                    return super.entryAction(cause);
                }
            }
        }
        return super.entryAction(cause);
    }

    private IEvent fireCorrectEvent(IEvent cause) throws Exception {
        if (model.getTable().getIslands().size() <= 3) {
            gameEnd().fireStateEvent();
            return super.entryAction(cause);
        } else if (model.isLastTurn()) {
            if (model.getCurrentPlayer().equals(model.getPlayers().get(model.getPlayers().size() - 1))) {
                gameEnd().fireStateEvent();
            } else {
                model.nextPlayer();
                goToStudentPhase().fireStateEvent();
            }
            return super.entryAction(cause);
        } else {
            GoToCloudPhase().fireStateEvent();
            return super.entryAction(cause);
        }
    }

    /**
     * Reinitialize variables for next uses
     *
     * @param cause the event that caused us to exit this state
     */
    @Override
    public void exitAction(IEvent cause) throws IOException {
        model.getTable().setFarmerEffect(null);
        storekeeper = true;
        super.exitAction(cause);
    }

    /**
     * Utils method for ping and disconnection manage
     */
    public ParametersFromNetwork getMessage() {
        return message;
    }

    public void setMessage(ParametersFromNetwork message) {
        this.message = message;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    public void setFromPing(boolean fromPing) {
        this.fromPing = fromPing;
    }
}
