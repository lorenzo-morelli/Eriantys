package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.MessageReceived;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class ConnectToServer extends State{
    Model model;
    View view;
    ConnectedToServer connected ;
    NotConnectedToServer notConnected;

    public ConnectToServer(View view, Model model){
        super("[STATO Tentativo di connessione al server (ConnectToServer.java)]");
        this.view = view;
        this.model = model;

        connected = new ConnectedToServer();
        notConnected = new NotConnectedToServer();

    }



    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {

        view.setCallingState(this);
        view.showTryToConnect();
        Network.setupClient(model.getIp(),model.getPort());
        if (Network.isConnected()){
            connected.fireStateEvent();
        }
        else{
            notConnected.fireStateEvent();
        }
        return super.entryAction(cause);
    }

        // avvia timeout
        //todo: connettiti al server (indirizzo Ip, numero di porta NOTI) tramite connection info dati
        //todo: attendi che server accetti la connessione:
        //se timeout scade chiama:

    public ConnectedToServer Connected_to_server() {
        return connected;
    }

    public NotConnectedToServer Connection_to_server_failed() {
        return notConnected;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
    }

}
