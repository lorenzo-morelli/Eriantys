package it.polimi.ingsw.client.controller.events;

import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.Event;

public class ConnectedToServer extends Event {

    private boolean enabled = false;

    public ConnectedToServer() {
        super("[Collegato al server]");
    }
}
