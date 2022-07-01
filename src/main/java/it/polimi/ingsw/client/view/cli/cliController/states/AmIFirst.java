package it.polimi.ingsw.client.view.cli.cliController.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.util.concurrent.TimeUnit;

/**
 * This client state makes a request to the server to find out if it is actually the
 * first client to connect. The server can respond in three ways: you are the first,
 * you are not the first, or you have entered an invalid nickname and please enter
 * a new one. So this state does nothing more than read the server response and
 * forward to the client view. It also handles various disconnection situations.
 * @author Ignazio Neto Dell'Acqua
 * @author Fernando Morea
 */

public class AmIFirst extends State {
    private ClientModel clientModel;
    private final Gson json;
    private ParametersFromNetwork response;
    private final Event yes;
    private final Event no;
    private final Event nicknameAlreadyPresent;

    public AmIFirst(ClientModel clientModel, Controller controller) {
        super("[The client ask to server if it is the first (AmIFirst.java)]");
        response = new ParametersFromNetwork(1);
        this.clientModel = clientModel;
        yes = new Event("I am the first");
        no = new Event("I am not the first");
        nicknameAlreadyPresent = new Event("Exist a player with the same nickname");
        json = new Gson();

        yes.setStateEventListener(controller);
        no.setStateEventListener(controller);
        nicknameAlreadyPresent.setStateEventListener(controller);
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        Network.setClientModel(clientModel);
        boolean responseReceived = false;

        System.out.println("You are connected to the server, if a game is available you will be automatically connected\n" +
                "otherwise it means that the server is full and cannot host other players");
        TimeUnit.MILLISECONDS.sleep(500);

        long start = System.currentTimeMillis();
        long end = start + 17 * 1000L;

        Network.send(json.toJson(clientModel));
        while (!responseReceived) {
            response = new ParametersFromNetwork(1);
            response.enable();
            boolean check = response.waitParametersReceived(5);

            if (check || System.currentTimeMillis() >= end) {
                System.out.println("\n\nServer did not give an answer, you can see that the game was already full or the game has not been created yet or that the nickname is not valid, please try again");
                Network.disconnect();
                System.exit(0);
            }


            if (response.getParameter(0)!=null && json.fromJson(response.getParameter(0), ClientModel.class).getClientIdentity() == clientModel.getClientIdentity()) {
                responseReceived = true;
            }
        }

        clientModel = json.fromJson(response.getParameter(0), ClientModel.class);

        if (clientModel.getClientIdentity() == Network.getClientModel().getClientIdentity() && clientModel.isNotKicked() && clientModel.getTypeOfRequest() != null && clientModel.getTypeOfRequest().equals("CONNECTTOEXISTINGGAME")) {
            System.out.println("\n You have been connected to an existing game.");
            no.fireStateEvent();
            return super.entryAction(cause);
        }

        if (clientModel.getAmFirst() == null) {
            nicknameAlreadyPresent.fireStateEvent();
        } else if (clientModel.getAmFirst().equals(true)) {
            yes.fireStateEvent();
        } else if (clientModel.getAmFirst().equals(false)) {

            System.out.println("Waiting for the other players to log in ...");
            no.fireStateEvent();
        }
        return super.entryAction(cause);
    }

    public Event nicknameAlreadyPresent() {
        return nicknameAlreadyPresent;
    }

    public Event no() {
        return no;
    }

    public Event yes() {
        return yes;
    }
}
