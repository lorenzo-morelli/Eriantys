package it.polimi.ingsw.server.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.Model;
import it.polimi.ingsw.utils.commons.events.InputString;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class AskNickname extends State {

    Model model;
    View view;

    InputString insertNickname;

    public AskNickname(View view, Model model) throws IOException {
        super("STATO di attesa di \"ciao\"");
        this.view = view;
        this.model = model;
        insertNickname = new InputString();
    }

    public InputString insertedNickname() {
        return insertNickname;
    }

    public IEvent entryAction(IEvent cause) throws IOException{
        // abilito l'evento di ricezione del nickname
        insertNickname.enable();

        String nickname = view.askNickname();
        model.setNickname(nickname);

        // disabilito l'evento di ricezione del nickame
        insertNickname.disable();
        return null;
    }
}
