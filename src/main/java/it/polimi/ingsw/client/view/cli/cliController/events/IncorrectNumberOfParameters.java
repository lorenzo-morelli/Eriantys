package it.polimi.ingsw.client.view.cli.cliController.events;

import it.polimi.ingsw.client.view.cli.CommandPrompt;
import it.polimi.ingsw.utils.observerPattern.Observer;
import it.polimi.ingsw.utils.stateMachine.Event;

import java.io.IOException;

/**
 * Evento che si attiva quando l'utente inserisce un numero di parametri scorretti
 * da terminale
 * <p>
 * * @author Fernando
 */

public class IncorrectNumberOfParameters extends Event implements Observer {
    private final CommandPrompt commandPrompt;

    public IncorrectNumberOfParameters(int numberOfStrings) throws IOException {
        super("[Numero di parametri non corretto (doveva essere " + numberOfStrings + ")]");
        this.commandPrompt = CommandPrompt.getInstance();
        this.subscribe();
    }

    @Override
    public void update(Object message) {
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
