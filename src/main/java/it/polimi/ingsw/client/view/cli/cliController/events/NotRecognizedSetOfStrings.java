package it.polimi.ingsw.client.view.cli.cliController.events;

import it.polimi.ingsw.client.view.cli.CommandPrompt;
import it.polimi.ingsw.utils.observerPattern.Observer;
import it.polimi.ingsw.utils.stateMachine.Event;

import java.util.ArrayList;

/**
 * The event is triggered when the user DOES NOT enter one of the words
 * indicated in the ArrayList toListen
 * @author Fernando
 */
public class NotRecognizedSetOfStrings extends Event implements Observer {
    public final ArrayList<String> toListen;
    private final CommandPrompt commandPrompt;
    private boolean enabled = false;

    public NotRecognizedSetOfStrings(ArrayList<String> words) {
        super("[None of the recognized options]");
        this.toListen = words;
        this.commandPrompt = CommandPrompt.getInstance();
        this.subscribe();
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    private boolean isInList(String message) {
        for (String x : toListen){
            if (message.equals(x)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(Object message) throws Exception {
        if(toListen != null && enabled){
            if(!isInList((String)message)){
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