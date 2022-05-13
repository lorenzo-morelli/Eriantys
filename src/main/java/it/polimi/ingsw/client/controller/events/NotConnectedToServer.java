package it.polimi.ingsw.client.controller.events;

import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.Event;

public class NotConnectedToServer extends Event {

    private boolean enabled = false;

    public NotConnectedToServer() {
        super("[Collegamento al server non riuscito]");
    }
}
