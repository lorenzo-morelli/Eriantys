package it.polimi.ingsw.client.view.cli.cliController.events;

import it.polimi.ingsw.utils.stateMachine.Event;

/**
 * Handle the connection to the server
 */
public class ConnectedToServer extends Event {

    public ConnectedToServer() {
        super("[Connected to the server]");
    }
}
