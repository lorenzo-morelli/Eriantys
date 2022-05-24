package it.polimi.ingsw.client.controller.events;

import it.polimi.ingsw.utils.stateMachine.Event;

public class ConnectedToServer extends Event {

    public ConnectedToServer() {
        super("[Collegato al server]");
    }
}
