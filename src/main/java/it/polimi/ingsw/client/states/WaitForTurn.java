package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.events.MessageReceived;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class WaitForTurn extends WaitForServer {
    private MessageReceived GO_1;
    private MessageReceived GO_2;
    private MessageReceived GO_3;
    private MessageReceived GO_4;
    private MessageReceived GO_5;

    public WaitForTurn(View view, ClientModel clientModel, String type) throws IOException {
        super(view, clientModel, type);
        GO_1 = new MessageReceived(type);
        GO_2 = new MessageReceived(type);
        GO_3 = new MessageReceived(type);
        GO_4 = new MessageReceived(type);
        GO_5 = new MessageReceived(type);
    }
    public MessageReceived go_to_assistantcardphase(){return GO_1; }
    public MessageReceived go_to_studentphase(){return GO_2; }
    public MessageReceived go_to_motherphase(){return GO_3; }
    public MessageReceived go_to_cloudphase(){return GO_4; }
    public MessageReceived go_to_endgame(){return GO_5; }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        String command = "DOASSISTANTCARDPHASE"; //messo solo per togliere l'errore
        // todo: mettiti in attesa di un messaggio command da server
        view.itsyourturn(type);
        try {
            switch (command) {  //command di 5 tipi: DOASSISTANTCARDPHASE/ DOSTUDENTPHASE / DOMOTHERPHASE / DOCLOUDPHASE /GOENDGAME
                case ("DOASSISTANTCARDPHASE"):
                    view.itsyourturn(command);
                    GO_1.fireStateEvent();
                    break;
                case ("DOSTUDENTPHASE"):
                    view.itsyourturn(command);
                    GO_2.fireStateEvent();
                    break;
                case ("DOMOTHERPHASE"):
                    view.itsyourturn(command);
                    GO_3.fireStateEvent();
                    break;
                case ("DOCLOUDPHASE"):
                    view.itsyourturn(command);
                    GO_4.fireStateEvent();
                case ("ENDGAME"):
                    GO_5.fireStateEvent();
                    break;
            }
        }catch(InterruptedException e){}
        return null;
    }
}

