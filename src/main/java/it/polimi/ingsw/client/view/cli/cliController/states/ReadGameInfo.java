package it.polimi.ingsw.client.view.cli.cliController.states;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

/**
 * Read the game modes chosen by the user, to facilitate debugging a
 * preset choice is provided by pressing the simple enter without writing any parameters.
 */
public class ReadGameInfo extends ReadFromTerminal {
    public ReadGameInfo(View view, ClientModel clientModel, Controller controller) throws IOException {
        super(view, clientModel, controller, 2, "GAMEINFO");
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        if (clientModel.getFromTerminal().get(0).equals("")){
            clientModel.setNumOfPlayers(4);
            clientModel.setGameMode("EXPERT");
            return;
        }
        clientModel.setNumOfPlayers(Integer.parseInt(clientModel.getFromTerminal().get(0)));
        clientModel.setGameMode(clientModel.getFromTerminal().get(1));
        super.exitAction(cause);
    }
}
