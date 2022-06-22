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
 * State in which the server is waiting for the remaining n - 1 players, where n is the number
 * of players chosen by the first player when selecting game modes.
 */

public class WaitOtherClients extends State {
    private final ConnectionModel connectionModel;
    private ClientModel clientModel = null;
    private final Gson json;
    private ParametersFromNetwork message;

    private final Event twoOrThreeClientsConnected;
    private final Event fourClientsConnected;

    public WaitOtherClients(ServerController serverController) {
        super("[Waiting other players]");
        this.connectionModel = serverController.getConnectionModel();
        Controller controller = ServerController.getFsm();
        json = new Gson();
        message = new ParametersFromNetwork(1);
        message.setStateEventListener(controller);
        fourClientsConnected = new Event("4 clients connected and ready to play");
        fourClientsConnected.setStateEventListener(controller);
        twoOrThreeClientsConnected = new Event("2 or 3 clients connected and ready to play");
        twoOrThreeClientsConnected.setStateEventListener(controller);
    }

    /**
     * Events callers
     *
     * @return different events in order to change to different phase
     */
    public Event twoOrThreeClientsConnected() {
        return twoOrThreeClientsConnected;
    }

    public Event fourClientsConnected() {
        return fourClientsConnected;
    }

    /**
     * Wait the connection of the other player and put it into the game. Then sends an ack to them.
     *
     * @param cause the event that caused the controller transition in this state
     * @return null event
     * @throws Exception input output or network related exceptions
     */
    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        int numOfPlayersToWait = connectionModel.getClientsInfo().get(0).getNumofplayer() - 1;

        while (numOfPlayersToWait > 0) {
            System.out.println("[Waiting for " + numOfPlayersToWait + " clients]");

            message = new ParametersFromNetwork(1);
            message.enable();
            while (!message.parametersReceived()) {
                message.waitParametersReceived(10);
            }
            clientModel = json.fromJson(message.getParameter(0), ClientModel.class);

            isNicknameAlreadyExistent();

            System.out.println("PLAYER: " + clientModel.getNickname() + " " + clientModel.getMyIp());

            clientModel.setAmIfirst(false);
            Network.send(json.toJson(clientModel));
            connectionModel.getClientsInfo().add(clientModel);

            numOfPlayersToWait--;

        }
        if (connectionModel.getClientsInfo().size() == 4) {
            fourClientsConnected.fireStateEvent();
        } else if (connectionModel.getClientsInfo().size() == 3 || connectionModel.getClientsInfo().size() == 2) {
            twoOrThreeClientsConnected.fireStateEvent();
        }
        return super.entryAction(cause);
    }

    /**
     * This method check if the nickname chose by the Client is valid. If it is not, send another request to change the nickname.
     */
    public void isNicknameAlreadyExistent() throws InterruptedException {
        for (ClientModel c : connectionModel.getClientsInfo()) {
            if (clientModel.getNickname().equals(c.getNickname())) {

                Network.send(json.toJson(clientModel));
                System.out.println("[Invalid Nickname]");

                ParametersFromNetwork nickname = new ParametersFromNetwork(1);


                boolean messageReceived = false;
                while (!messageReceived) {
                    nickname = new ParametersFromNetwork(1);
                    nickname.enable();
                    nickname.waitParametersReceived();
                    if (json.fromJson(nickname.getParameter(0), ClientModel.class).getClientIdentity() == clientModel.getClientIdentity()) {
                        messageReceived = true;
                    }
                }
                clientModel = json.fromJson(nickname.getParameter(0), ClientModel.class);
                isNicknameAlreadyExistent();
            }
        }
    }
}
