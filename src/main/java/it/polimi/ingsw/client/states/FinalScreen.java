package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class FinalScreen extends State {
    View view;
    Model model;

    public FinalScreen(View view, Model model) {
        super("[STATO finale]");
        this.view = view;
        this.model = model;
    }

    public IEvent entryAction(IEvent cause) throws IOException {

        view.setCallingState(this);
        view.showConfirmation(model.getNickname());

        return null;
    }
}
