package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class Read_from_terminal extends State {
    Model model;
    View view;
    String type;
    int parameter;
    ParametersFromTerminal insertedParameters;
    IncorrectParameters numberOfParametersIncorrect;

    public Read_from_terminal(View view, Model model, int numofparameters, String type) throws IOException {
        super("[STATO di lettura di " + numofparameters + " parametri da terminale interpretati come :"+ type+ "]");
        this.view = view;
        this.model = model;
        this.parameter = numofparameters;
        this.type = type;

        insertedParameters = new ParametersFromTerminal(model, numofparameters);
        numberOfParametersIncorrect = new IncorrectParameters(model, numofparameters);
    }

    public ParametersFromTerminal insertedParameters() {
        return insertedParameters;
    }

    public IncorrectParameters numberOfParametersIncorrect() {
        return numberOfParametersIncorrect;
    }


    public String getType() {
        return type;
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        view.askParameters();
        return null;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
    }
}
