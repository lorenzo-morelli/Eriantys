package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class ChooseAssistentCardPhase extends State {
    Model model;
    View view;
    ParametersFromTerminal Choose_Card;
    Message_Received Choice_Registered;
    Message_Sended Choice_Sended;
    Message_Received End_Phase;
    ParametersFromTerminal insertedCard;
    IncorrectParameters numberOfParametersIncorrect;

    public ChooseAssistentCardPhase(View view, Model model) throws IOException {
        super("[STATO di scelta Carta Assistente]");
        this.view = view;
        this.model = model;
        Choice_Registered = new Message_Received("CHOICE_REGISTERED");
        Choice_Sended = new Message_Sended("CHOICE_SENDED");
        End_Phase = new Message_Received("PHASE_ENDED");
        Choose_Card= new ParametersFromTerminal(model, 1);

    }

    public ParametersFromTerminal insertedCard() {
        return insertedCard;
    }

    public IncorrectParameters numberOfParametersIncorrect() {
        return numberOfParametersIncorrect;
    }

    public Message_Received choice_correctly_registred() {
        return Choice_Registered;
    }

    public Message_Received go_to_wait() {
        return End_Phase;
    }

    public Message_Sended choice_sended() { return Choice_Sended; }


    public IEvent entryAction(IEvent cause) throws IOException {
        view.setCallingState(this);
        //todo: ricevi da server le carte assistenti disponibili e stampale a video
        view.ask_carta_assistente();   // NOTA: la scelta sarà un numero tra le carte assistenti disponibili
        if (cause instanceof ParametersFromTerminal) {
            //NOTA: ora model.getFromTerminal().get(0) conterrà l'intero scelto'
            //invia
        }
        return null;
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
    }

}
