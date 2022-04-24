package it.polimi.ingsw.client.events;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.observerPattern.Observer;
import it.polimi.ingsw.utils.stateMachine.Event;

import java.io.IOException;

/**
 * Evento di inserimento di una parola da parte dell'utente,
 * l'utente scrive una parola di suo gradimento e preme invio.
 *
 * * @author Fernando
 */

public class InputString extends Event implements Observer {
    private CommandPrompt commandPrompt;
    private boolean enabled = false;

    private Model model;

    public InputString(Model model) throws IOException {
        super("[L'utente ha scitto una parola a piacere]" );
        this.commandPrompt = CommandPrompt.getInstance();
        this.subscribe();
        this.model = model;
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
            model.setNickname(CommandPrompt.gotFromTerminal());
            fireStateEvent();
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
