package it.polimi.ingsw.client.view.cli.CLIcontroller.states;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.cli.CLIcontroller.events.NotRecognizedSetOfStrings;
import it.polimi.ingsw.client.view.cli.CLIcontroller.events.RecognizedString;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Decision extends State {

        View view;
        ClientModel clientModel;
        RecognizedString scelta1Riconosciuta, scelta2Riconosciuta;
        NotRecognizedSetOfStrings nessunaDellePrecedenti;
        String scelta1;
        String scelta2;


        public Decision(View view, ClientModel clientModel, String scelta1, String scelta2) throws IOException {
            super("[Decisione tra " +scelta1+" e " + scelta2 +"]"  );
            this.view = view;
            this.clientModel = clientModel;
            this.scelta1 = scelta1;
            this.scelta2 = scelta2;
            scelta1Riconosciuta = new RecognizedString(scelta1);
            scelta2Riconosciuta = new RecognizedString(scelta2);
            nessunaDellePrecedenti = new NotRecognizedSetOfStrings(new ArrayList<>(Arrays.asList(scelta1, scelta2)));
        }

        public RecognizedString haScelto1() {
            return scelta1Riconosciuta;
        }

        public RecognizedString haScelto2() {
            return scelta2Riconosciuta;
        }

    public IEvent entryAction(IEvent cause) throws IOException, InterruptedException {
            view.setCallingState(this);
            view.askDecision(scelta1,scelta2);
            return null;
        }
    }
