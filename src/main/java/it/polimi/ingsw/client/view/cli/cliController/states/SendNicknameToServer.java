package it.polimi.ingsw.client.view.cli.cliController.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

/**
 * Send the client nickname to the server, and handle the case in which the server reports the existence of another
 * client with the same nickname, by forwarding to the client view asking to choose another nickname.
 * @author Ignazio Neto Dell'Acqua
 * @author Fernando Morea
 */
public class SendNicknameToServer extends State {
    private final Gson json;
    private final ClientModel clientModel;
    private final Controller controller;
    private ParametersFromNetwork ack;
    private final Event nickAlreadyExistent;
    private final Event nickUnique;

    public SendNicknameToServer(ClientModel clientModel, Controller controller) {
        super("[Send the data to the server and wait ak]");
        this.clientModel = clientModel;
        this.controller = controller;
        ack = new ParametersFromNetwork(1);
        json = new Gson();
        nickAlreadyExistent = new Event("Nickname already existent");
        nickUnique = new Event("Nick unique");
        nickAlreadyExistent.setStateEventListener(controller);
        nickUnique.setStateEventListener(controller);
    }

    public Event nickAlreadyExistent() {
        return nickAlreadyExistent;
    }

    public Event nickUnique() {
        return nickUnique;
    }


    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        System.out.println("[Send new Nickname to the Server]");
        Network.send(json.toJson(clientModel));
        try {
            checkAck();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return super.entryAction(cause);
    }


    public void checkAck() throws Exception {
        ack = new ParametersFromNetwork(1);
        ack.setStateEventListener(controller);
        ack.enable();
        ack.waitParametersReceived();

        ClientModel fromNetwork = json.fromJson(ack.getParameter(0), ClientModel.class);

        if (fromNetwork.getAmFirst() == null) {
            nickAlreadyExistent.fireStateEvent();
        } else {

            System.out.println("Waiting for the other players to log in ...");
            nickUnique.fireStateEvent();
        }

    }
}
