package it.polimi.ingsw.client.view.cli.CLIcontroller.states;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;
import java.util.Random;

public class ReadUserInfo extends ReadFromTerminal {

    public ReadUserInfo(View view, ClientModel clientModel, Controller controller) throws IOException {
        super(view, clientModel, controller, 3, "USERINFO");
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        if (clientModel.getFromTerminal().get(0).equals("")){
            Random rand = new Random();
            String generatedUsername = Integer.toString(rand.nextInt(10000));
            clientModel.setNickname(generatedUsername);
            clientModel.setIp("127.0.0.1");
            clientModel.setPort("1234");
            return;
        }
            //NICKNAME / IP / PORTA
            clientModel.setNickname(clientModel.getFromTerminal().get(0));
            clientModel.setIp(clientModel.getFromTerminal().get(1));
            clientModel.setPort(clientModel.getFromTerminal().get(2));
    }
}
