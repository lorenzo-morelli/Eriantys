package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.events.MessageReceived;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class WaitForStartGame extends WaitForServer {
    public WaitForStartGame(View view, ClientModel clientModel, String type) throws IOException {
        super(view, clientModel, type);
    }
    public MessageReceived start_game(){return GO; }
    @Override
    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        // todo: mettiti in attesa di un ACK "GAMESTART" da server
        view.showGameStarted();
        try {
            GO.fireStateEvent();
        }catch (InterruptedException e) {}
        return null;
    }
}
