package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.events.MessageReceived;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class WaitForStartGame extends WaitForServer {
    public WaitForStartGame(View view, Model model, String type) throws IOException {
        super(view, model, type);
    }
    public MessageReceived start_game(){return GO; }
    @Override
    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        // todo: mettiti in attesa di un ACK "GAMESTART" da server
        view.showGameStarted();
        start_game();
        return null;
    }
}
