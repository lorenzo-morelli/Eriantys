package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class ConnectGame extends State{
    Model model;
    View view;
    Message_Received Game_started;
    NetworkIssue ConnectionFailed;

    public ConnectGame(View view, Model model) throws IOException {
        super("[STATO di attesa connessione a partita]");
        this.view = view;
        this.model = model;
        ConnectionFailed= new NetworkIssue("CONNECTION_TO_GAME_FAILED");
        Game_started= new Message_Received("GAME_STARTED");
    }

   public Message_Received Game_Started(){ return Game_started; }
    public NetworkIssue Connection_failed(){ return ConnectionFailed; }


    @Override
    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        // todo:  connettiti alla partita dato il codice partita //che si trovera a model.getFromTerminal().get(0)
        view.showConnectingGame();
        // CONTESTO: server ti inserisce nella partita
        // todo:  aspetta che server ti inserisca nella partita -> ricevuto ack JOINEDTOGAME vai avanti
        // try/catch : se riceve qualsiasi cosa che non sia JOINEDTOGAME:
        view.ComunicationError();
        Connection_failed();
        // else
        view.showWaitingForOtherPlayer();
        // CONTESTO: server si mette in attesa della connessione di tutti i giocatori
        // todo: aspetta che server inizi partita -> ricevuto ack GAMESTARTED vai avanti
        view.showGameStarted();
        Game_Started();
        return null;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
    }

}
