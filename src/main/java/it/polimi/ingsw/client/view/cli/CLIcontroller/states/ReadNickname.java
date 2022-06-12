package it.polimi.ingsw.client.view.cli.CLIcontroller.states;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class ReadNickname extends ReadFromTerminal{
    public ReadNickname(View view, ClientModel clientModel, Controller controller) throws IOException {
        super(view, clientModel, controller, 1, "NICKNAMEEXISTENT");
    }
    @Override
    public void exitAction(IEvent cause) throws IOException {
        //NICKNAME
        clientModel.setNickname(clientModel.getFromTerminal().get(0));
    }
}
