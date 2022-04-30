package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.ParametersFromTerminal;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class Read_UserInfo extends Read_from_terminal{

    public Read_UserInfo(View view, Model model, int numofparameters, String type) throws IOException {
        super(view, model, numofparameters, type);
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        view.askConnectionInfo();
        return null;
    }
    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
        if (cause instanceof ParametersFromTerminal) {
            //NICKNAME / IP / PORTA
            model.setNickname(model.getFromTerminal().get(0));
            model.setIp(model.getFromTerminal().get(1));
            model.setPort(model.getFromTerminal().get(2));
        }
    }
}
