package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.events.ParametersFromTerminal;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class Read_GameCode extends Read_from_terminal {
    public Read_GameCode(View view, Model model) throws IOException {
        super(view, model, 1, "GAMECODE");
    }
    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
        if (cause instanceof ParametersFromTerminal) {
            //GAMECODE
            model.setGameCodeNumber(Integer.parseInt(model.getFromTerminal().get(0)));
        }
    }
}
