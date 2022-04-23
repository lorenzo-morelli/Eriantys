package it.polimi.ingsw.server.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.Model;
import it.polimi.ingsw.utils.commons.events.InputString;
import it.polimi.ingsw.utils.commons.events.NotRecognizedSetOfStrings;
import it.polimi.ingsw.utils.commons.events.RecognizedString;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CheckNickname extends State {

    private Model model;
    private View view;


    private final RecognizedString si, no;
    private final NotRecognizedSetOfStrings neSineNo;

    public CheckNickname(View view, Model model) throws IOException {
        super("STATO di controllo del nickname");
        this.view = view;
        this.model = model;
        si = new RecognizedString("si");
        no = new RecognizedString("no");
        neSineNo = new NotRecognizedSetOfStrings(new ArrayList<>(Arrays.asList("no", "si")));
    }

    public RecognizedString insertedSi() {
        return si;
    }

    public RecognizedString insertedNo() {
        return no;
    }

    public NotRecognizedSetOfStrings insertedNeSineNo() {
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
