package it.polimi.ingsw.client.states;

import com.sun.source.tree.NewArrayTree;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class CreateGameScreen extends State{
    Model model;
    View view;
    ParametersFromTerminal insertUserInfo;
    Event idle;

    public CreateGameScreen(View view, Model model) throws IOException {
        super("[STATO di attesa info di setup per la nuova partita]");
        this.view = view;
        this.model = model;
        insertUserInfo = new ParametersFromTerminal(model, 2); // numero di giocatori / modalita di gioco
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        return null;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
        Network.send(Network.getMyIp() + " "+model.getNickname()+" CREATE PRINCIPIANT 2");
        //invia al server numero di giocatori e game mode scelto
        //lato server nel frattempo crea partita e invia codice
        //ricevi codice partita
        //lato server si mette in attesa della connessione di tutti i giocatori
        //aspetta che server inizi partita
    }

}
