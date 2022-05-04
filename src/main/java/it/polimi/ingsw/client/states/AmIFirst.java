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
    private Event yes, no, nicknameAlreadyPresent;

    public AmIFirst(ClientModel clientModel, Controller controller) {
        super("[Il client chiede al server se è il primo ad essersi collegato (AmIFirst.java)]");
        response = new ParametersFromNetwork(1);
        this.clientModel = clientModel;
        yes = new Event("Sono il primo client");
        no = new Event("Non sono il primo client");
        nicknameAlreadyPresent = new Event("Esiste gia un client con lo stesso nome");
        json = new Gson();

        yes.setStateEventListener(controller);
        no.setStateEventListener(controller);
        nicknameAlreadyPresent.setStateEventListener(controller);
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        // invio al server il mio modello
        System.out.println("[Chiedo al server se sono il primo client]");
        Network.send(json.toJson(clientModel));

        response.enable();
        while( !response.parametersReceived()){
            // Non ho ancora ricevuto una risposta dal server
        }
        response.disable();

        System.out.println("[Ho ricevuto la risposta]");
        clientModel = json.fromJson(response.getParameter(0), ClientModel.class);
        if (clientModel.getAmIfirst() == null){
            nicknameAlreadyPresent.fireStateEvent();
        }
        else if (clientModel.getAmIfirst().equals(true)){
            yes.fireStateEvent();
        }
        else if (clientModel.getAmIfirst().equals(false)){
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
