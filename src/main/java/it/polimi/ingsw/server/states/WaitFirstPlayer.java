package it.polimi.ingsw.server.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.server.ConnectionModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class WaitFirstPlayer extends State {
    private ClientModel clientModel = null;
    private Gson json;
    private Controller controller;
    private ConnectionModel connectionModel;

    private ParametersFromNetwork firstMessage;
    public WaitFirstPlayer(Controller controller, ConnectionModel connectionModel) {
        super("[Il server è in attesa del primo giocatore]");
        json = new Gson();
        firstMessage = new ParametersFromNetwork(1);
        firstMessage.setStateEventListener(controller);
        this.controller = controller;
        this.connectionModel = connectionModel;
    }

    public ParametersFromNetwork getFirstMessage() {
        return firstMessage;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        System.out.println("[Non ho ancora ricevuto niente]");

        firstMessage.enable();

        while(!firstMessage.parametersReceived() ){
            // non ho ricevuto ancora un messaggio dal primo player
        }
        System.out.println("[Il primo player si è connesso]");
        if (firstMessage.parametersReceived()){
            // Converti il messaggio stringa json in un oggetto clientModel
            clientModel = json.fromJson(firstMessage.getParameter(0), ClientModel.class);

            System.out.println("Ricevuto " + clientModel.getNickname() +" " +clientModel.getMyIp());
            // Appendi alla lista di ClientModel il modello appena ricevuto così da salvarlo per usi futuri
            connectionModel.getClientsInfo().add(clientModel);
            // Compila il campo "sei primo" e invia la risposta al client
            clientModel.setAmIfirst(true);
            Network.send(json.toJson(clientModel));
            System.out.println("[Inviato ack al primo player]");

            //scateno l'evento ed esco dallo stato
            firstMessage.fireStateEvent();
            firstMessage.disable();
        }
        return super.entryAction(cause);
    }
}
