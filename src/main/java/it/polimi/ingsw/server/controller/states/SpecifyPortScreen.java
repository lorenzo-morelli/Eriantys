package it.polimi.ingsw.server.controller.states;

import it.polimi.ingsw.client.view.cli.CommandPrompt;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This state handles the the specification of the port by the server administrator. A default port is provided,
 * carefully chosen so as not to conflict with existing services in unix and windows operating systems)
 */
public class SpecifyPortScreen extends State {
    private final Event portSpecified;

    private static final String PORT_REGEX =
            "^([1-9]\\d{0,3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$";


    private static final Pattern PORT_PATTERN = Pattern.compile(PORT_REGEX);

    public SpecifyPortScreen(){
        super("[Specifica porta dove mettersi in ascolto dei clients]");
        portSpecified = new Event("Porta specificata");

    }


    public static boolean isValidPort(String port) {
        if (port == null) {
            return false;
        }

        Matcher matcher = PORT_PATTERN.matcher(port);

        return matcher.matches();
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        if (!Network.isServerListening()) {
            CommandPrompt.ask(
                    "Inserire la porta sulla quale mettersi in ascolto dei clients [ENTER with empty string: default port 1234]",
                    "porta >");
        }
        // empty string --> default port
        if (CommandPrompt.gotFromTerminal().equals("")){
            CommandPrompt.forceInput("1234");
        }

        while (!isValidPort(CommandPrompt.gotFromTerminal())) {
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

}
