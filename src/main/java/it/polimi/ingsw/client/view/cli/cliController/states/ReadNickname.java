package it.polimi.ingsw.client.view.cli.cliController.states;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

/**
 * Read the nickname chosen by the user.
 */
public class ReadNickname extends ReadFromTerminal{
    public ReadNickname(View view, ClientModel clientModel, Controller controller) throws IOException {
        super(view, clientModel, controller, 1, "NICKNAMEEXISTENT");
    }
    @Override
    public void exitAction(IEvent cause) {
        //NICKNAME
        clientModel.setNickname(clientModel.getFromTerminal().get(0));
    }
}
