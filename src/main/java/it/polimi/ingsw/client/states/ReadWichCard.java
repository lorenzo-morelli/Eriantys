package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class ReadWichCard extends ReadFromTerminal {
    public ReadWichCard(View view, ClientModel clientModel, Controller controller) throws IOException {
        super(view, clientModel, controller, 1, "WHICHCARD");
    }
    @Override
    public void exitAction(IEvent cause) throws IOException {
        clientModel.setCardChoosed(Integer.parseInt(clientModel.getFromTerminal().get(0)));
    }
}
