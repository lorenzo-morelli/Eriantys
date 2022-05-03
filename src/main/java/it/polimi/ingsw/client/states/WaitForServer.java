package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.utils.network.events.MessageReceived;
import it.polimi.ingsw.utils.stateMachine.*;

import java.io.IOException;

public class WaitForServer extends State {
    ClientModel clientModel;
    View view;
    String type;
    MessageReceived GO;

    public WaitForServer(View view, ClientModel clientModel, String type) throws IOException {
        super("[STATO di attesa gase di (WaitForTurn.java)]"+ type);
        this.view = view;
        this.clientModel = clientModel;
        this.type= type;
        GO = new MessageReceived(type);
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        return null;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
    }
}