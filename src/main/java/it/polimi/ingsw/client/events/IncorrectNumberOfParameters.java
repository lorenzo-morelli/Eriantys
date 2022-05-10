package it.polimi.ingsw.client.events;

import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.observerPattern.Observer;
import it.polimi.ingsw.utils.stateMachine.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Evento che si attiva quando l'utente inserisce un numero di parametri scorretti
 * da terminale
 * <p>
 * * @author Fernando
 */

public class IncorrectNumberOfParameters extends Event implements Observer {
    private CommandPrompt commandPrompt;
    private boolean enabled = false;

    private ClientModel clientModel;

    private ArrayList<String> parsedStrings;

    private int numberOfStrings;

    public IncorrectNumberOfParameters(ClientModel clientModel, int numberOfStrings) throws IOException {
        super("[Numero di parametri non corretto (doveva essere " + numberOfStrings + ")]");
        this.commandPrompt = CommandPrompt.getInstance();
        this.subscribe();
        this.clientModel = clientModel;
        this.numberOfStrings = numberOfStrings;
    }

    @Override
    public void update(Object message) throws Exception {
        if (enabled) {
            parsedStrings = new ArrayList<String>(Arrays.asList(CommandPrompt.gotFromTerminal().split(" ")));
            if (parsedStrings.size() != numberOfStrings) {
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
