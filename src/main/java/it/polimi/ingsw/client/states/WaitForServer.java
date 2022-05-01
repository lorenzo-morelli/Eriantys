package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.utils.network.events.MessageReceived;
import it.polimi.ingsw.utils.stateMachine.*;

import javax.annotation.processing.Messager;
import java.io.IOException;

public class WaitForServer extends State {
    Model model;
    View view;
    String type;
    MessageReceived GO;

    public WaitForServer(View view, Model model,String type) throws IOException {
        super("[STATO di attesa gase di (WaitForTurn.java)]"+ type);
        this.view = view;
        this.model = model;
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