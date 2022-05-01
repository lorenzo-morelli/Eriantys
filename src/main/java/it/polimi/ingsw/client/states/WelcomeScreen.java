package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.events.NotRecognizedString;
import it.polimi.ingsw.client.events.RecognizedString;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class WelcomeScreen extends State{
    View view;
    private final RecognizedString start;
    private final NotRecognizedString notStart;

    public WelcomeScreen(View view) throws IOException {
        super("[STATO di attesa di start (WelcomeScreen.java)]");
        this.view = view;
        start = new RecognizedString("start");
        notStart = new NotRecognizedString("start");
    }

    public RecognizedString start() {
        return start;
    }

    public NotRecognizedString notStart() {
        return notStart;
    }

    public IEvent entryAction(IEvent cause) throws IOException {

        view.setCallingState(this);
        view.askToStart();
        return null;
    }
}
