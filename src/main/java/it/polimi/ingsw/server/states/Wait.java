package it.polimi.ingsw.server.states;

import it.polimi.ingsw.utils.network.MessageEvent;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class Wait extends State {

    private MessageEvent newmessage;
    public Wait() {
        super("[Il server è in attesa di eventi]]");
        newmessage = new MessageEvent("CREATE");
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
