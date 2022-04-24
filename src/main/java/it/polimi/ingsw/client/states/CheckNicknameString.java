package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.NotRecognizedSetOfStrings;
import it.polimi.ingsw.client.events.RecognizedString;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CheckNicknameString extends State {

    private Model model;
    private View view;


    private final RecognizedString si, no;
    private final NotRecognizedSetOfStrings neSineNo;

    public CheckNicknameString(View view, Model model) throws IOException {
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
        // abilito gli eventi
        si.enable();
        no.enable();
        neSineNo.enable();

        view.askNicknameConfirmation(model.getNickname());

        // disabilito l'evento di ricezione del nickame
        si.disable();
        no.disable();
        neSineNo.disable();
        return null;
    }
}
