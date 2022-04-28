package it.polimi.ingsw.server.states;

import it.polimi.ingsw.utils.network.SpecificMessageEvent;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class WaitSpecificMessage extends State {

    private SpecificMessageEvent newmessage;
    public WaitSpecificMessage() {
        super("[Il server è in attesa di un messaggio specifico]]");
        newmessage = new SpecificMessageEvent("CREATE");
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        while(!newmessage.messageReceived()){
                // messaggio non ancora ricevuto

        }
        newmessage.fireStateEvent();
        return super.entryAction(cause);
    }
}
