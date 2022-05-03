package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class ReadWichCloud extends ReadFromTerminal {
    public ReadWichCloud(View view, ClientModel clientModel, Controller controller) throws IOException {
        super(view, clientModel, controller,1, "WICHCLOUD");
    }
    @Override
    public void exitAction(IEvent cause) throws IOException {
        clientModel.setCloudChoosed(Integer.parseInt(clientModel.getFromTerminal().get(0)));
    }
}
