package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class WaitForturn extends State {
    Model model;
    View view;

    Message_Received ASSISTENTPHASE;
    Message_Received ENDPHASE;
    Message_Received STUDENTPHASE;
    Message_Received CLOUDPHASE;

    public WaitForturn(View view, Model model) throws IOException {
        super("[STATO di attesa]");
        this.view = view;
        this.model = model;
        ENDPHASE = new Message_Received("GO_TO_ENDPHASE");
        ASSISTENTPHASE = new Message_Received("GO_TO_ASSISTENTPHASE");
        STUDENTPHASE = new Message_Received("GO_TO_STUDENTPHASE");
        CLOUDPHASE = new Message_Received("GO_TOCLOUDPHASE");
    }

    // testing
    public Message_Received go_to_assistantcardphase() {
        return ASSISTENTPHASE;
    }

    public Message_Received go_to_endgame() {
        return ENDPHASE;
    }

    public Message_Received go_to_studentphase() {
        return STUDENTPHASE;
    }

    public Message_Received go_to_cloudphase() {
        return CLOUDPHASE;
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        return null;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
        // todo: mettiti in attesa di un ACK "command" da server
        switch ("ASSISTENTCARDPAHSE" /*command*/){   //messo giusto per togliere l'errore
            case "STUDENTPHASE": go_to_studentphase();
            case "ASSISTANTCARDPHASE": go_to_assistantcardphase();
            case "CLOUDPHASE": go_to_cloudphase();
            case "ENDGAME": go_to_endgame();
        }
    }
}