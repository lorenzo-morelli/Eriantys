package it.polimi.ingsw.utils.commons.events;

import it.polimi.ingsw.utils.commandLine.CommandPrompt;
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
    private CommandPrompt commandPrompt;

    public NotRecognizedString(String message) throws IOException {
        super("Terminal event " + message);
        this.toListen = message;
        System.out.println("[Costruito l'evento di ricenzione da terminale della parola "+message+"]");
        this.commandPrompt = CommandPrompt.getInstance();
        this.subscribe();
    }

    @Override
    public void update(Object message) throws IOException, InterruptedException {
        if(toListen != null){
            if (!this.toListen.equals ((String)message)){
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

