package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateOrConnectDecision extends State {

    View view;
    Model model;

    RecognizedString creaNuovoGioco, connettitiAdUnGioco;
    NotRecognizedSetOfStrings nessunaDellePrecedenti;


    public CreateOrConnectDecision(View view, Model model) throws IOException {
        super("[Decisione se creare una nuova partita o collegarsi ad una esistente (CreateOrConnectDecision.java)]");
        this.view = view;
        this.model = model;
        creaNuovoGioco = new RecognizedString("create");
        connettitiAdUnGioco = new RecognizedString("connect");
        nessunaDellePrecedenti = new NotRecognizedSetOfStrings(new ArrayList<>(Arrays.asList("create", "connect")));
    }

    public RecognizedString haSceltoConnetti() {
        return connettitiAdUnGioco;
    }

    public RecognizedString haSceltoCrea() {
        return creaNuovoGioco;
    }

    public NotRecognizedSetOfStrings sceltaNonValida(){
        return nessunaDellePrecedenti;
    }

    public IEvent entryAction(IEvent cause) throws IOException{


        view.setCallingState(this);
        view.askConnectOrCreate();

        return null;
    }
}
