package it.polimi.ingsw.client.events;

import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.observerPattern.Observer;
import it.polimi.ingsw.utils.stateMachine.Event;

import java.io.IOException;
import java.util.ArrayList;

/**
 * L'evento scatta quando l'utente NON inserisce una delle parole
 * indicate nell'ArrayList toListen
 * @author Fernando
 */
public class NotRecognizedSetOfStrings extends Event implements Observer {
    public ArrayList<String> toListen = null;
    private CommandPrompt commandPrompt;
    private boolean enabled = false;

    public NotRecognizedSetOfStrings(ArrayList<String> words) throws IOException {
        super("[Nessuna delle opzioni riconosciute]");
        this.toListen = words;
        System.out.println("[Costruito l'evento Stringhe non riconosciute]");
        this.commandPrompt = CommandPrompt.getInstance();
        this.subscribe();
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    private boolean isInList(String message) throws IOException {
        for (String x : toListen){
            if (message.equals(x)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(Object message) throws IOException, InterruptedException {
        if(toListen != null && enabled == true){
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