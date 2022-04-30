package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class ConnectGameScreen extends State{
    Model model;
    View view;
    ParametersFromNetwork creationSuccessful;

    public ConnectGameScreen(View view, Model model) throws IOException {
        super("[STATO di attesa connessione a partita]");
        this.view = view;
        this.model = model;
        creationSuccessful = new ParametersFromNetwork(2);  // myIp CREATION_SUCCESSFULL
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        if(creationSuccessful.parametersReceived()){
            // Ho ricevuto un messaggio inviato a me (conteneva il mio Ip)
            if (creationSuccessful.getParameter(0).equals(Network.getMyIp())){
                if (creationSuccessful.getParameter(1).equals("CREATION_SUCCESSFUL")){
                    // todo: vista.printaCreazioneAvvenutaConSuccesso()
                    // per il momento gestito con un messaggio di log
                    System.out.println("[Creazione avvenuta con successo]");
                }
                else if(creationSuccessful.getParameter(1).equals("NICKNAME_ALREADY_EXIST")){
                    System.out.println("[Nickname già esistente]");
                }
            }
        }
        return null;
    }

    public ParametersFromNetwork creationSuccessful(){ return creationSuccessful; }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
        // todo: connettiti alla partita con codice partita
        // server si mette in attesa della connessione di tutti i giocatori
        // aspetta che server inizi partita
    }

}
