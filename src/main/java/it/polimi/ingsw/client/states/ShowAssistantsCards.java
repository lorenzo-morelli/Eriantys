package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.network.events.MessageReceived;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class ShowAssistantsCards extends State {
    Model model;
    View view;
    MessageReceived cardView;

    public ShowAssistantsCards(View view, Model model) throws IOException {
        super("[STATO di scelta Carta Assistente (ShowAssistantsCards.java)]");
        this.view = view;
        this.model = model;
        cardView = new MessageReceived("PRINTED_AVAIABLE_ASSISTENT_CARD");
    }

    public MessageReceived view_done() {
        return cardView;
    }


    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        //todo: ricevi da server le carte assistenti disponibili e stampale a video
        view_done();
        return null;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
    }

    public MessageReceived insertedCard() {
        return cardView;  // forse?
    }
}
