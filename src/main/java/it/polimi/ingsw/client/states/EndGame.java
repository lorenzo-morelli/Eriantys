package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class EndGame extends State {
    View view;
    Model model;
    public EndGame(View view, Model model) {
        super("[Stato finale (EndGame.java)]");
        this.view = view;
        this.model = model;
    }
    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        //todo: mettiti in attesa di un messaggio VINCITORE: "string winner"
        String winner="PIPPO";
        view.showendscreen(winner);
        return null;
    }

}
