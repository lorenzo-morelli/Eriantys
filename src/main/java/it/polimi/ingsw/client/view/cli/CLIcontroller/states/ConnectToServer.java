package it.polimi.ingsw.client.view.cli.CLIcontroller.states;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.cli.CLIcontroller.events.ConnectedToServer;
import it.polimi.ingsw.client.view.cli.CLIcontroller.events.NotConnectedToServer;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

/**
 * This state manages the actual connection phase with the server, by calling the utility class Network on
 * the server's ip-port specified by the user. If this connection fails the client view will again prompt
 * the user to enter a valid ip-port.
 */
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
