package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class ConnectGameScreen extends State{
    Model model;
    View view;
    ParametersFromTerminal insertUserInfo;

    Event idle;

    public ConnectGameScreen(View view, Model model) throws IOException {
        super("[STATO di attesa connessione a partita]");
        this.view = view;
        this.model = model;
        insertUserInfo = new ParametersFromTerminal(model, 1); // codice partita
        idle = new Event("Evento ritorno");
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        view.showTryToConnect();
        return null;
    }

    public Event go_to_wait(){ return idle; }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
        // todo: connettiti alla partita con codice partita
        // server si mette in attesa della connessione di tutti i giocatori
        // aspetta che server inizi partita
        go_to_wait();
    }

}
