package it.polimi.ingsw.client.events;

import it.polimi.ingsw.utils.stateMachine.Event;

/**
 * L'evento di riconoscimento di una parola inviata al server.
 * @author Ignazio
 */
public class Message_Sended extends Event{
    private boolean enabled = false;

    public Message_Sended(String message){
        super("[Inviato Messaggio: " + message + "]");
    }


    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

}
