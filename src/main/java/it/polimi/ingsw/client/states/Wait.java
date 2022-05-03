package it.polimi.ingsw.client.states;

import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class Wait extends State {
    public Wait() {
        super("[Stato di attesa di comandi (aggiornamento vista o comandi di inserimento da terminale)]");

    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        while (true){

        }
        //return super.entryAction(cause);
    }
}
