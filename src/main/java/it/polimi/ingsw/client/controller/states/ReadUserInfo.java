package it.polimi.ingsw.client.controller.states;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class ReadUserInfo extends ReadFromTerminal {

    public ReadUserInfo(View view, ClientModel clientModel, Controller controller) throws IOException {
        super(view, clientModel, controller, 3, "USERINFO");
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
            //NICKNAME / IP / PORTA
            clientModel.setNickname(clientModel.getFromTerminal().get(0));
            clientModel.setIp(clientModel.getFromTerminal().get(1));
            clientModel.setPort(clientModel.getFromTerminal().get(2));
    }
}
