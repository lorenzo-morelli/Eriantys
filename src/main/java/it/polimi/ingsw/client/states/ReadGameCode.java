package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class ReadGameCode extends ReadFromTerminal {
    public ReadGameCode(View view, ClientModel clientModel, Controller controller) throws IOException {
        super(view, clientModel, controller, 1, "GAMECODE");
    }
    @Override
    public void exitAction(IEvent cause) throws IOException {
            //GAMECODE
            clientModel.setGameCodeNumber(Integer.parseInt(clientModel.getFromTerminal().get(0)));
    }

}
