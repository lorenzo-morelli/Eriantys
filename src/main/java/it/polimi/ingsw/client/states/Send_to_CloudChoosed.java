package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class Send_to_CloudChoosed extends Send_to_Server {
    public Send_to_CloudChoosed(View view, Model model, String type) {
        super(view, model, type);
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        Network.send("CLOUDCHOOSED " + model.getCloudChoosed());
        // avvia timeout
        // CONTESTO: lato server arriva la scelta della nuvola e invia ack,  poi esegue funzioni correlate
        // todo: aspetta -> se ricevuto ack NUVOLASCELTA fai
        Recevied_ack();
        // -> se ricevuto ack INVALIDMESSAGE fai
        Message_not_valid();
        // try catch: se scade timeout
        send_failed();
        return null;
    }
}
