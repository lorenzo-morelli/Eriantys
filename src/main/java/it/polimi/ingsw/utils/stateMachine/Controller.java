package it.polimi.ingsw.utils.stateMachine;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Objects;

/**
 * This class provides a mechanism for creating transitions between states
 * which create a finite state machine, which will be stored in a hashMap internally
 * <p>
 * For example, if we wanted to use the keyboard to govern a finite state machine
 * if you only agree to write the word "lode", the table of states would be like this:
 * <p>
 * States:
 * starting   l    lo   lod   lode (ending state)
 * eventi    |------------------------------|
 * l     |    >l      x    x      x     |
 * |------------------------------|
 * o     |    x      >lo   x      x     |
 * |------------------------------|
 * d     |    x       x  >lod     x     |
 * |------------------------------|
 * e     |    x       x    x     >lode  |
 * |------------------------------|
 * <p>
 * 4 calls are required to store this table:
 * <p>
 * addTransaction(state_starting, event_l, state_l)
 * addTransaction(state_l,        event_o, state_lo)
 * addTransaction(state_lo,       event_d, state_lod)
 * addTransaction(state_lod,      event_e, state_lode)
 * <p>
 * After this configuration, the controller will be operated by itself, managing notifications
 * from its events and by sending actions to its states.
 * <p>
 * Event queuing is allowed, even during an event. The events will be processed in order.
 * <p>
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
     * @param name       The name of the controller
     * @param stateTable Alternative to store transitions between states
     */
    public Controller(String name, Object[] stateTable) {
        this.name = name;
        int colNo = 0;

        IEvent readingEvent = null;
        currentState = (IState) stateTable[0];

        // The current node being read
        int i = 0;

        // Skip over the state headers at the top
        while (stateTable[i] instanceof IState) {
            i++;
        }

        // Make sure the first thing after loading the table is the event column
        if (!(stateTable[i] instanceof IEvent)) {
            throw new IllegalArgumentException();
        }

        // Now, starting from the first event in the event column, read each row
        for (; i < stateTable.length; i++) {
            Object cell = stateTable[i];

            // Switch to next row when we hit an event
            if (cell instanceof IEvent) {
                readingEvent = (IEvent) cell;
                colNo = -1;
                continue;
            }
            // Keep a running count of what column of the row we are on, so we
            // can find a source event
            // (index into the top row of the table.) A nice check to add here
            // would be to make sure
            // it is never past the first event we saw (always in the top row)
            colNo++;

            // Ignore nulls, but they still count as columns
            if (cell == null) {
                continue;
            }

            // Just an easy sanity check
            if (!(cell instanceof IState)) {
                throw new IllegalArgumentException("Item found in table not IState, IStateEvent or null, item:" + i);
            }

            // We have identified a "next state" in the node, a "starting state"
            // in the
            // header at the top of this column, and a transition (the last
            // event we hit)
            // Add the transition.
            assert readingEvent != null;
            addTransition((IState) stateTable[colNo], readingEvent, (IState) cell);
        }
    }

    /**
     * Program the controller with allowed transitions.
     * <p>
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
     * <p>
     * If a status change is invalid, it throws an "IllegalStateException"
     * containing the names of the initial state, final state and event.
     * <p>
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
                    System.out.println(this + " transition from state \"" + currentState + "\" to state \"" + tmp + "\" because of event \"" + ev + "\"");
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
 * <p>
 * If "startingState" is the string "Default", it means to use this event for all startingStates
 * If nextState is "EMPTY" it means to block a default event from this startingState
 * <p>
 * This can be implemented in a few ways. If we were to guarantee that Events and States
 * had unique .toStrings () then we could use a lookup like Event.toString () + "+" + State.toString ()
 * <p>
 * That's why this module is extracted - it's replaceable.
 */
class StateTableWithNestedHashtable {

    private final Hashtable<Object,Object> states = new Hashtable<>(); //todo risolvere warning

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
        // Never seen this state.  So how did we even get the event?
        if (h == null) {
            return null;
        }

        Object next = h.get(currentState);
        // Next can now be any of null, "EMPTY" or a new state
        if (next == null) {
            return (IState) h.get("Default");
        }

        if (next.equals("EMPTY")) {
            return null;
        }

        return (IState) next;
    }
}


