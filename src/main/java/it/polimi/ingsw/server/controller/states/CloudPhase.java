package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.controller.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.util.concurrent.TimeUnit;

public class CloudPhase extends State {
    private final Event goToEndTurn, gameEnd;
    private final Event goToStudentPhase;

    private final ConnectionModel connectionModel;

    private final Gson json;
    private final ServerController serverController;
    private final Event reset = new ClientDisconnection();

    private ParametersFromNetwork message;
    private boolean disconnected,fromPing;


    public Event GoToEndTurn() {
        return goToEndTurn;
    }
    public Event gameEnd() {
        return gameEnd;
    }

    public Event GoToStudentPhase() {
        return goToStudentPhase;
    }

    public CloudPhase(ServerController serverController) {
        super("[Choose Cloud]");
        this.serverController = serverController;
        Controller controller = ServerController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        goToEndTurn= new Event("go to end turn");
        reset.setStateEventListener(controller);
        goToEndTurn.setStateEventListener(controller);
        goToStudentPhase= new Event("go to student phase");
        goToStudentPhase.setStateEventListener(controller);
        gameEnd = new Event("end phase");
        gameEnd.setStateEventListener(controller);

        json = new Gson();
    }

    public Event getReset() {
        return reset;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        Model model = serverController.getModel();
        // retrive the current player
        Player currentPlayer = model.getcurrentPlayer();
        if(currentPlayer.isDisconnected()){
            if (model.getcurrentPlayer().equals(model.getPlayers().get(model.getPlayers().size() - 1))) {
                GoToEndTurn().fireStateEvent();
            } else {
                model.nextPlayer();
                GoToStudentPhase().fireStateEvent();
            }
            return super.entryAction(cause);
        }
        // retrive data of the current player
        ClientModel currentPlayerData = connectionModel.findPlayer(currentPlayer.getNickname());
        disconnected=false;

        currentPlayerData.setServermodel(model);
        currentPlayerData.setTypeOfRequest("CHOOSECLOUDS");
        currentPlayerData.setPingMessage(false);
        currentPlayerData.setResponse(false); //non è una risposta, è una richiesta del server al client

        boolean chekDisco= Network.send(json.toJson(currentPlayerData));
        if(!chekDisco){
            if (model.getcurrentPlayer().equals(model.getPlayers().get(model.getPlayers().size() - 1))) {
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
            synchronized (this) {
                if(!fromPing) {
                    message = new ParametersFromNetwork(1);
                    message.enable();
                }
            }
            while (!message.parametersReceived()) {
                if(disconnected){
                    break;
                }
            }
            synchronized (this) {
                if (disconnected || (json.fromJson(message.getParameter(0), ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity() && !json.fromJson(message.getParameter(0), ClientModel.class).isPingMessage())) {
                    responseReceived = true;
                    if(disconnected){
                        currentPlayer.setDisconnected(true);
                        for(Cloud c: model.getTable().getClouds()) {
                            if(c.getStudentsAccumulator().size()!=0) {
                                currentPlayer.getSchoolBoard().load_entrance(c,model.getTable().getClouds());
                                break;
                            }
                        }
                        model.getTable().getClouds().removeIf(cloud -> (cloud.getStudentsAccumulator().size()==0));
                    }
                    else {
                        ping.interrupt();
                    }
                }
            }
        }

        if(!currentPlayer.isDisconnected()) {

            currentPlayerData = json.fromJson(message.getParameter(0), ClientModel.class);
            Cloud cloud = currentPlayerData.getCloudChoosed();
            currentPlayer.getSchoolBoard().load_entrance(cloud, model.getTable().getClouds());
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
                    for (Player p : model.getPlayers()) {
                        ClientModel Data = connectionModel.findPlayer(p.getNickname());

                        Data.setTypeOfRequest("TRYTORECONNECT");
                        Data.setServermodel(model);
                        Data.setResponse(false);
                        Data.setPingMessage(false);

                        Network.send(json.toJson(Data));
                    }

                    TimeUnit.SECONDS.sleep(40);

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
                        model.setDisconnection(true);
                        gameEnd().fireStateEvent();
                        return super.entryAction(cause);
                    }
                }
            }
        }
        if (model.getcurrentPlayer().equals(model.getPlayers().get(model.getPlayers().size() - 1))) {
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

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    public void setFromPing(boolean fromPing) {
        this.fromPing = fromPing;
    }
}

class CloudThread extends Thread {
    private final CloudPhase phase;
    private final ClientModel CurrentPlayerData;
    private final Gson json;

    protected CloudThread(CloudPhase phase,ClientModel CurrentPlayerData) {
        this.phase = phase;
        this.CurrentPlayerData=CurrentPlayerData;
        json=new Gson();
    }

    public void run() {
        while (!phase.getMessage().parametersReceived() || json.fromJson(phase.getMessage().getParameter(0), ClientModel.class).isPingMessage()) {
            try {
                sleep(15000);
            } catch (InterruptedException e) {
                return;
            }
            System.out.println("ping sended");
            CurrentPlayerData.setResponse(false); // è una richiesta non una risposta// lato client avrà una nella CliView un metodo per gestire questa richiesta
            CurrentPlayerData.setPingMessage(true);
            try {
                Network.send(json.toJson(CurrentPlayerData));
            } catch (InterruptedException e) {
                return;
            }

            long start = System.currentTimeMillis();
            long end = start + 10 * 1000;
            ParametersFromNetwork pingmessage = new ParametersFromNetwork(1);
            pingmessage.enable();

            while (!pingmessage.parametersReceived() && System.currentTimeMillis() < end) {
            }
            synchronized (phase) {
                if (!pingmessage.parametersReceived()) {
                    phase.setDisconnected(true);
                    return;
                }
                if (!json.fromJson(pingmessage.getParameter(0), ClientModel.class).isPingMessage()) {
                    phase.setMessage(pingmessage);
                    phase.setFromPing(true);
                    return;
                }
            }
        }
    }
}