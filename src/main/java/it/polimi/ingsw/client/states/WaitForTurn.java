package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.utils.network.events.MessageReceived;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class WaitForTurn extends State {
    Model model;
    View view;

    MessageReceived ASSISTENTPHASE;
    MessageReceived ENDPHASE;
    MessageReceived STUDENTPHASE;
    MessageReceived CLOUDPHASE;

    public WaitForTurn(View view, Model model) throws IOException {
        super("[STATO di attesa (WaitForTurn.java)]");
        this.view = view;
        this.model = model;
        ENDPHASE = new MessageReceived("GO_TO_ENDPHASE");
        ASSISTENTPHASE = new MessageReceived("GO_TO_ASSISTENTPHASE");
        STUDENTPHASE = new MessageReceived("GO_TO_STUDENTPHASE");
        CLOUDPHASE = new MessageReceived("GO_TOCLOUDPHASE");
    }

    // testing
    public MessageReceived go_to_assistantcardphase() {
        return ASSISTENTPHASE;
    }

    public MessageReceived go_to_endgame() {
        return ENDPHASE;
    }

    public MessageReceived go_to_studentphase() {
        return STUDENTPHASE;
    }

    public MessageReceived go_to_cloudphase() {
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