package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class Send_CardChoosed extends Send_to_Server {
    public Send_CardChoosed(View view, Model model, String type) {
        super(view, model, type);
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        Network.send("CARDCHOOSED " + model.getCardChoosed());
        // avvia timeout
        // CONTESTO: lato server inserisce carta tra le carte scelte per il client e invia ack,  poi esegue funzioni correlate
        // todo: aspetta -> se ricevuto ack CARTASCELTA fai
        Recevied_ack();
        // -> se ricevuto ack INVALIDMESSAGE fai
        Message_not_valid();
        // try catch: se scade timeout
        send_failed();
        return null;
    }
}
