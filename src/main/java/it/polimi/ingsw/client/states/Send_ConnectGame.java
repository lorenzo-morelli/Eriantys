package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class Send_ConnectGame extends Send_to_Server {
    public Send_ConnectGame(View view, Model model, String type) {
        super(view, model, type);
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        view.showConnectingGame();
        Network.send(Network.getMyIp()+" "+ "CONNECT "+ model.getNickname());
        // avvia timeout
        // CONTESTO: lato server inserisce o meno il player nella partita e invia ack
        // todo: aspetta -> se ricevuto ack CONNESSO fai
        Recevied_ack();
        // -> se ricevuto ack NONCONNESSO fai
        Message_not_valid();
        // try catch: se scade timeout
        send_failed();
        return null;
    }
}