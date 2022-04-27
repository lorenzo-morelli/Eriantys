package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class ConnectionToServer extends State {
    private View view;
    private Model model;
    private Network network;

    ConnectedToServer connected = new ConnectedToServer();
    NotConnectedToServer notConnected = new NotConnectedToServer();


    public ConnectionToServer(View view, Network network, Model model) {
        super("[Tentativo di connessione al server]");
        this.view = view;
        this.network = network;
        this.model = model;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        view.setCallingState(this);
        network.setupClient(model.getIp(), model.getPort());
        if (network.isConnected()){
            connected.fireStateEvent();
        }
        else{
            notConnected.fireStateEvent();
        }
        return super.entryAction(cause);
    }

    public ConnectedToServer connected() {
        return connected;
    }

    public NotConnectedToServer notConnected() {
        return notConnected;
    }
}
