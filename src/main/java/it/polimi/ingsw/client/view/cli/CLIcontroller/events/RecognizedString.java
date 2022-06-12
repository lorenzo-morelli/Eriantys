package it.polimi.ingsw.client.view.cli.CLIcontroller.events;

import it.polimi.ingsw.client.view.cli.CommandPrompt;
import it.polimi.ingsw.utils.observerPattern.Observer;
import it.polimi.ingsw.utils.stateMachine.Event;

import java.io.IOException;

/**
 * L'evento di riconoscimento di una parola inserita dall'utente.
 * Vogliamo poter comparare la stringa ricevuta dal cmd con una stringa memorizzata,
 * quando l'utente inserisce proprio quella stringa scateniamo l'evento
 * Ho utilizzato il pattern observer per poter osservare la CommandPrompt
 *
 * @author Fernando
 */
public class RecognizedString extends Event implements Observer {
    public String toListen;
    private final CommandPrompt commandPrompt;
    private boolean enabled = false;

    public RecognizedString(String message) throws IOException {
        super("[Riconosciuta la parola " + message + "]");
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
        if(toListen != null && enabled){
            if (this.toListen.equals (message)){
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
