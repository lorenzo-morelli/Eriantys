package it.polimi.ingsw.server.states;

import it.polimi.ingsw.server.model.ConnectionInfo;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class WaitGameCreation extends State {
    private ConnectionInfo connectionInfo;

    private ParametersFromNetwork create, connect;
    public WaitGameCreation(ConnectionInfo connectionInfo) {
        super("[Il server è in attesa della creazione della partita]");
        create = new ParametersFromNetwork(5); // ricevo "client_ip Nickname CREATE gamemode numOfPlayers"
        connect = new ParametersFromNetwork(3); // ricevo "client_ip Nickname CONNECT"
        this.connectionInfo = connectionInfo;
    }

    public ParametersFromNetwork getConnect() {
        return connect;
    }

    public ParametersFromNetwork getCreate() {
        return create;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        System.out.println("[Non ho ancora ricevuto niente]");
        while(!create.parametersReceived() && !connect.parametersReceived()){
            // non ho ricevuto ne un messaggio di creazione ne uno di creazione
        }
        System.out.println("[Ho ricevuto qualcosa]");
        if (create.parametersReceived()){
            connectionInfo.setNewIp(create.getParameter(0));
            connectionInfo.setNewNickname(create.getParameter(1));
            System.out.println("[Paramentro ricevuto]");
            create.fireStateEvent();
        }
        else if (connect.parametersReceived()){
            connectionInfo.setNewIp(connect.getParameter(0));
            connectionInfo.setNewNickname(connect.getParameter(1));
            Network.send(connectionInfo.getNewIp()+ " GAME_NOT_EXIST");
            System.out.println("[Sent response that Game Not Exists and needs to be created]");
            connect.fireStateEvent();
        }
        return super.entryAction(cause);
    }
}
