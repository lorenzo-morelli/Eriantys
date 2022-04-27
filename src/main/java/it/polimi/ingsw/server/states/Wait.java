package it.polimi.ingsw.server.states;

import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class Wait extends State {
    private boolean noEvents = true;
    public Wait() {
        super("[Il server è in attesa di eventi]]");
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        while(noEvents){
            // non ci sono eventi da runnare, resto in wait
        }
        return super.entryAction(cause);
    }
}
