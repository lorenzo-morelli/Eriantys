package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class AskNicknameScreen extends State{
    Model model;
    View view;
    InputString insertUserInfo;

    public AskConnectionInfoScreen(View view, Model model) throws IOException {
        super("[STATO di attesa del nickname]");
        this.view = view;
        this.model = model;
        insertNickname = new InputString(model);
    }

    public InputString userInfo() {
        return insertUserInfo;
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        view.askNickname();
        return null;
    }

    public void setNickname(String nickname) {
        model.setNickname(nickname);
    }
}
