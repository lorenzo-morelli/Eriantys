package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.NotRecognizedSetOfStrings;
import it.polimi.ingsw.client.events.RecognizedString;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.IEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateOrConnectDecision extends Decision {
    public CreateOrConnectDecision(View view, Model model, String type) throws IOException {
        super(view, model, type);
    }
    @Override
    public IEvent entryAction(IEvent cause) throws IOException{
        view.setCallingState(this);
        scelta1 = new RecognizedString("create");
        scelta2 = new RecognizedString("connect");
        nessunaDellePrecedenti = new NotRecognizedSetOfStrings(new ArrayList<>(Arrays.asList("create", "connect")));
        view.askConnectOrCreate();
        return null;
    }
}
