package it.polimi.ingsw.utils.stateMachine;

import java.io.IOException;

/**
 * A default class to implement the IEvent interface.
 * What it does is take care of the listener and the toString method.
 */
public class Event implements IEvent {

    private String name;
    private Controller listener;

    private boolean enabled = false;

    /**
     * Constructor to force naming of each event.
     * @param eventName The name used for logging
     */
    public Event(String eventName) {
        name = eventName;
    }

    /**
     * This causes the controller to transition to the next state if
     * possible.  If there is no transaction for the current state + this
     * event, it will throw an IllegalStateException.
     */
    public void fireStateEvent() throws Exception {
            listener.fireStateEvent(this);
    }

    /**
     * The controller will set itself as listener for events from this
     * object.  I don't think it will ever need more than one listener,
     * hence the set instead of add; this should make it easier for
     * a developer to implement the interface (which will be useful if
     * you are also extending an event helper class)
     *
     * @param engine the controller that will get events
     */
    public void setStateEventListener(Controller engine) {
        listener = engine;
    }

    /**
     * Used for logging purposes
     * @return the name set in the constructor.
     */
    public String toString() {
        return name;
    }

    public void enable(){
        enabled = true;
    }

    public void disable(){
        enabled = false;
    }
}