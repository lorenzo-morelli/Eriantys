package it.polimi.ingsw.client.events;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.observerPattern.Observer;
import it.polimi.ingsw.utils.stateMachine.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Evento di inserimento di una parola da parte dell'utente,
 * l'utente scrive una parola di suo gradimento e preme invio.
 *
 * * @author Fernando
 */

public class ParametersFromTerminal extends Event implements Observer {
    private CommandPrompt commandPrompt;
    private boolean enabled = false;

    private Model model;

    private ArrayList<String> parsedStrings;

    private int numberOfStrings;

    public ParametersFromTerminal(Model model, int numberOfStrings) throws IOException {
        super("[Inserimento di "+numberOfStrings+" parametri da terminale]" );
        this.commandPrompt = CommandPrompt.getInstance();
        this.subscribe();
        this.model = model;
        this.numberOfStrings = numberOfStrings;
    }


    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    @Override
    public void update(Object message) throws IOException, InterruptedException {
        if (enabled == true){
            parsedStrings = new ArrayList<String>(Arrays.asList(CommandPrompt.gotFromTerminal().split(" ")));
            if (parsedStrings.size() == numberOfStrings){
                model.setFromTerminal(parsedStrings);
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
