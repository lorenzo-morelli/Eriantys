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
    MessageReceived MOVEMOTHERPHASE;

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

    public MessageReceived go_to_movemotherphase(){ return MOVEMOTHERPHASE;}

    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        return null;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
        String command;
        // todo: mettiti in attesa di un ACK "command" da server
        command="ASSISTENTCARDPAHSE"; //messo giusto per togliere l'errore
        switch (command){
            case "STUDENTPHASE": go_to_studentphase();
            break;
            case "ASSISTANTCARDPHASE": go_to_assistantcardphase();
            break;
            case "CLOUDPHASE": go_to_cloudphase();
            break;
            case "MOVEMOTHERPHASE": go_to_movemotherphase();
            break;
            case "ENDGAME": go_to_endgame();
            break;
        }
        view.itsyourturn(command);
    }
}