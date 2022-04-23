package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class Final extends State {
    View view;
    Model model;

    public Final(View view, Model model) {
        super("Stato finale");
        this.view = view;
        this.model = model;
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        view.showConfirmation(model.getNickname());
        return null;
    }
}
