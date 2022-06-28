package it.polimi.ingsw.utils.stateMachine;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Objects;

/**
 * This class provides a mechanism for creating transitions between states
 * which create a finite state machine, which will be stored in a hashMap internally
 *
 * For example, if we wanted to use the keyboard to govern a finite state machine
 * if you only agree to write the word "lode", the table of states would be like this:
 *
 * States:
 * starting   l    lo   lod   lode (ending state)
 * eventi    |------------------------------|
 * l         |    >l      x    x      x     |
 *           |------------------------------|
 * o         |    x      >lo   x      x     |
 *           |------------------------------|
 * d         |    x       x  >lod     x     |
 *           |------------------------------|
 * e         |    x       x    x     >lode  |
 *           |------------------------------|
 *
 * 4 calls are required to store this table:
 *
 * addTransaction(state_starting, event_l, state_l)
 * addTransaction(state_l,        event_o, state_lo)
 * addTransaction(state_lo,       event_d, state_lod)
 * addTransaction(state_lod,      event_e, state_lode)
 *
 * After this configuration, the controller will be operated by itself, managing notifications
 * from its events and by sending actions to its states.
 *
 * Event queuing is allowed, even during an event. The events will be processed in order.
 *
 * If an "X" state is reached, the controller will either ignore it or throw an exception to the event that caused the error.
 *
 * @author Fernando Morea
 */

public class Controller {
    private boolean hideDebugMessages = true;

    /**
     * Stores all the transitions
     */
    private final StateTableWithNestedHashtable states = new StateTableWithNestedHashtable();
    /**
     * Current state of the controller
     */
    private IState currentState;
    /**
     * the name of the controller for debug purpose only
     */
    private final String name;
    /**
     * The event queue
     */
    private final LinkedList<IEvent> events = new LinkedList<>();

    /**
     * The entryAction of the first state will not be called, because no event triggered it
     * The exitAction will be called.
     *
     * @param firstState Initial state of the machine.
     * @param name       name of the machine used only for debug and log purpose
     */
    public Controller(String name, IState firstState) {
        this.name = name;
        if (firstState == null) {
            throw new IllegalArgumentException();
        }
        currentState = firstState;
    }

    /**
     * Program the controller with allowed transitions.
     *
     * The controller will always be in a state, when an event ends,
     * this table is queried to see if the event is valid in the context
     * of the current state, if the transition occurs.
     *
     * @param startingState If the controller is in this state
     * @param ev            And this event is triggered
     * @param nextState     then transition to this state
     */
    public void addTransition(IState startingState, IEvent ev, IState nextState) {
        ev.setStateEventListener(this);
        if (!hideDebugMessages) {
            System.out.println(this + " Transition (" + startingState + "," + ev + ") --> " + nextState + " added");
        }
        states.addTransition(startingState, ev, nextState);
    }

    /**
     * What activates the transitions.
     * Queue events, take first event, make sure it's valid, call
     * exitAction of the current state, change state and call the entryAction of the next state
     *
     * If a status change is invalid, it throws an "IllegalStateException"
     * containing the names of the initial state, final state and event.
     *
     * It should handle more threading events and situations well.
     * Events are handled in the order they are received.
     *
     * @param ev The triggered event
     */


    public void fireStateEvent(IEvent ev) throws Exception {
        synchronized (events) {
            // add the arrived event to the queue
            // the first event that arrives should be the first one executed
            events.addLast(ev);
            // If there are more events, they will be executed after the current
            // one is done, so just return the thread to whoever sent it.

            if (events.size() != 1) {
                return;
            }


        }
        while (true) {

            synchronized (events) {
                ev = events.getFirst();
            }

            IState tmp = states.findTransition(currentState, ev);

            // No valid state to transition to
            if (tmp == null && events.size() == 1) {
                // Default Route

                /*
                 * Flag to throw exception if the event was not expected for the current state
                 */
                throw new IllegalStateException(this + " can not accept event \"" + ev + "\" when in state \"" + currentState + "\"");

            } else if (tmp != null) {
                if (!hideDebugMessages) {
                    System.out.println(this + " Transition from state \"" + currentState + "\" to state \"" + tmp + "\" because of event \"" + ev + "\"");
                }

                if (currentState != null) {
                    currentState.exitAction(ev);
                }

                currentState = tmp;

                if (setListener != null) {
                    setListener.newState(currentState, ev);
                }

                IEvent next = currentState.entryAction(ev);

                synchronized (events) {
                    if (next != null) {
                        events.add(next);
                    }

                }
            }
            synchronized (events) {
                events.removeFirst();
                if (events.isEmpty()) {
                    return;
                }
            }
        }
    }

    /**
     * Reimplementation of String for logging. FSM=Finite State Machine
     */
    public String toString() {
        return "[FSM " + name + "]";
    }

    @SuppressWarnings("unused")
    private ITransitionListener setListener;

    /**
     * Show the log information
     */
    public void showDebugMessages() {
        hideDebugMessages = false;
    }
}


/**
 * Each row of the table has the association (event, (starting state, next state))
 *
 * If "startingState" is the string "Default", it means to use this event for all startingStates
 * If nextState is "EMPTY" it means to block a default event from this startingState
 *
 * This can be implemented in a few ways. If we were to guarantee that Events and States
 * had unique .toStrings () then we could use a lookup like Event.toString () + "+" + State.toString ()
 */
@SuppressWarnings("unchecked")
class StateTableWithNestedHashtable {

    private final Hashtable<Object,Object> states = new Hashtable<>();

    public void addTransition(IState startingState, IEvent ev, IState nextState) {
        Hashtable<Object,Object> h = (Hashtable<Object, Object>) states.get(ev);
        if (h == null) {
            h = new Hashtable<>(3);
            states.put(ev, h);
        }

        Object next;
        next = Objects.requireNonNullElse(nextState, "EMPTY");

        h.put(Objects.requireNonNullElse(startingState, "Default"), next);
    }

    public IState findTransition(IState currentState, IEvent ev) {
        Hashtable<Object,Object> h = (Hashtable<Object,Object>) states.get(ev);
        if (h == null) {
            return null;
        }

        Object next = h.get(currentState);
        if (next == null) {
            return (IState) h.get("Default");
        }

        if (next.equals("EMPTY")) {
            return null;
        }
        return (IState) next;
    }
}


