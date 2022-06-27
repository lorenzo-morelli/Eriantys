package it.polimi.ingsw.client.view.cli.cliController.events;

import it.polimi.ingsw.utils.stateMachine.Event;

/**
 * Handle the situation of "not connected" state of the client
 */
public class NotConnectedToServer extends Event {

    public NotConnectedToServer() {
        super("[Collegamento al server non riuscito]");
    }
}
