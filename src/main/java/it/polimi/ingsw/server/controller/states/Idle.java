package it.polimi.ingsw.server.controller.states;

import it.polimi.ingsw.utils.stateMachine.State;

public class Idle extends State {
    /**
     * Idle state, as the name suggest, no functional operation
     * are done in this state, is just a label for "do no operation".
     */
    public Idle() {
        super("[STATO di IDLE]");
    }
}
