package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.MessageReceived;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class Send_to_Server extends State{
    Model model;
    View view;
    String type;
    MessageReceived ack;
    MessageReceived not_valid;
    NetworkIssue message_issue;

    public Send_to_Server(View view, Model model,String type){
        super("[STATO di invio messaggi al server interpretati come :]"+ type);
        this.view = view;
        this.model = model;
        this.type = type;
        message_issue= new NetworkIssue("SOMETHING_STRANGE_HAPPENNED...");
        ack= new MessageReceived("MESSAGE_SENT_CORRECTLY");
        not_valid=new MessageReceived("MESSAGE_NOT_VALID");
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        return null;
    }

    public MessageReceived Recevied_ack(){ return ack; }
    public MessageReceived Message_not_valid(){ return not_valid; }
    public NetworkIssue send_failed(){ return message_issue; }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
    }
}
