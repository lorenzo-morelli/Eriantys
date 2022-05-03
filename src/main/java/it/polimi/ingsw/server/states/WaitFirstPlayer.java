package it.polimi.ingsw.server.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.server.model.ConnectionInfo;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class WaitFirstPlayer extends State {
    private ConnectionInfo connectionInfo;
    private ClientModel clientModel = null;
    Gson json;
    Controller controller;

    private ParametersFromNetwork firstMessage;
    public WaitFirstPlayer(ConnectionInfo connectionInfo, Controller controller) {
        super("[Il server è in attesa del primo giocatore]");
        firstMessage = new ParametersFromNetwork(1);
        this.connectionInfo = connectionInfo;
        json = new Gson();
        this.controller = controller;
    }

    public ParametersFromNetwork getCreate() {
        return firstMessage;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        System.out.println("[Non ho ancora ricevuto niente]");
        while(!firstMessage.parametersReceived() ){
            // non ho ricevuto ne un messaggio di creazione ne uno di creazione
        }
        System.out.println("[Il primo player si è connesso]");
        if (firstMessage.parametersReceived()){

            clientModel = json.fromJson(firstMessage.getParameter(0), ClientModel.class);
            clientModel.setAmIfirst(true);
            Network.send(json.toJson(clientModel));
            System.out.println("[Inviato ack al primo player]");


        }
        return super.entryAction(cause);
    }
}
