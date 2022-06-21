package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.cli.CommandPrompt;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.util.ArrayList;

/**
 * State in which the server is waiting for the first client, which we can define as the initializer client,
 * that is the one that will decide the type of game (game mode) and the number of players.
 * It therefore plays a fundamental role in the business logic of the game.
 */
public class WaitFirstPlayer extends State {
    private final Gson json;
    private Controller controller;
    private final ConnectionModel connectionModel;
    private final ParametersFromNetwork firstMessage;
    private final Event reset;

    /**
     * Main constructor
     * @param serverController the main server controller
     */
    public WaitFirstPlayer(ServerController serverController) {
        super("[Server is waiting for the connection of the first player]");
        json = new Gson();
        firstMessage = new ParametersFromNetwork(1);
        firstMessage.setStateEventListener(controller);
        this.controller = ServerController.getFsm();
        reset = new Event("Network issues, server goes to WaitForPlayerConnection");
        reset.setStateEventListener(controller);
        this.connectionModel = serverController.getConnectionModel();
    }

    /**
     * Events callers
     * @return different events in order to change to different phase
     */
    public ParametersFromNetwork gotFirstMessage() {
        return firstMessage;
    }


    /**
     * Wait the connection of the first player and put it into the game. Then sends a request to the view of the first player to make it able to choose the game mode and the number of player of the game.
     * @param cause the event that caused the controller transition in this state
     * @return null event
     * @throws Exception input output or network related exceptions
     */
    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        if (Network.disconnectedClient()){
            Network.disconnect();
        }
        System.out.println("[Listening on port " + CommandPrompt.gotFromTerminal()+ " ]");
        Network.setupServer(CommandPrompt.gotFromTerminal());
        Network.setDisconnectedClient(false);
        connectionModel.setClientsInfo(new ArrayList<>());


        firstMessage.enable();

        while (!firstMessage.parametersReceived() ) {
            firstMessage.waitParametersReceived(10);
            if(Network.disconnectedClient()){
                reset.fireStateEvent();
            }
        }

        System.out.println("[First Player is connected]");
        if (firstMessage.parametersReceived()) {
            ClientModel clientModel = json.fromJson(firstMessage.getParameter(0), ClientModel.class);

            System.out.println("Player: " + clientModel.getNickname() + " " + clientModel.getMyIp());
            connectionModel.getClientsInfo().add(0, clientModel);
            clientModel.setAmIfirst(true);
            Network.send(json.toJson(clientModel));
            System.out.println("[Ack sent to the first player]");

            firstMessage.fireStateEvent();
            firstMessage.disable();
        }
        return super.entryAction(cause);
    }
}
