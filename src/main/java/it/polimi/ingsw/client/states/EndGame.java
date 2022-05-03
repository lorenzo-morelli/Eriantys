package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class EndGame extends State {
    View view;
    ClientModel clientModel;
    public EndGame(View view, ClientModel clientModel) {
        super("[Stato finale (EndGame.java)]");
        this.view = view;
        this.clientModel = clientModel;
    }
    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        //todo: mettiti in attesa di un messaggio VINCITORE: "string winner"
        String winner="PIPPO";
        view.showendscreen(winner);
        return null;
    }

}
