package it.polimi.ingsw.server.states;

import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class SpecifyPortScreen extends State {
    Event portSpecified;

    public SpecifyPortScreen() throws IOException {
        super("[Specifica porta dove mettersi in ascolto dei clients]");
        portSpecified = new Event("Porta specificata");

    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        if(!Network.isServerListening()) {
            CommandPrompt.ask(
                    "Inserire la porta sulla quale mettersi in ascolto dei clients",
                    "porta >");
        }
        portSpecified.fireStateEvent();

        return super.entryAction(cause);
    }

    public Event portSpecified() {
        return portSpecified;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        Network.setupServer(CommandPrompt.gotFromTerminal());
        super.exitAction(cause);
    }
}
