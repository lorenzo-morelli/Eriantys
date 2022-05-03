package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class Send_ConnectGame extends Send_to_Server {
    public Send_ConnectGame(View view, ClientModel clientModel, String type) {
        super(view, clientModel, type);
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException{
        view.setCallingState(this);
        view.showConnectingGame();
        Network.send(Network.getMyIp()+" "+ "CONNECT "+ clientModel.getNickname());
        // avvia timeout
        // CONTESTO: lato server inserisce o meno il player nella partita e invia ack
        // todo: aspetta -> se ricevuto ack CONNESSO fai
        try{
            ack.fireStateEvent();
        // -> se ricevuto ack NONCONNESSO fai
            not_valid.fireStateEvent();
        // try catch: se scade timeout
            //message_issue.fireStateEvent(); TODO
        }
        catch(InterruptedException e){ }
        return null;
    }
}