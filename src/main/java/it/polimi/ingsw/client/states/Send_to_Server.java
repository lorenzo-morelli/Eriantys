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
    NetworkIssue message_issue;

    public Send_to_Server(View view, Model model,String type){
        super("[STATO di invio messaggi al server interpretati come :]"+ type);
        this.view = view;
        this.model = model;
        this.type = type;
        message_issue= new NetworkIssue("SOMETHING_STRANGE_HAPPENNED...");
        ack= new MessageReceived("MESSAGE_SENT_CORRECTLY");
    }

    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        switch (this.type) {
            case ("CREATIONPARAMETERS"):
                Network.send("CREATIONPARAMETERS "+ Network.getMyIp() + " " + model.getNickname() +" "+ model.getGamemode());
                // CONTESTO: lato server crea partita e invia codice
                // todo: aspetta -> ricevuto ack CODICEPARTITA vai avanti
                Recevied_ack();
                // try catch: se riceve qualsiasi altra cosa che non sia CODICEPARTITA:
                send_failed();
                break;
            case ("CARDCHOOSED"):
                Network.send("CARDCHOOSED " + model.getCardChoosed());
                // CONTESTO: lato server inserisce carta tra le carte scelte per il client
                // todo: aspetta -> ricevuto ack CARTASCELTA vai avanti
                Recevied_ack();
                // try catch: se riceve qualsiasi altra cosa che non sia CARTASCELTA:
                send_failed();
                break;
            case ("STUDENT_TOSCHOOL"):
                Network.send("STUDENT_TOSCHOOL " + model.getStudent_in_entrance_Choosed());
                // CONTESTO: lato server inserisce studente nella school board del client ed esegue funzioni correlate
                // todo: aspetta -> ricevuto ack STUDENT_PLACED vai avanti
                Recevied_ack();
                // try catch: se riceve qualsiasi altra cosa che non sia STUDENT_PLACED:
                send_failed();
            case("STUDENT_TOISLAND"):
                Network.send("STUDENT_TOISLAND " + model.getStudent_in_entrance_Choosed() + " " + model.getIsland_Choosed());
                // todo: aspetta -> ricevuto ack STUDENT_PLACED vai avanti
                Recevied_ack();
                // try catch: se riceve qualsiasi altra cosa che non sia STUDENT_PLACED:
                send_failed();
                break;
        }
        return null;
    }

    public MessageReceived Recevied_ack(){ return ack; }
    public NetworkIssue send_failed(){ return message_issue; }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
    }
}
