package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class CreateGameScreen extends State{
    Model model;
    View view;
    ParametersFromTerminal insertUserInfo;

    public CreateGameScreen(View view, Model model) throws IOException {
        super("[STATO di attesa info di setup per la nuova partita]");
        this.view = view;
        this.model = model;
        insertUserInfo = new ParametersFromTerminal(model, 2); // numero di giocatori / modalita di gioco
    }

    public ParametersFromTerminal userInfo() {
        return insertUserInfo;
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        view.showTryToConnect();
        return null;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
        //invia al server numero di giocatori e game mode scelto
        //ricevi codice partita
    }

}
