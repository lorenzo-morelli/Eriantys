package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.MessageReceived;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class CreateGame extends State{
    Model model;
    View view;
    MessageReceived Game_code;
    NetworkIssue CreationProblem;

    public CreateGame(View view, Model model){
        super("[STATO di attesa info di setup per la nuova partita (CreateGame.java)]");
        this.view = view;
        this.model = model;
        CreationProblem= new NetworkIssue("SOMETHING_STRANGE_HAPPENNED...");
        Game_code= new MessageReceived("RECEVIED_GAME_CODE");
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        return null;
    }

    public MessageReceived Recevied_game_code(){ return Game_code; }
    public NetworkIssue Creation_failed(){ return CreationProblem; }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
        // todo: crea gioco <- invia al server numero di giocatori e game mode scelto
        Network.send(Network.getMyIp() + " "+model.getNickname()+" CREATE PRINCIPIANT 2"); //buona idea!
        // CONTESTO: lato server nel frattempo crea partita e invia codice
        // todo: aspetta che server crei gioco -> ricevuto ack CODICEPARTITA printa sulla view il codice partita, vai avanti
        Recevied_game_code();
        // try catch: se riceve qualsiasi altra cosa che non sia CODICEPARTITA:
        Creation_failed();
    }
}
