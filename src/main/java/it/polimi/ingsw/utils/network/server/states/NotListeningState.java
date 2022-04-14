package it.polimi.ingsw.utils.network.server.states;

import it.polimi.ingsw.utils.stateMachine.*;

public class NotListeningState extends State {

    public NotListeningState() {
        super("ConnectToServer");
    }

    public IEvent entryAction(IEvent cause) {
        System.out.println("Provando a riconnettermi al Server");
        return null;
    }
}
