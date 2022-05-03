package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class ReadWichStudent extends ReadFromTerminal {
    public ReadWichStudent(View view, ClientModel clientModel, Controller controller) throws IOException {
        super(view, clientModel, controller,1, "WICHSTUDENT");
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        clientModel.setStudent_in_entrance_Choosed(Integer.parseInt(clientModel.getFromTerminal().get(0)));
    }
}