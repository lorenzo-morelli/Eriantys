package it.polimi.ingsw.utils.commons.events;

import it.polimi.ingsw.utils.commandLine.CommandPrompt;
import it.polimi.ingsw.utils.observerPattern.Observer;
import it.polimi.ingsw.utils.stateMachine.Event;

import java.io.IOException;

/**
 * L'evento TerminalEvent rappresenta un generico evento di inserimento di una parola nel terminale.
 * Noi vogliamo poter costruire un TerminalEvent, ad esempio "L'utente ha inserito ciao sul terminale"
 * Cioè vogliamo poter comparare la stringa ricevuta dal cmd con una stringa memorizzata,
 * quando l'utente inserisce proprio quella stringa scateniamo l'evento
 * Ho utilizzato il pattern observer per poter osservare la CommandPrompt
 *
 * @author Fernando
 */
public class RecognizeString extends Event implements Observer {
    public String toListen = null;
    private CommandPrompt commandPrompt;

    public RecognizeString(String message) throws IOException {
        super("Terminal event " + message);
        this.toListen = message;
        System.out.println("[Costruito l'evento di ricenzione da terminale della parola "+message+"]");
        this.commandPrompt = CommandPrompt.getInstance();
        this.subscribe();
    }

    @Override
    public void update(Object message) throws IOException, InterruptedException {
        if(toListen != null){
            if (this.toListen.equals ((String)message)){
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
