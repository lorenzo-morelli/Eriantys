package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;
import java.util.*;

public class CheckNicknameScreen extends State{

    private Model model;
    private View view;


    private final RecognizedString si, no;
    private final NotRecognizedSetOfStrings neSineNo;

    public CheckNicknameScreen(View view, Model model) throws IOException {
        super("[STATO di controllo del nickname]");
        this.view = view;
        this.model = model;
        si = new RecognizedString("si");
        no = new RecognizedString("no");
        neSineNo = new NotRecognizedSetOfStrings(new ArrayList<>(Arrays.asList("no", "si")));
    }

    public RecognizedString si() {
        return si;
    }

    public RecognizedString no() {
        return no;
    }


    public NotRecognizedSetOfStrings neSineNo() {
        return neSineNo;
    }

    public IEvent entryAction(IEvent cause) throws IOException{

        view.setCallingState(this);
        view.askNicknameConfirmation(model.getNickname());

        return null;
    }
}
