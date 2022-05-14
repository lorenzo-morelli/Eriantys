package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.Cloud;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

public class CloudPhase extends State {
    private Event goToEndTurn, goToStudentPhase;
    private Model model;

    private ConnectionModel connectionModel;
    private Controller controller;

    private Gson json;
    private ServerController serverController;

    private ParametersFromNetwork message;


    public Event GoToEndTurn() {
        return goToEndTurn;
    }

    public Event GoToStudentPhase() {
        return goToStudentPhase;
    }

    public CloudPhase(ServerController serverController) {
        super("[Choose Cloud]");
        this.serverController = serverController;
        this.controller = serverController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        goToEndTurn= new Event("go to end turn");
        goToEndTurn.setStateEventListener(controller);
        goToStudentPhase= new Event("go to student phase");
        goToStudentPhase.setStateEventListener(controller);

        json = new Gson();
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        model = serverController.getModel();
        // retrive the current player
        Player currentPlayer = model.getcurrentPlayer();
        // retrive data of the current player
        ClientModel currentPlayerData = connectionModel.findPlayer(currentPlayer.getNickname());

        currentPlayerData.setServermodel(model);
        currentPlayerData.setTypeOfRequest("CHOOSECLOUDS");
        currentPlayerData.setResponse(false); //non è una risposta, è una richiesta del server al client

        Network.send(json.toJson(currentPlayerData));

        boolean responseReceived = false;

        while (!responseReceived) {
            message = new ParametersFromNetwork(1);
            message.enable();
            while (!message.parametersReceived()) {
                // il client non ha ancora scelto la carta assistente
            }
            if(json.fromJson(message.getParameter(0),ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity()){
                responseReceived = true;
            }
        }


        currentPlayerData = json.fromJson(message.getParameter(0),ClientModel.class);
        Cloud cloud = currentPlayerData.getCloudChoosed();
        currentPlayer.getSchoolBoard().load_entrance(cloud);
        model.nextPlayer();
        if(model.getcurrentPlayer().equals(model.getPlayers().get(model.getPlayers().size()-1))) {
            GoToEndTurn().fireStateEvent();
        }
        else{
            GoToStudentPhase().fireStateEvent();
        }

        return super.entryAction(cause);
    }
}