package it.polimi.ingsw.client.view.cli.cliController.events;

import it.polimi.ingsw.client.view.cli.CommandPrompt;
import it.polimi.ingsw.utils.observerPattern.Observer;
import it.polimi.ingsw.utils.stateMachine.Event;

/**
 * The recognition event of a word entered by the user.
 * We want to be able to compare the string received from the cmd with a stored string,
 * when the user inserts that string we trigger the event
 * I used the pattern observer to be able to observe the Command Prompt
 *
 * @author Fernando
 */
public class RecognizedString extends Event implements Observer {
    public final String toListen;
    private final CommandPrompt commandPrompt;
    private boolean enabled = false;

    public RecognizedString(String message) {
        super("[Recognized the word " + message + "]");
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
