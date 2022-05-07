package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class ConnectToServer extends State{
    ClientModel clientModel;
    View view;
    ConnectedToServer connected ;
    NotConnectedToServer notConnected;

    public ConnectToServer(View view, ClientModel clientModel){
        super("[STATO Tentativo di connessione al server (ConnectToServer.java)]");
        this.view = view;
        this.clientModel = clientModel;

        connected = new ConnectedToServer();
        notConnected = new NotConnectedToServer();

    }



    public IEvent entryAction(IEvent cause) throws Exception {

        view.setCallingState(this);
        Network.setupClient(clientModel.getIp(), clientModel.getPort());
        clientModel.setMyIp(Network.getMyIp());

        if (Network.isConnected()){
            connected.fireStateEvent();
        }
        else{
            notConnected.fireStateEvent();
        }
        return super.entryAction(cause);
    }

    public ConnectedToServer connectedToServer() {
        return connected;
    }

    public NotConnectedToServer connectionToServerFailed() {
        return notConnected;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
    }

}
