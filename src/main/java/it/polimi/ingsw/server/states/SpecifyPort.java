package it.polimi.ingsw.server.states;

import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class SpecifyPort extends State {
    Network network;

    Event portSpecified;

    public SpecifyPort(Network network) throws IOException {
        super("[Specifica porta dove mettersi in ascolto dei clients]");
        this.network = network;
        portSpecified = new Event("Porta specificata");

    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        CommandPrompt.ask(
                "Inserire la porta sulla quale mettersi in ascolto dei clients",
                "porta >");
        portSpecified.fireStateEvent();

        return super.entryAction(cause);
    }

    public Event portSpecified() {
        return portSpecified;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        network.setupServer(CommandPrompt.gotFromTerminal());
        super.exitAction(cause);
    }
}
