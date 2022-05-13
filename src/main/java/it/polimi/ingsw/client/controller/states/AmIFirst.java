package it.polimi.ingsw.client.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

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
    public IEvent entryAction(IEvent cause) throws Exception {
        boolean responseReceived = false;

        while(!responseReceived) {
            // invio al server il mio modello
            //System.out.println("[Chiedo al server se sono il primo client]");
            Network.send(json.toJson(clientModel));


            System.out.println("Sei connesso al server, se è disponibile una partita verrai automaticamente collegato\n" +
                    "altrimenti vuoi dire che il server è al completo e non può ospitare altri giocatori");
            response.enable();
            while (!response.parametersReceived()) {
                // Non ho ancora ricevuto una risposta dal server
            }

            // se il messaggio è rivolto a me allora ho ricevuto l'ack, altrimenti reinvio e riattendo
            if(json.fromJson(response.getParameter(0), ClientModel.class).getClientIdentity() == clientModel.getClientIdentity()){
                responseReceived = true;
            }
        }

        //System.out.println("[Ho ricevuto la risposta: ");
        clientModel = json.fromJson(response.getParameter(0), ClientModel.class);
        if (clientModel.getAmIfirst() == null){
            nicknameAlreadyPresent.fireStateEvent();
            //System.out.println("   il nickname era già presente !!!]");
        }
        else if (clientModel.getAmIfirst().equals(true)){
            yes.fireStateEvent();
            //System.out.println("   sono primo !!!]");
        }
        else if (clientModel.getAmIfirst().equals(false)){

            System.out.println("In attesa che gli altri giocatori si colleghino...");
            no.fireStateEvent();
            //System.out.println("   non sono primo !!!]");
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
