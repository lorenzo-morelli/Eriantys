package it.polimi.ingsw.client.view.cli.cliController.states;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.cli.cliController.events.NotRecognizedSetOfStrings;
import it.polimi.ingsw.client.view.cli.cliController.events.RecognizedString;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This state implements a generic decision between two command line options.
 * The user is therefore offered two strings, and he can choose only one of these two.
 * If the user writes a string that is not present between the two options, an error event
 * is generated which will be captured by the controller.
 */
public class Decision extends State {

        final View view;
        final ClientModel clientModel;
        final RecognizedString scelta1Riconosciuta;
    final RecognizedString scelta2Riconosciuta;
        NotRecognizedSetOfStrings nessunaDellePrecedenti;
        final String scelta1;
        final String scelta2;


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

    public IEvent entryAction(IEvent cause) throws InterruptedException {
            view.setCallingState(this);
            view.askDecision(scelta1,scelta2);
            return null;
        }
    }
