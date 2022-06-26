package it.polimi.ingsw.client.view.cli.cliController.events;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.cli.CommandPrompt;
import it.polimi.ingsw.utils.observerPattern.Observer;
import it.polimi.ingsw.utils.stateMachine.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Event of entering a word by the user,
 * the user writes a word of his liking and presses enter.
 *
 * * @author Fernando
 */

public class ParametersFromTerminal extends Event implements Observer {
    private final CommandPrompt commandPrompt;
    private final ClientModel clientModel;
    private final int numberOfStrings;
    private boolean parametersReceived = false;

    public ParametersFromTerminal(ClientModel clientModel, int numberOfStrings) throws IOException {
        super("[Inserimento di "+numberOfStrings+" parametri da terminale]" );
        this.commandPrompt = CommandPrompt.getInstance();
        this.subscribe();
        this.clientModel = clientModel;
        this.numberOfStrings = numberOfStrings;
    }

    @Override
    public void update(Object message) {
        ArrayList<String> parsedStrings = new ArrayList<>(Arrays.asList(CommandPrompt.gotFromTerminal().split(" ")));
            if (parsedStrings.size() == numberOfStrings){
                clientModel.setFromTerminal(parsedStrings);
                this.parametersReceived = true;
            }
    }

    public boolean parametersReceived() {
        return parametersReceived;
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
