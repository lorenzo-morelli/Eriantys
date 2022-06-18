package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.view.cli.CLIcontroller.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.PingThread.CloudThread;
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
        disconnected=false; //flag che indica se si disconnette durante questo turno
        fromPing=false; //risposta non proviene da ping

        //salto fase se player se si è disconnesso precedentemente

        if(currentPlayer.isDisconnected()){  //se giocatore è disconnesso lo salto

            currentPlayer.setDisconnected(true);
            for(Cloud c: model.getTable().getClouds()) {
                if(c.getStudentsAccumulator().size()!=0) {
                    currentPlayer.getSchoolBoard().load_entrance(c,model.getTable().getClouds());
                    break;
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

        // retrive data of the current player

        ClientModel currentPlayerData = connectionModel.findPlayer(currentPlayer.getNickname());
        currentPlayerData.setServermodel(model);
        currentPlayerData.setTypeOfRequest("CHOOSECLOUDS");
        currentPlayerData.setPingMessage(false);
        currentPlayerData.setResponse(false); //non è una risposta, è una richiesta del server al client

        //invio e controllo che invio network sia fatto correttamente

        boolean chekDisco= Network.send(json.toJson(currentPlayerData));

        // se invio non va a buon fine salta il giocatore

        if(!chekDisco){
            if (model.getcurrentPlayer().equals(model.getPlayers().get(model.getPlayers().size() - 1))) {
                GoToEndTurn().fireStateEvent();
            } else {
                model.nextPlayer();
                GoToStudentPhase().fireStateEvent();
            }
            return super.entryAction(cause);
        }

        //controllo ricezione risposta invio ping e settaggio del giocatore in disconnessione in caso di ricezione ping fallita

        Thread ping = new CloudThread(this,currentPlayerData);
        ping.start();

        boolean responseReceived = false;
        while (!responseReceived) {
            //System.out.println("another one");
                if(!fromPing) {
                    message = new ParametersFromNetwork(1);
                    message.enable();
                }
            while (!message.parametersReceived()) {
                //System.out.println("loop");
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
                                currentPlayer.getSchoolBoard().load_entrance(c,model.getTable().getClouds());
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

        //codice effettivo della fase se non si è disconnesso

        if(!currentPlayer.isDisconnected()) {

            currentPlayerData = json.fromJson(message.getParameter(0), ClientModel.class);
            Cloud cloud = currentPlayerData.getCloudChoosed();
            currentPlayer.getSchoolBoard().load_entrance(cloud, model.getTable().getClouds());

        }

        //codice per disconnessione durante questo turno

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
                    System.out.println("numero minimo di giocatori non disponibile, attendo 40 secondi in attesa che un altro giocatore si riconnette");
                    for (Player p : model.getPlayers()) {
                        if(!p.isDisconnected()) {
                            ClientModel Data = connectionModel.findPlayer(p.getNickname());

                            Data.setTypeOfRequest("TRYTORECONNECT");
                            Data.setServermodel(model);
                            Data.setResponse(false);
                            Data.setPingMessage(false);
                            System.out.println("send to"+p.getNickname());

                            Network.send(json.toJson(Data));
                        }
                    }

                    model.setDisconnection(true);
                    TimeUnit.MILLISECONDS.sleep(40000); //aspetto 40 secondi nella speranza che qualcuno si riconnetta

                    if (model.isDisconnection()) {
                        gameEnd().fireStateEvent();
                        return super.entryAction(cause);
                    }
                    System.out.println("un giocatore si è riconnesso, la partita può ricominciare");
                }
            }
        }

        //gestione scoppio eventi per fase successiva

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