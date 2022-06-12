package it.polimi.ingsw.client.view.cli.controller.events;

import it.polimi.ingsw.utils.stateMachine.Event;

public class ClientDisconnection extends Event {

    public ClientDisconnection() {
        super("Client disconnesso");
    }
}
