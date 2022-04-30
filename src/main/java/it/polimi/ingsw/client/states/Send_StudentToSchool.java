package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class Send_StudentToSchool extends Send_to_Server {
    public Send_StudentToSchool(View view, Model model, String type) {
        super(view, model, type);
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        Network.send("STUDENT_TOSCHOOL " + model.getStudent_in_entrance_Choosed());
        // avvia timeout
        // CONTESTO: lato server inserisce studente nella school board del client ,invia ack ,poi esegue funzioni correlate
        // todo: aspetta -> se ricevuto ack STUDENT_PLACED fai
        Recevied_ack();
        // -> se ricevuto ack INVALIDMESSAGE fai
        Message_not_valid();
        // try catch: se scade timeout
        send_failed();
        return null;
    }
}
