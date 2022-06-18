package it.polimi.ingsw.utils.stateMachine;

import java.io.IOException;

public class State implements IState {

    private final String name;

    /**
     * Constructor to force the user to name the state
     * @param name name used for logging purposes
     */
    public State(String name) {
        this.name = name;
    }

    /**
     * This event will be called when going to this state. If this
     * state is the Initial state (passed to the ControllerServer constructor)
     * will not be called.
     *
     * If a value is returned, this status + the returned event MUST
     * be in the transaction table (to ensure that the transaction table is complete)
     *
     *
     * @param cause
     * @return any event but NULL causes the system to go straight to that state.
     */
    public IEvent entryAction(IEvent cause) throws Exception {
        return null;
    }

    /**
            * Like entryActon, but called while exiting this state.
      * This cannot cause a change of state by returning an event
      * @param cause the event that caused us to exit this state
      */
    public void exitAction(IEvent cause) throws IOException {

    }

    /**
     * for logging purposes
     * @return the name passed into the constructor.
     */
    public String toString() {
        return name;
    }
}

