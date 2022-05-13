package it.polimi.ingsw.client.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.*;

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
    public IEvent entryAction(IEvent cause) throws Exception {
        Network.send(json.toJson(clientModel));
        checkAck();
        return super.entryAction(cause);
    }

    public ParametersFromNetwork getAck() {
        return ack;
    }

    public void checkAck() throws Exception {

        ack.enable();
        while (!ack.parametersReceived()){
            // non ho ancora ricevuto l'ack
        }
        //System.out.println("[Conferma ricevuta]");

        System.out.println("In attesa che gli altri giocatori si colleghino...");
        ack.fireStateEvent();

    }
}
