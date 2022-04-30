package it.polimi.ingsw.client.states;

import com.sun.source.tree.NewArrayTree;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class CreateGameScreen extends State{
    Model model;
    View view;
    ParametersFromNetwork creationSuccessful;

    public CreateGameScreen(View view, Model model) throws IOException {
        super("[STATO di invio richiesta di connessione CreateGameScreen]");
        this.view = view;
        this.model = model;
        creationSuccessful = new ParametersFromNetwork(2); //ricevo "myIp CREATION_SUCCESSFUL"
    }

    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
        view.setCallingState(this);
        // invia al server numero di giocatori e game mode scelto
        Network.send(Network.getMyIp() + " "+model.getNickname()+" CREATE PRINCIPIANT 2");

        //lato server nel frattempo crea partita e invia response
        while(!creationSuccessful.parametersReceived()){
            // non ho ancora ricevuto la response da server
        }
        if(creationSuccessful.getParameter(0).equals(Network.getMyIp())) {
            System.out.println("[E' arrivato un messaggio per me]");
            if(creationSuccessful.getParameter(1).equals("CREATION_SUCCESSFUL")){
                System.out.println("[Il messaggio dice che ho creato la partita con successo]");
                creationSuccessful.fireStateEvent();
            }

        }
        //lato server si mette in attesa della connessione di tutti i giocatori
        //aspetta che server inizi partita
        return null;
    }
}
