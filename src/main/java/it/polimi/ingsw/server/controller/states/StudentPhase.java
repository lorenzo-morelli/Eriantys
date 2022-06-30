package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.view.cli.cliController.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.pingThread.StudentThread;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.characters.*;
import it.polimi.ingsw.server.model.enums.GameMode;
import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.shuffle;

/**
 * This state handle the  phase of the game in which the user has to choose where to move the student
 * (in his school or on an island). Formal correctness checks have been carried out in the event of
 * malicious input from the user.
 * @author Ignazio Neto Dell'Acqua
 * @author Fernando Morea
 */
public class StudentPhase extends State {
    private final Event studentPhaseEnded, gameEnd;

    private final ConnectionModel connectionModel;

    private final Gson json;
    private final ServerController serverController;

    private ParametersFromNetwork message;

    private boolean disconnected,fromPing;

    /**
     * Events caller
     * @return studentPhaseEnded event in order to trigger the fsm machine
     */
    public Event studentPhaseEnded() {
        return studentPhaseEnded;
    }
    /**
     * Events caller
     * @return gameEnd event in order to trigger the fsm machine
     */
    public Event gameEnd() {
        return gameEnd;
    }

    /**
     * The main constructor of the Student phase
     * @param serverController the main server controller
     */
    public StudentPhase(ServerController serverController) {
        super("[Move students]");
        this.serverController = serverController;
        Controller controller = ServerController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        studentPhaseEnded = new Event("game created");
        studentPhaseEnded.setStateEventListener(controller);
        Event reset = new ClientDisconnection();
        reset.setStateEventListener(controller);
        gameEnd = new Event("end phase");
        gameEnd.setStateEventListener(controller);
        json = new Gson();
    }

    /**
     * Sends a request to the view of the current player to choose where to move students over the table
     * and waits a response from the view.
     * Also handle the usage of card for expert mode.
     * Special code to handle disconnection was added.
     * @param cause the event that caused the controller transition in this state
     * @return null event
     * @throws Exception input output or network related exceptions
     */
    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        int moves;
        Model model = serverController.getModel();

        Player currentPlayer = model.getCurrentPlayer();
        disconnected=false;
        fromPing=false;

        if(currentPlayer.isDisconnected()){
            studentPhaseEnded.fireStateEvent();
            return super.entryAction(cause);
        }

        ClientModel currentPlayerData = connectionModel.findPlayer(currentPlayer.getNickname());

        if(model.getNumberOfPlayers() == 3){
            moves = 4;
        }
        else{
            moves = 3;
        }

        for(int i=0; i< moves; i++) {

            currentPlayerData.setServerModel(model);
            currentPlayerData.setTypeOfRequest("CHOOSEWHERETOMOVESTUDENTS");
            currentPlayerData.setPingMessage(false);
            currentPlayerData.setResponse(false);

            boolean checkDisconnection;
            try {
                checkDisconnection = Network.send(json.toJson(currentPlayerData));
            }catch (ConcurrentModificationException e){
                checkDisconnection = Network.send(json.toJson(currentPlayerData));
            }

            if(!checkDisconnection){
                studentPhaseEnded.fireStateEvent();
                return super.entryAction(cause);
            }

            Thread ping = new StudentThread(this, currentPlayerData);
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
                            List<PeopleColor> colors= Arrays.asList(PeopleColor.values());
                            shuffle(colors);
                            int j=0;
                            for(PeopleColor color: colors) {
                                if(currentPlayer.getSchoolBoard().getEntranceSpace().numStudentsByColor(color)!=0) {
                                    currentPlayer.getSchoolBoard().loadDinnerTable(color);
                                    model.getTable().checkProfessor(color,model.getPlayers());
                                    j++;
                                    if (j == moves - i) {
                                        break;
                                    }
                                }
                            }
                            for(int k=0;k<model.getTable().getClouds().size();k++) {
                                if(model.getTable().getClouds().get(k).getStudentsAccumulator().size()!=0)
                                {
                                    currentPlayer.getSchoolBoard().loadEntrance(model.getTable().getClouds().get(k),model.getTable().getClouds());
                                    model.getTable().getClouds().remove(k);
                                    break;
                                }
                            }
                            break;
                        } else {
                            ping.interrupt();
                        }
                    }
                    fromPing=false;
            }

            if (!currentPlayer.isDisconnected()) {
                currentPlayerData = json.fromJson(message.getParameter(0), ClientModel.class);

                String type = currentPlayerData.getTypeOfRequest();
                if (type.equals("SCHOOL")) {
                    currentPlayer.getSchoolBoard().loadDinnerTable(currentPlayerData.getChosenColor());
                    if (model.getGameMode().equals(GameMode.EXPERT) && currentPlayer.getSchoolBoard().getDinnerTable().numStudentsByColor(currentPlayerData.getChosenColor()) % 3 == 0) {
                        currentPlayer.increaseCoin();
                    }
                    model.getTable().checkProfessor(currentPlayerData.getChosenColor(), model.getPlayers());
                } else if (type.equals("ISLAND")) {
                    model.getTable().loadIsland(currentPlayer, currentPlayerData.getChosenColor(), currentPlayerData.getChosenIsland());
                } else {
                    i--;
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
                                    ((Minstrel) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, currentPlayerData.getColors2(), currentPlayerData.getColors1(),model.getTable(),model.getPlayers());
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
                        System.out.println("Minimum number of player not available, wait 40 second in order to make the player able to reconnect");
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
                break;
            }
        }

        studentPhaseEnded.fireStateEvent();
        return super.entryAction(cause);
    }

    public ParametersFromNetwork getMessage() {
        return message;
    }

    public void setMessage(ParametersFromNetwork message) {
        this.message = message;
    }

    /**
     * Set the value disconnected from pings in order to know if the player is disconnected or not
     */
    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    /**
     * set if the message received is retrieved when it expects a ping
     */
    public void setFromPing(boolean fromPing) {
        this.fromPing = fromPing;
    }
}