package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Decision extends State {

        View view;
        Model model;
        RecognizedString scelta1Riconosciuta, scelta2Riconosciuta;
        NotRecognizedSetOfStrings nessunaDellePrecedenti;
        String scelta1;
        String scelta2;


        public Decision(View view, Model model, String scelta1, String scelta2) throws IOException {
            super("[Decisione tra " +scelta1+" e " + scelta2 +"]"  );
            this.view = view;
            this.model = model;
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

        public NotRecognizedSetOfStrings sceltaNonValida(){
            return nessunaDellePrecedenti;
        }

        public IEvent entryAction(IEvent cause) throws IOException{
            view.setCallingState(this);

            return null;
        }
    }
