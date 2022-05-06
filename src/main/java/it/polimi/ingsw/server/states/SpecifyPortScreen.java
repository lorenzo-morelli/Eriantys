package it.polimi.ingsw.server.states;

import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecifyPortScreen extends State {
    private Event portSpecified;

    private static final String PORT_REGEX=
            "^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";


    private static final Pattern PORT_PATTERN = Pattern.compile(PORT_REGEX);

    public SpecifyPortScreen() throws IOException {
        super("[Specifica porta dove mettersi in ascolto dei clients]");
        portSpecified = new Event("Porta specificata");

    }


    public static boolean isValidPort(String port)
    {
        if (port == null) {
            return false;
        }

        Matcher matcher = PORT_PATTERN.matcher(port);

        return matcher.matches();
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        if(!Network.isServerListening()) {
            CommandPrompt.ask(
                    "Inserire la porta sulla quale mettersi in ascolto dei clients",
                    "porta >");
        }

        while(!isValidPort(CommandPrompt.gotFromTerminal())){
            CommandPrompt.ask(
                    "La porta inserita non è nel formato giusto (numero da 0 to 65536)",
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
