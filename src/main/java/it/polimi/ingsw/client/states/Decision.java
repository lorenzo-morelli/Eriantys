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
        String type;
        RecognizedString scelta1, scelta2;
        NotRecognizedSetOfStrings nessunaDellePrecedenti;


        public Decision(View view, Model model, String type) throws IOException {
            super("[Decisione]");
            this.view = view;
            this.model = model;
            this.type =type;
            switch (type) {
                case ("CREATEORCONNECT"):
                    scelta1 = new RecognizedString("create");
                    scelta2 = new RecognizedString("connect");
                    nessunaDellePrecedenti = new NotRecognizedSetOfStrings(new ArrayList<>(Arrays.asList("create", "connect")));
                    break;
                case ("ISLANDORSCHOOL"):
                    scelta1 = new RecognizedString("island");
                    scelta2 = new RecognizedString("school");
                    nessunaDellePrecedenti = new NotRecognizedSetOfStrings(new ArrayList<>(Arrays.asList("island", "school")));
                    break;
            }
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
            switch (this.type) {
                case ("CREATEORCONNECT"):
                    view.askConnectOrCreate();
                    break;
                case ("ISLANDORSCHOOL"):
                    view.askIslandOrSchool();
                    break;
            }
            return null;
        }
    }
