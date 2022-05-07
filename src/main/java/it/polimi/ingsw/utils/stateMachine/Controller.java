package it.polimi.ingsw.utils.stateMachine;

import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;

/*
 * Questa classe mette a disposizione un meccanismo per la creazione di transizioni tra stati
 * che creano una macchina a stati finiti, che sara' memorizzata in una hashMap internamente
 *
 * Per esempio, se volessimo utilizzare la tastiera per governare una macchina a stati finiti
 * che accetti solamente di scrivere la parola "lode", la tabella degli stati sarebbe cosi':
 *
 *                          Stati:
 *             starting   l    lo   lod   lode (ending state)
 * eventi    |------------------------------|
 *     l     |    >l      x    x      x     |
 *           |------------------------------|
 *     o     |    x      >lo   x      x     |
 *           |------------------------------|
 *     d     |    x       x  >lod     x     |
 *           |------------------------------|
 *     e     |    x       x    x     >lode  |
 *           |------------------------------|
 *
 * Per memorizzare questa tabella sono necessarie 4 chiamate:
 *
 * addTransaction(state_starting, event_l, state_l)
 * addTransaction(state_l,        event_o, state_lo)
 * addTransaction(state_lo,       event_d, state_lod)
 * addTransaction(state_lod,      event_e, state_lode)
 *
 * Dopo tale configurazione, il controllore verrà azionato da solo, gestendo le notifiche
 * dai suoi eventi e inviando azioni ai suoi stati.
 *
 * L'accodamento degli eventi è consentito, anche durante un evento. Gli eventi verranno elaborati in ordine.
 *
 * Se viene raggiunto uno stato "X", il controllore lo ignorerà o lancerà un'eccezione all'evento che ha causato l'errore.

 */

public class Controller {
    private boolean hideDebugMessages = true;
    public final static Object STATE_DO_NOTHING=null;

    /** Memorizza tutte le possibili transazioni */
    private final StateTableWithNestedHashtables states = new StateTableWithNestedHashtables();
    /** Lo stato corrente del controllore,non puo'essere null */
    private IState currentState;
    /** il nome del controllore per motivi di log */
    private final String name;
    /** Coda di eventi */
    private final LinkedList<IEvent> events = new LinkedList<IEvent>();
    /**
     * Lancia eccezione se non ci sono eventi disponibili
     */
    private boolean exceptOnIllegal = true;

    /**
     * L'entryAction del primo stato non sara' chiamata, siccome non c'e' nessun evento che l'ha triggerata
     * L' exitAction sara' chiamata.
     *
     * @param firstState
     *            Stato iniziale della macchina.
     * @param name
     *            nome dello stato utilizzato per il logging
     */
    public Controller(String name, IState firstState) {
        this.name = name;
        if (firstState == null) {
            throw new IllegalArgumentException();
        }
        currentState = firstState;
    }
    /*
     * @param name
     * @param stateTable
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
            // Keep a running count of what column of the row we are on so we
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
                throw new IllegalArgumentException("Item found in table not IState, IStateEvent or null, itemno:" + i);
            }

            // We have identified a "next state" in the node, a "starting state"
            // in the
            // header at the top of this column, and a transition (the last
            // event we hit)
            // Add the transition.
            addTransition((IState) stateTable[colNo], readingEvent, (IState) cell);
        }
    }

    /**
     * Programma il controllore con le transizioni consentite.
     *
     * Il controllore sara' sempre in uno stato, quando un evento termina,
     * questa tabella e' interrogata per vedere se l'evento e' valido nel contesto
     * dello stato corrente, se si avviene la transizione.
     *
     * @param startingState
     *            se il controllore si trova in questo stato
     * @param ev
     *            e viene scatenato questo evento
     * @param nextState
     *            allora transisci in ques'altro stato
     */
    public void addTransition(IState startingState, IEvent ev, IState nextState) {
        ev.setStateEventListener(this);
        if (!hideDebugMessages) {
            System.out.println(this + " Transizione (" + startingState + "," + ev + ") --> " + nextState + " aggiunta");
        }
        states.addTransition(startingState, ev, nextState);
    }

    /**
     * Vero di default, se viene settato a false e accade un evento non tra quelli consentiti esso sara'
     * semplicemente ignorato.
     *
     * @param except
     *            false per stoppare le eccezioni.
     */
    public void setExceptionOnIllegalTransiction(boolean except) {
        this.exceptOnIllegal = except;
    }

    /**
     * Cio' che attiva le transizioni.
     * Mette in coda gli eventi, prende il primo evento, si assicura che sia valido, chiama l'
     * exitAction dello stato corrente, cambia stato e chiama l'entryAction dello stato successivo
     *
     * Se un cambio di stato non è valido, genera una "IllegalStateException"
     * contenente i nomi dello stato iniziale, dello stato finale e dell'evento.
     *
     * Dovrebbe gestire bene più eventi e situazioni di threading.
     * Gli eventi sono gestiti nell'ordine in cui vengono ricevuti.
     * @param ev
     *            L'evento azionato.
     */
    public void fireStateEvent(IEvent ev) throws Exception {
        synchronized (events) {
            // aggiunge in coda l'evento arrivato
            // il primo evento che arriva dovrebbe essere il primo eseguito
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

                if (exceptOnIllegal) {
                    throw new IllegalStateException(this + " can not accept event \"" + ev + "\" when in state \"" + currentState + "\"");
                }

            } else if (tmp != null ){
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
     * Reimplementazione di toString per motivi di logging. FSM=Finite State Machine
     *
     * @return
     */
    public String toString() {
        return "[FSM " + name + "]";
    }
    private ITransitionListener setListener;

    public void addStateEngineTransactionListener(ITransitionListener testEventsMatrix) {
        this.setListener = testEventsMatrix;
    }

    /**
     * Mostra i log delle transizioni tra gli stati
     */
    public void showDebugMessages(){
        hideDebugMessages = false;
    }
}



/**
 *  Ciascuna riga della tabella ha l'associazione (evento, (stato di partenza , stato prossimo))
 *
 * If "startingState" is the string "Default", it means to use this event for all startingStates
 * If nextState is "EMPTY" it means to block a default event from this startingState
 *
 * This can be implemented in a few ways.  If we were to gaurentee that Events and States
 * had unique .toStrings() then we could use a lookup like Event.toString()+"+"+State.toString()
 *
 * That's why this module is extracted -- it's replaceable.
 */
class StateTableWithNestedHashtables {

    private final Hashtable states = new Hashtable();

    public void addTransition(IState startingState, IEvent ev, IState nextState) {
        Hashtable h = (Hashtable) states.get(ev);
        if (h == null) {
            h = new Hashtable(3);
            states.put(ev, h);
        }

        Object next;
        if (nextState != null) {
            next = nextState;
        } else {
            next = "EMPTY";
        }

        if (startingState == null) {
            h.put("Default", next);
        } else {
            h.put(startingState, next);
        }
    }

    public IState findTransition(IState currentState, IEvent ev) {
        Hashtable h = (Hashtable) states.get(ev);
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


