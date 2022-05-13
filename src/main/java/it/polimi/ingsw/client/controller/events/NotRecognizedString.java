package it.polimi.ingsw.client.controller.events;

import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.observerPattern.Observer;
import it.polimi.ingsw.utils.stateMachine.Event;

import java.io.IOException;

/**
 * L'evento di riconoscimento di quando una parola NON viene inserita dall'utente.
 * Vogliamo poter comparare la stringa ricevuta dal cmd con una stringa memorizzata,
 * quando l'utente NON inserisce proprio quella stringa scateniamo l'evento
 *
 * @author Fernando
 */
public class NotRecognizedString extends Event implements Observer {
    public String toListen = null;
    private final CommandPrompt commandPrompt;
    private boolean enabled = false;

    public NotRecognizedString(String message) throws IOException {
        super("[Non è stata riconosciuta la parola " + message + "]");
        this.toListen = message;
        this.commandPrompt = CommandPrompt.getInstance();
        this.subscribe();
    }


    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    @Override
    public void update(Object message) throws Exception {
        if (toListen != null && enabled) {
            if (!this.toListen.equals((String) message)) {
                fireStateEvent();
            }
        }
    }

    @Override
    public void subscribe() {
        this.commandPrompt.subscribeObserver(this);
    }

    @Override
    public void unSubscribe() {
        this.commandPrompt.unsubscribeObserver(this);
    }
}

