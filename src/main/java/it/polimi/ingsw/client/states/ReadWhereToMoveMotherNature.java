package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.events.ConnectedToServer;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import javax.naming.ldap.Control;
import java.io.IOException;

public class ReadWhereToMoveMotherNature extends ReadFromTerminal {
    public ReadWhereToMoveMotherNature(View view, ClientModel clientModel, Controller controller) throws IOException {
        super(view, clientModel,controller, 1, "WHEREMOVEMOTHER");
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        clientModel.setMother_movement_Choosed(Integer.parseInt(clientModel.getFromTerminal().get(0)));
    }
}

