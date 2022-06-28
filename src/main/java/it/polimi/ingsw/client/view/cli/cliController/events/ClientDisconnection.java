package it.polimi.ingsw.client.view.cli.cliController.events;

import it.polimi.ingsw.utils.stateMachine.Event;

/**
 * Handle the event of disconnection of one client
 */
public class ClientDisconnection extends Event {

    public ClientDisconnection() {
        super("A client disconnected from the game");
    }
}
