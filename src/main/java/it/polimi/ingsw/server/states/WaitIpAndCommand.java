package it.polimi.ingsw.server.states;

import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.network.events.SpecificMessageEvent;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class WaitIpAndCommand extends State {

    private ParametersFromNetwork newMessage;
    public WaitIpAndCommand() {
        super("[Il server è in attesa di un messaggio specifico]]");
        newMessage = new ParametersFromNetwork(2);
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        while(!newMessage.parametersReceived()){
            // messaggio non ancora ricevuto
        }
        newMessage.fireStateEvent();
        return super.entryAction(cause);
    }
}
