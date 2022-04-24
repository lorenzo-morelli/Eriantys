package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.InputString;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class AskNicknameScreen extends State {

    Model model;
    View view;

    InputString insertNickname;

    public AskNicknameScreen(View view, Model model) throws IOException {
        super("[STATO di attesa del nickname]");
        this.view = view;
        this.model = model;
        insertNickname = new InputString(model);
    }

    public InputString nickname() {
        return insertNickname;
    }

    public IEvent entryAction(IEvent cause) throws IOException{
        // abilito l'evento di ricezione del nickname
        insertNickname.enable();

        view.askNickname();

        // disabilito l'evento di ricezione del nickame
        insertNickname.disable();
        return null;
    }
}
