package it.polimi.ingsw.client.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AmIFirst extends State {
    private ClientModel clientModel;
    private final Gson json;
    private ParametersFromNetwork response;
    private final Event yes;
    private final Event no;
    private final Event nicknameAlreadyPresent;

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
        // Ci interessa sapere se il server segnala una disconnessione di qualche client
        // memorizziamo questo segnale in arrivo dal server in Network.disconnectedClient
        Network.setClientModel(clientModel);
        boolean responseReceived = false;

        System.out.println("Sei connesso al server, se è disponibile una partita verrai automaticamente collegato\n" +
                "altrimenti vuoi dire che il server è al completo e non può ospitare altri giocatori");

        //System.out.println("Attendo x secondi per non intasare la rete...");
        //TimeUnit.MILLISECONDS.sleep(4500);

        long start = System.currentTimeMillis();
        long end = start + 15 * 1000L;

        Network.send(json.toJson(clientModel));
        while (!responseReceived) {
            // invio al server il mio modello
            //System.out.println("another one");
            response = new ParametersFromNetwork(1);
            response.enable();
            boolean check =response.waitParametersReceived(5);

            if(check || System.currentTimeMillis()>=end){
                System.out.println("\n\nServer non ha dato risposta, si vede che il gioco era già pieno oppure la partita non è stata ancora creata oppure che il nickname non è valido... si prega di riprovare");
                Network.disconnect();
                System.exit(0);
            }


            // se il messaggio è rivolto a me allora ho ricevuto l'ack, altrimenti reinvio e riattendo
            if (json.fromJson(response.getParameter(0), ClientModel.class).getClientIdentity() == clientModel.getClientIdentity()) {
                responseReceived = true;
            }
        }


        //System.out.println("[Ho ricevuto la risposta: ");
        clientModel = json.fromJson(response.getParameter(0), ClientModel.class);

        if(clientModel.getClientIdentity() == Network.getClientModel().getClientIdentity() && clientModel.NotisKicked() && clientModel.getTypeOfRequest() != null && clientModel.getTypeOfRequest().equals("CONNECTTOEXISTINGGAME")) {
            System.out.println("\n Sei stato connesso a una partita già esistente.");
            no.fireStateEvent();
            return super.entryAction(cause);
        }

        if (clientModel.getAmIfirst() == null ) {
            nicknameAlreadyPresent.fireStateEvent();
            //System.out.println("   il nickname era già presente !!!]");
        } else if (clientModel.getAmIfirst().equals(true)) {
            yes.fireStateEvent();
            //System.out.println("   sono primo !!!]");
        } else if (clientModel.getAmIfirst().equals(false)) {

            System.out.println("In attesa che gli altri giocatori si colleghino...");
            no.fireStateEvent();
            //System.out.println("   non sono primo !!!]");
        }
        return super.entryAction(cause);
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {

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
