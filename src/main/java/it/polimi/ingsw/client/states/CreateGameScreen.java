package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class CreateGameScreen extends State{
    Model model;
    View view;
    Network network;
    ParametersFromTerminal insertUserInfo;
    Event idle;

    public CreateGameScreen(View view, Model model, Network network) throws IOException {
        super("[STATO di attesa info di setup per la nuova partita]");
        this.view = view;
        this.model = model;
        this.network = network;
        insertUserInfo = new ParametersFromTerminal(model, 2); // numero di giocatori / modalita di gioco
        idle= new Event("Evento Ritorno");
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        try {
            idle.fireStateEvent();
        } catch(InterruptedException e){}
        return null;
    }

    public Event go_to_wait(){ return idle; }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
        network.send("CREATE");
        //invia al server numero di giocatori e game mode scelto
        //lato server nel frattempo crea partita e invia codice
        //ricevi codice partita
        //lato server si mette in attesa della connessione di tutti i giocatori
        //aspetta che server inizi partita
    }

}
