package it.polimi.ingsw.client.events;

import it.polimi.ingsw.utils.stateMachine.Event;

/**
 * L'evento di riconoscimento errori di comunicazione con server.
 * @author Ignazio
 */
public class NetworkIssue extends Event{
    private boolean enabled = false;

    public NetworkIssue(String message){
        super("[Ricevuto ERRORE: " + message + "]");
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

}
