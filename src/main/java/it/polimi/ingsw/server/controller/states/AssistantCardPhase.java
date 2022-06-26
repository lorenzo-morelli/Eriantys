package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.view.cli.cliController.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.pingThread.AssistantCardThread;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Team;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This server state implements the logic necessary to correctly handle
 * the Assistant card phase of the game.
 */
public class AssistantCardPhase extends State {
    private final Event cardsChosen,gameEnd;
    private final ConnectionModel connectionModel;

    private final Gson json;
    private final ServerController serverController;
    private ParametersFromNetwork message;
    private boolean disconnected,fromPing;

    /**
     * Events callers
     * @return different events in order to change to different phase
     */
    public Event gameEnd() {
        return gameEnd;
    }
    public Event cardsChosen() {
        return cardsChosen;
    }

    /**
     * The main constructor of the Assistant Card Phase
     * @param serverController the main server controller
     */
    public AssistantCardPhase(ServerController serverController) {
        super("[Choose Assistant Card]");
        this.serverController = serverController;
        Controller controller = ServerController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        cardsChosen = new Event("game created");
        cardsChosen.setStateEventListener(controller);
        gameEnd = new Event("end phase");
        gameEnd.setStateEventListener(controller);
        Event reset = new ClientDisconnection();
        reset.setStateEventListener(controller);
        json = new Gson();
    }

    /**
     * For each player among those correctly connected to the game, in turn,
     * send a request to their view asking him to choose the assistant card
     * and wait for the answer.
     * Some sanity checks are made (in case of malicious client), as well as
     * special code to handle the advanced disconnection feature.
     * @param cause what caused the server controller to enter this method
     * @return null event
     * @throws Exception IO errors and network related problems.
     */
    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        Model model = serverController.getModel();
        ArrayList<AssistantCard> alreadyChooses=new ArrayList<>();
        model.nextTurn();

        for(int i = 0; i< model.getNumberOfPlayers(); i++){

            Player currentPlayer = model.getcurrentPlayer();
            disconnected=false;
            fromPing=false;


            if(!currentPlayer.isDisconnected()) {

                ClientModel currentPlayerData;
                boolean lowPriority = false;
                fromPing = false;

                currentPlayerData = connectionModel.findPlayer(currentPlayer.getNickname());


                List<AssistantCard> canBeChoose = new ArrayList<>(currentPlayer.getAvailableCards().getCardsList());
                if (alreadyChooses.containsAll(canBeChoose)) {
                    lowPriority = true;
                } else {
                    canBeChoose.removeAll(alreadyChooses);
                }

                currentPlayerData.setDeck(canBeChoose);
                currentPlayerData.setResponse(false);
                currentPlayerData.setTypeOfRequest("CHOOSEASSISTANTCARD");
                currentPlayerData.setPingMessage(false);
                currentPlayerData.setServermodel(model);

                boolean checkDisconnection;

                try {
                    checkDisconnection = Network.send(json.toJson(currentPlayerData));
                }catch (ConcurrentModificationException e){
                    checkDisconnection = Network.send(json.toJson(currentPlayerData));
                }

                if (checkDisconnection) {

                    Thread ping = new AssistantCardThread(this, currentPlayerData);
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
                                    alreadyChooses.add(currentPlayer.getAvailableCards().getCardsList().get(currentPlayer.getAvailableCards().getCardsList().size() - 1));
                                    currentPlayer.setChoosedCard(canBeChoose.get(canBeChoose.size()-1));
                                    boolean check=true;
                                    for(int j=0;j<model.getTable().getClouds().size();j++) {
                                        if(model.getTable().getClouds().get(j).getStudentsAccumulator().size()==0)
                                        {
                                            model.getTable().getClouds().remove(j);
                                            check=false;
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
                            fromPing=false;
                    }

                }


                if (!currentPlayer.isDisconnected()) {

                    AssistantCard chosen = null;
                    currentPlayerData = json.fromJson(message.getParameter(0), ClientModel.class);

                    for (int j = 0; j < currentPlayer.getAvailableCards().getCardsList().size(); j++) {
                        if (currentPlayerData.getCardChoosedValue() == currentPlayer.getAvailableCards().getCardsList().get(j).getValues()) {
                            chosen = currentPlayer.getAvailableCards().getCardsList().get(j);
                        }
                    }


                    if (lowPriority) {
                        assert chosen != null;
                        chosen.lowPriority();
                    }

                    alreadyChooses.add(chosen);
                    boolean checkEndCondition = currentPlayer.setChoosedCard(chosen);

                    if (checkEndCondition) {
                        model.setlastturn();
                    }

                }


                else {
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
                        for (Player p : model.getPlayers()) {
                            if(!p.isDisconnected()) {
                                ClientModel Data = connectionModel.findPlayer(p.getNickname());

                                Data.setTypeOfRequest("TRYTORECONNECT");
                                Data.setServermodel(model);
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
            }


            model.nextPlayer();
        }
        cardsChosen.fireStateEvent();
        model.schedulePlayers();
        return super.entryAction(cause);
    }

    /**
     * Utils method for ping and disconnection manage
     */

    public void setDisconnected(boolean value){
        disconnected=value;
    }

    public void setMessage(ParametersFromNetwork message) {
        this.message = message;
    }

    public ParametersFromNetwork getMessage() {
        return message;
    }

    public void setFromPing(boolean fromPing) {
        this.fromPing = fromPing;
    }
}
