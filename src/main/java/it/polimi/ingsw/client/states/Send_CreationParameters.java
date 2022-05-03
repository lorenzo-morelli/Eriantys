package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class Send_CreationParameters extends Send_to_Server {
    public Send_CreationParameters(View view, Model model, String type) {
        super(view, model, type);
    }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        Network.send(Network.getMyIp()+" "+ "CREATE "+ model.getGamemode() +" "+  model.getNumofplayer());
        // avvia timeout
        // CONTESTO: lato server crea partita e invia codice
        // todo: aspetta -> se ricevuto ack CODICEPARTITA fai
        try{
            ack.fireStateEvent();
            // -> se ricevuto ack INVALIDMESSAGE fai
            not_valid.fireStateEvent();
            // try catch: se scade timeout
            //message_issue.fireStateEvent(); TODO
        }
        catch(InterruptedException e){ }
        return null;
    }
}
