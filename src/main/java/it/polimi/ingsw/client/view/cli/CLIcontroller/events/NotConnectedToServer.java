package it.polimi.ingsw.client.view.cli.CLIcontroller.events;

import it.polimi.ingsw.utils.stateMachine.Event;

public class NotConnectedToServer extends Event {

    public NotConnectedToServer() {
        super("[Collegamento al server non riuscito]");
    }
}
