package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class WaitForturn extends State {
    Model model;
    View view;
    ParametersFromTerminal insertUserInfo;
    // todo:
    //  NOTA: per testare lo facciamo da tastiera , in pratica usiamo exitAction

    RecognizedString ASSISTENTPHASE;
    RecognizedString ENDPHASE;
    RecognizedString STUDENTPHASE;
    RecognizedString CLOUDPHASE;

    public WaitForturn(View view, Model model) throws IOException {
        super("[STATO di attesa]");
        this.view = view;
        this.model = model;
        //testing
        ENDPHASE = new RecognizedString("ENDPHASE");
        ASSISTENTPHASE = new RecognizedString("ASSISTENTPHASE");
        STUDENTPHASE = new RecognizedString("STUDENTPHASE");
        CLOUDPHASE = new RecognizedString("CLOUDPHASE");
    }

    // testing
    public RecognizedString go_to_assistantcardphase() {
        return ASSISTENTPHASE;
    }

    public RecognizedString go_to_endgame() {
        return ENDPHASE;
    }

    public RecognizedString go_to_studentphase() {
        return STUDENTPHASE;
    }

    public RecognizedString go_to_cloudphase() {
        return CLOUDPHASE;
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        return null;
    }
}
/*
    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
        // todo: mettiti in attesa di un comando "command" da server
        switch (command){
            case "STUDENTPHASE": go_to_studentphase();
            case "ASSISTANTCARDPHASE": go_to_assistantcardphase();
            case "CLOUDPHASE": go_to_cloudpahse();
            case "ENDGAME": go_to_endgame();
        }
    }

}

 */