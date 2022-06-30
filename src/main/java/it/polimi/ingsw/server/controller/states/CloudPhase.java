package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.view.cli.cliController.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.pingThread.CloudThread;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.util.ConcurrentModificationException;
import java.util.concurrent.TimeUnit;

/**
 * This class handles the cloud phase of the game, on witch the user can choose one
 * of the clouds in the board (that had not already been chosen by another player)
 * @author Ignazio Neto Dell'Acqua
 * @author Fernando Morea
 */
public class CloudPhase extends State {
    private final Event goToEndTurn, gameEnd;
    private final Event goToStudentPhase;

    private final ConnectionModel connectionModel;

    private final Gson json;
    private final ServerController serverController;

    private ParametersFromNetwork message;
    private boolean disconnected,fromPing;

    /**
     * Events callers
     * @return goToEndTurn event in order to trigger the fsm machine
     */

    public Event GoToEndTurn() {
        return goToEndTurn;
    }

    /**
     * Events callers
     * @return gameEnd event in order to trigger the fsm machine
     */
    public Event gameEnd() {
        return gameEnd;
    }

    /**
     * Events callers
     * @return goToStudentPhase event in order to trigger the fsm machine
     */
    public Event GoToStudentPhase() {
        return goToStudentPhase;
    }

    /**
     * The main constructor of the cloud phase
     * @param serverController the main server controller
     */
    public CloudPhase(ServerController serverController) {
        super("[Choose Cloud]");
        this.serverController = serverController;
        Controller controller = ServerController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        goToEndTurn= new Event("go to end turn");
        Event reset = new ClientDisconnection();
        reset.setStateEventListener(controller);
        goToEndTurn.setStateEventListener(controller);
        goToStudentPhase= new Event("go to student phase");
        goToStudentPhase.setStateEventListener(controller);
        gameEnd = new Event("end phase");
        gameEnd.setStateEventListener(controller);

        json = new Gson();
    }


    /**
     * Sends a request to the view of the current player to choose one of the available clouds
     * and waits a response from the view.
     * Also handle special case as well as sanity checks (for example is not possible to choose
     * a cloud which had already been chosen from another player).
     * Special code to handle disconnection was added.
     * @param cause the event that caused the controller transition in this state
     * @return null event
     * @throws Exception input output or network related exceptions
     */
    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        Model model = serverController.getModel();

        Player currentPlayer = model.getCurrentPlayer();
        disconnected=false;
        fromPing=false;

        if(currentPlayer.isDisconnected()){

            currentPlayer.setDisconnected(true);
            for(Cloud c: model.getTable().getClouds()) {
                if(c.getStudentsAccumulator().size()!=0) {
                    currentPlayer.getSchoolBoard().loadEntrance(c,model.getTable().getClouds());
                    break;
                }
            }

            if (model.getCurrentPlayer().equals(model.getPlayers().get(model.getPlayers().size() - 1))) {
                GoToEndTurn().fireStateEvent();
            } else {
                model.nextPlayer();
                GoToStudentPhase().fireStateEvent();
            }
            return super.entryAction(cause);
        }

        ClientModel currentPlayerData = connectionModel.findPlayer(currentPlayer.getNickname());
        currentPlayerData.setServerModel(model);
        currentPlayerData.setTypeOfRequest("CHOOSECLOUDS");
        currentPlayerData.setPingMessage(false);
        currentPlayerData.setResponse(false);

        boolean checkDisconnection;
        try {
            checkDisconnection = Network.send(json.toJson(currentPlayerData));
        }catch (ConcurrentModificationException e){
            checkDisconnection = Network.send(json.toJson(currentPlayerData));
        }

        if(!checkDisconnection){
            if (model.getCurrentPlayer().equals(model.getPlayers().get(model.getPlayers().size() - 1))) {
                GoToEndTurn().fireStateEvent();
            } else {
                model.nextPlayer();
                GoToStudentPhase().fireStateEvent();
            }
            return super.entryAction(cause);
        }

        Thread ping = new CloudThread(this,currentPlayerData);
        ping.start();

        boolean responseReceived = false;
        while (!responseReceived) {
                if(!fromPing) {
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
                    if(disconnected){
                        currentPlayer.setDisconnected(true);
                        for(Cloud c: model.getTable().getClouds()) {
                            if(c.getStudentsAccumulator().size()!=0) {
                                currentPlayer.getSchoolBoard().loadEntrance(c,model.getTable().getClouds());
                                break;
                            }
                        }
                        for(int j=0;j<model.getTable().getClouds().size();j++) {
                            if(model.getTable().getClouds().get(j).getStudentsAccumulator().size()==0)
                            {
                                model.getTable().getClouds().remove(j);
                                break;
                            }
                        }
                    }
                    else {
                        ping.interrupt();
                    }
                }
                fromPing=false;
        }

        if(!currentPlayer.isDisconnected()) {

            currentPlayerData = json.fromJson(message.getParameter(0), ClientModel.class);
            Cloud cloud = currentPlayerData.getCloudChosen();
            currentPlayer.getSchoolBoard().loadEntrance(cloud, model.getTable().getClouds());

        }

        else {
            int check=0;
            if(model.getNumberOfPlayers()==4){
                for(Team team: model.getTeams()){
                    if(!team.getPlayer1().isDisconnected() || !team.getPlayer2().isDisconnected()){
                        check++;
                    }
                }
            }
            else{
                for(Player p: model.getPlayers()){
                    if(!p.isDisconnected()){
                        check++;
                    }
                }
            }
            if(check<=1){
                check=0;
                if(model.getNumberOfPlayers()==4){
                    for(Team team: model.getTeams()){
                        if(!team.getPlayer1().isDisconnected() || !team.getPlayer2().isDisconnected()){
                            check++;
                        }
                    }
                }
                else{
                    for(Player p: model.getPlayers()){
                        if(!p.isDisconnected()){
                            check++;
                        }
                    }
                }
                if(check<=1){
                    System.out.println("Minimum number of player not available, wait 40 second in order to make the player able to reconnect");
                    for (Player p : model.getPlayers()) {
                        if(!p.isDisconnected()) {
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
        }

        if (model.getCurrentPlayer().equals(model.getPlayers().get(model.getPlayers().size() - 1))) {
            GoToEndTurn().fireStateEvent();
        } else {
            model.nextPlayer();
            GoToStudentPhase().fireStateEvent();
        }

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