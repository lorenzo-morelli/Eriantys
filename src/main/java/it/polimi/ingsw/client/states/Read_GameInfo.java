package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.events.ParametersFromTerminal;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class Read_GameInfo extends Read_from_terminal {
    public Read_GameInfo(View view, Model model, int numofparameters, String type) throws IOException {
        super(view, model, numofparameters, type);
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        view.askGameInfo();
        return null;
    }
    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
        if (cause instanceof ParametersFromTerminal) {
            //NUMOFPLAYER / GAMEMODE
            model.setNumofplayer(Integer.parseInt(model.getFromTerminal().get(0)));
            model.setGamemode(model.getFromTerminal().get(1));
        }
    }
}
