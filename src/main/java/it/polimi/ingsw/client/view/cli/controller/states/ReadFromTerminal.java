package it.polimi.ingsw.client.view.cli.controller.states;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.cli.controller.events.IncorrectNumberOfParameters;
import it.polimi.ingsw.client.view.cli.controller.events.ParametersFromTerminal;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class ReadFromTerminal extends State {
    ClientModel clientModel;
    View view;
    String type;
    ParametersFromTerminal fromTerminal;
    IncorrectNumberOfParameters numberOfParametersIncorrect;
    Event insertedParameters;

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
