package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.controller.events.ClientDisconnection;
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
    private final Event goToEndTurn;
    private final Event goToStudentPhase;

    private final ConnectionModel connectionModel;

    private final Gson json;
    private final ServerController serverController;
    private final Event reset = new ClientDisconnection();

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
        Controller controller = ServerController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        goToEndTurn= new Event("go to end turn");
        reset.setStateEventListener(controller);
        goToEndTurn.setStateEventListener(controller);
        goToStudentPhase= new Event("go to student phase");
        goToStudentPhase.setStateEventListener(controller);

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
            while (!message.parametersReceived()) {}
            if(json.fromJson(message.getParameter(0),ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity()){
                responseReceived = true;
            }
        }

        currentPlayerData = json.fromJson(message.getParameter(0),ClientModel.class);
        Cloud cloud = currentPlayerData.getCloudChoosed();
        currentPlayer.getSchoolBoard().load_entrance(cloud, model.getTable().getClouds());
        if(model.getcurrentPlayer().equals(model.getPlayers().get(model.getPlayers().size()-1))) {
            GoToEndTurn().fireStateEvent();
        }
        else{
            model.nextPlayer();
            GoToStudentPhase().fireStateEvent();
        }

        return super.entryAction(cause);
    }
}