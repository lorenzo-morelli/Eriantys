package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class Send_MotherMovement extends Send_to_Server {
    public Send_MotherMovement(View view, ClientModel clientModel, String type) {
        super(view, clientModel, type);
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        Network.send("MOTHER_TOISLAND " + clientModel.getMother_movement_Choosed());
        // avvia timeout
        // CONTESTO: lato server inserisce studente nella school board del client ,invia ack ,poi esegue funzioni correlate
        // todo: aspetta -> se ricevuto ack MOTHER_MOVED fai
        try{
            ack.fireStateEvent();
            // -> se ricevuto ack INVALIDMESSAGE fai
            not_valid.fireStateEvent();
            // try catch: se scade timeout
            //message_issue.fireStateEvent(); TODO
        }
        catch(InterruptedException e){ }
        return null;
    }
}