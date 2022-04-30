package it.polimi.ingsw.client.events;

import it.polimi.ingsw.utils.stateMachine.Event;

/**
 * L'evento di riconoscimento di una parola inviata dal server.
 * @author Ignazio
 */
public class Message_Received extends Event{
    private boolean enabled = false;

    public Message_Received(String message){
        super("[Ricevuto Messaggio: " + message + "]");
    }


    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

}
