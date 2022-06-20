package it.polimi.ingsw.client.view.cli.CLIcontroller.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.cli.CLIcontroller.events.NotRecognizedString;
import it.polimi.ingsw.client.view.cli.CLIcontroller.events.RecognizedString;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

/**
 * State that required client interaction in early demo versions of the game,
 * the user had to enter start. No longer used but left for reference.
 */
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

    public IEvent entryAction(IEvent cause) throws Exception {

        //view.setCallingState(this);
        //view.askToStart();
        start.fireStateEvent();
        return null;
    }
}
