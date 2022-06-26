package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

/**
 * State in which the server is waiting for the game modes and the number
 * of players from the first player connected to the server.
 */
public class WaitFirstPlayerGameInfo extends State {
    private final Gson json;
    private Controller controller;
    private final ConnectionModel connectionModel;

    private final ParametersFromNetwork message;
    private final Event reset;


    /**
     * Main constructor
     * @param serverController the main server controller
     */
    public WaitFirstPlayerGameInfo(ServerController serverController) {
        super("[Server is waiting for the Game Mode and the Number of player]");
        json = new Gson();
        message = new ParametersFromNetwork(1);
        message.setStateEventListener(controller);
        this.controller = ServerController.getFsm();
        reset = new Event("Network issues, server goes to WaitForPlayerConnection");
        reset.setStateEventListener(controller);
        this.connectionModel = serverController.getConnectionModel();
    }

    /**
     * Events callers
     * @return different events in order to change to different phase
     */
    public Event getReset() {
        return reset;
    }
    public ParametersFromNetwork gotNumOfPlayersAndGameMode() {
        return message;
    }

    /**
     * Sends a request to the view of the first player to make it able to choose the game mode and the number of player of the game.
     * @param cause the event that caused the controller transition in this state
     * @return null event
     * @throws Exception input output or network related exceptions
     */
    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        boolean messageReceived = false;

        long start = System.currentTimeMillis();
        long end = start + 15 * 1000L;

        while (!messageReceived) {
            message.enable();
            boolean check =message.waitParametersReceived(20);

            if((check || System.currentTimeMillis()>=end) && Network.disconnectedClient()){
                System.out.println("\n\nPlayer don't give any response, server will be restarted");
                connectionModel.close();
                getReset().fireStateEvent();
                return super.entryAction(cause);
            }
            if (json.fromJson(message.getParameter(0), ClientModel.class).getClientIdentity() == connectionModel.getClientsInfo().get(0).getClientIdentity()) {
                messageReceived = true;
            }

        }
        System.out.println("[First Player sent Game Mode and Number of Player]");
        if (message.parametersReceived()) {

            ClientModel clientModel = json.fromJson(message.getParameter(0), ClientModel.class);

            ClientModel target= connectionModel.findPlayer(clientModel.getNickname());
            connectionModel.change(target,clientModel);

            connectionModel.getClientsInfo().removeIf(c -> c!=clientModel);

            System.out.println("GAME_MODE: " + clientModel.getGameMode() + "   PLAYERS: " + clientModel.getNumOfPlayers());

            Network.send(json.toJson(clientModel));
            message.fireStateEvent();
            message.disable();
        }
        return super.entryAction(cause);
    }
}

