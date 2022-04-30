package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Model;
import it.polimi.ingsw.client.events.*;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

    public class Decision extends State {

        View view;
        Model model;
        String type;
        RecognizedString scelta1, scelta2;
        NotRecognizedSetOfStrings nessunaDellePrecedenti;


        public Decision(View view, Model model, String type) throws IOException {
            super("[Decisione :]"+ type);
            this.view = view;
            this.model = model;
            this.type =type;
        }

        public RecognizedString haScelto1() {
            return scelta1;
        }

        public RecognizedString haScelto2() {
            return scelta2;
        }

        public NotRecognizedSetOfStrings sceltaNonValida(){
            return nessunaDellePrecedenti;
        }

        public IEvent entryAction(IEvent cause) throws IOException{
            view.setCallingState(this);
            return null;
        }
    }
