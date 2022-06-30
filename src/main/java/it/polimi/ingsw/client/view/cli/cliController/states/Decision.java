package it.polimi.ingsw.client.view.cli.cliController.states;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.cli.cliController.events.NotRecognizedSetOfStrings;
import it.polimi.ingsw.client.view.cli.cliController.events.RecognizedString;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This state implements a generic decision between two command line options.
 * The user is therefore offered two strings, and he can choose only one of these two.
 * If the user writes a string that is not present between the two options, an error event
 * is generated which will be captured by the controller.
 */
@SuppressWarnings("unused")
public class Decision extends State {

        final View view;
        final ClientModel clientModel;
        final RecognizedString recognizedString1;
    final RecognizedString recognizedString2;
        final NotRecognizedSetOfStrings notRecognizedSetOfStrings;
        final String chosen1;
        final String chosen2;


        public Decision(View view, ClientModel clientModel, String choose1, String choose2) {
            super("[Decision between " +choose1+" and " + choose2 +"]"  );
            this.view = view;
            this.clientModel = clientModel;
            this.chosen1 = choose1;
            this.chosen2 = choose2;
            recognizedString1 = new RecognizedString(choose1);
            recognizedString2 = new RecognizedString(choose2);
            notRecognizedSetOfStrings = new NotRecognizedSetOfStrings(new ArrayList<>(Arrays.asList(choose1, choose2)));
        }

        public RecognizedString heChoose1() {
            return recognizedString1;
        }

        public RecognizedString heChoose2() {
            return recognizedString2;
        }

    public IEvent entryAction(IEvent cause) throws InterruptedException {
            view.setCallingState(this);
            view.askDecision(chosen1,chosen2);
            return null;
        }
    }
