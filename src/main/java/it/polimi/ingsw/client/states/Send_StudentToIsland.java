package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class Send_StudentToIsland extends Send_to_Server {
    public Send_StudentToIsland(View view, ClientModel clientModel, String type) {
        super(view, clientModel, type);
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        Network.send("STUDENT_TOISLAND " + clientModel.getStudent_in_entrance_Choosed() + " " + clientModel.getIsland_Choosed());
        // avvia timeout
        // CONTESTO: lato server inserisce studente nell'isola scelta ,invia ack ,poi esegue funzioni correlate
        // todo: aspetta -> se ricevuto ack STUDENT_PLACED fai
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

