package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.network.events.MessageReceived;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;

public class WaitForTurn extends WaitForServer {
    public WaitForTurn(View view, Model model, String type) throws IOException {
        super(view, model, type);
    }
    public MessageReceived go_to_assistantcardphase(){return GO; }
    public MessageReceived go_to_studentphase(){return GO; }
    public MessageReceived go_to_motherphase(){return GO; }
    public MessageReceived go_to_cloudphase(){return GO; }
    public MessageReceived go_to_endgame(){return GO; }

    @Override
    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        String command = "DOSTUDENTPHASE"; //messo solo per togliere l'errore
        // todo: mettiti in attesa di un messaggio command da server
        view.itsyourturn(type);
        switch (command) {  //command di 5 tipi: DOASSISTANTCARDPHASE/ DOSTUDENTPHASE / DOMOTHERPHASE / DOCLOUDPHASE /GOENDGAME
            case ("DOASSISTANTCARDPHASE"):
                view.itsyourturn(command);
                go_to_assistantcardphase();
                break;
            case ("DOSTUDENTPHASE"):
                view.itsyourturn(command);
                go_to_studentphase();
                break;
            case ("DOMOTHERPHASE"):
                view.itsyourturn(command);
                go_to_motherphase();
                break;
            case ("DOCLOUDPHASE"):
                view.itsyourturn(command);
                go_to_cloudphase();
            case ("ENDGAME"):
                go_to_endgame();
                break;
        }
        return null;
    }
}

