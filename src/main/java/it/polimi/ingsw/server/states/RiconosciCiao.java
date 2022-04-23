package it.polimi.ingsw.server.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.commons.events.NotRecognizedString;
import it.polimi.ingsw.utils.commons.events.RecognizedString;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class RiconosciCiao extends State {
    View view;
    private RecognizedString ciao;
    private NotRecognizedString notciao;


    public RiconosciCiao(View view) throws IOException {
        super("STATO di attesa di \"ciao\"");
        this.view = view;
        ciao = new RecognizedString("ciao");
        notciao = new NotRecognizedString("ciao");
    }



    public RecognizedString insertedCiao() {
        return ciao;
    }

    public NotRecognizedString notInsertedCiao() {
        return notciao;
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        // mi metto in ascolto di possibili eventi che mi potrebbero far transire
        ciao.enable();
        notciao.enable();

        view.askCiao();

        // disabilito gli eventi adesso perché non mi servono più
        ciao.disable();
        notciao.disable();
        return null;
    }
}
