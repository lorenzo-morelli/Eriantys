package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.events.NotRecognizedString;
import it.polimi.ingsw.client.events.RecognizedString;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class WelcomeScreen extends State {
    View view;
    private RecognizedString start;
    private NotRecognizedString notStart;


    public WelcomeScreen(View view) throws IOException {
        super("[STATO di attesa di start]");
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
        // mi metto in ascolto di possibili eventi che mi potrebbero far transire
        start.enable();
        notStart.enable();

        view.askToStart();

        // disabilito gli eventi adesso perché non mi servono più
        start.disable();
        notStart.disable();
        return null;
    }
}
