package it.polimi.ingsw.client.view.cli.cliController.states;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.cli.cliController.events.IncorrectNumberOfParameters;
import it.polimi.ingsw.client.view.cli.cliController.events.ParametersFromTerminal;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

/**
 * This state manages the generic reading of information from stdin by the controller
 * (therefore it should be understood as: the controller is reading what the user is writing).
 * The user is expected to type a specified number of parameters (strings) separated by a space
 * and then hit enter when he thinks he's done.
 */
public class ReadFromTerminal extends State {
    final ClientModel clientModel;
    final View view;
    final String type;
    ParametersFromTerminal fromTerminal;
    final IncorrectNumberOfParameters numberOfParametersIncorrect;
    final Event insertedParameters;

    public ReadFromTerminal(View view, ClientModel clientModel, Controller controller, int numofparameters, String type) throws IOException {
        super("[STATO di lettura di " + numofparameters + " parametri da terminale interpretati come :"+ type+ "]");
        this.view = view;
        this.clientModel = clientModel;
        this.type = type;

        insertedParameters = new Event("Inserimento da terminale di tipo " +type );
        numberOfParametersIncorrect = new IncorrectNumberOfParameters(numofparameters);
        fromTerminal = new ParametersFromTerminal(clientModel, numofparameters);
        insertedParameters.setStateEventListener(controller);
    }

    public Event insertedParameters() {
        return insertedParameters;
    }

    public IncorrectNumberOfParameters numberOfParametersIncorrect() {
        return numberOfParametersIncorrect;
    }

    public String getType() {
        return type;
    }

    public IEvent entryAction(IEvent cause) throws Exception {
        view.setCallingState(this);
        view.askParameters();

        insertedParameters.fireStateEvent();

        return null;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
    }
}
