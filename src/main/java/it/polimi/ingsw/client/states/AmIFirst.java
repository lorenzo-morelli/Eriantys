package it.polimi.ingsw.client.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class AmIFirst extends State {
    private ClientModel clientModel;
    private Gson json;
    private ParametersFromNetwork response;
    private Event yes, no, networkError;

    public AmIFirst(ClientModel clientModel, Controller controller) {
        super("[Il client chiede al server se è il primo ad essersi collegato (amIFirst.java)]");
        response = new ParametersFromNetwork(1);
        this.clientModel = clientModel;
        yes = new Event("Sono il primo client");
        no = new Event("Non sono il primo client");
        networkError = new Event("Il messaggio è stato perso, si prega di reinviarlo");
        json = new Gson();

        yes.setStateEventListener(controller);
        no.setStateEventListener(controller);
        networkError.setStateEventListener(controller);
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        // invio al server il mio modello
        Network.send(json.toJson(clientModel));

        System.out.println("[Non so ancora se sono il primo o no]");
        while( !response.parametersReceived()){
            // Non ho ancora ricevuto una risposta dal server
        }
        System.out.println("[Ho ricevuto qualcosa]");
        clientModel = json.fromJson(response.getParameter(0), ClientModel.class);
        if (clientModel.getAmIfirst().equals(null)){
            networkError.fireStateEvent();
        }
        else if (clientModel.getAmIfirst().equals(true)){
            yes.fireStateEvent();
        }
        else if (clientModel.getAmIfirst().equals(false)){
            no.fireStateEvent();
        }
        return super.entryAction(cause);
    }

    public Event networkError() {
        return networkError;
    }

    public Event no() {
        return no;
    }

    public Event yes() {
        return yes;
    }
}
