package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class ConnectToServer extends State{
    Model model;
    View view;
    Message_Received connected ;
    NetworkIssue notConnected;

    public ConnectToServer(View view, Model model){
        super("[STATO di connessione]");
        this.view = view;
        this.model = model;

        connected = new Message_Received("CONNECTED");
        notConnected = new NetworkIssue("CONNECTION_TO_SERVER_FAILED");

    }
    public Message_Received Connected_to_server(){ return connected;}
    public NetworkIssue Connection_to_server_failed(){ return notConnected;}



    public IEvent entryAction(IEvent cause) throws IOException {

        view.setCallingState(this);
        view.showTryToConnect();
        // avvia timeout
        //todo: connettiti al server (indirizzo Ip, numero di porta NOTI) tramite connection info dati
        //todo: attendi che server accetti la connessione:
        Connected_to_server();
        //se timeout scade chiama:
        Connection_to_server_failed();
        return null;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
    }

}
