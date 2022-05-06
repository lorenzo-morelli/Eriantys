package it.polimi.ingsw.client.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.MessageReceived;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

/**
 * Generico stato di invio dati al server
 * Attende la ricezione di un generico ack dal server
 */
public class SendToServer extends State{
    private Gson json;
    private ClientModel clientModel;
    private ParametersFromNetwork ack;
    public SendToServer(ClientModel clientModel, Controller controller) {
        super("[Invio dati al server ed attesa di un ack]");
        this.clientModel = clientModel;
        ack = new ParametersFromNetwork(1);
        ack.setStateEventListener(controller);
        json = new Gson();
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        Network.send(json.toJson(clientModel));
        checkAck();
        return super.entryAction(cause);
    }

    public ParametersFromNetwork getAck() {
        return ack;
    }

    public void checkAck() throws IOException, InterruptedException {

        ack.enable();
        while (!ack.parametersReceived()){
            // non ho ancora ricevuto l'ack
        }
        System.out.println("[Conferma ricevuta]");
        ack.fireStateEvent();

    }
}
