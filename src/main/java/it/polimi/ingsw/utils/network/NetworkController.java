package it.polimi.ingsw.utils.network;

import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import javax.swing.*;

/**
 * Questa classe demo e' una macchina a stati che riceve, come eventi, messaggi da
 * socket TCP, i quali triggherano un cambiamento di stato
 */
public class NetworkController {
    // ////////////////////// DECLARE AND INSTANTIATE STATES
    // Each state can either inherit from the State base class when
    // it's instantiated (anynomous inner class)
    // OR they can be their own class (and have parent/child classes, etc).
    // Actually any java construct will work.

    // These overriden Action methods are where the implementation is done.
    private final State STATE_BEGIN = new State("TRY TO CONNECT");
    private final State STATE_SETUP = new State("SETUP") {

        // Note the event is passed in--and it can contain info about whatever
        // caused it.
        public IEvent entryAction(IEvent cause) {
            System.out.println("Attesa invio di nickname da parte del giocatore");
            return null;
        }
    };
    // Just defining a few extra states, but states would generally be useless
    // without overriding an Action method
    private final State STATE_PLANNING = new State("PLANNING") {
        public IEvent entryAction(IEvent cause) {
            System.out.println("Attesa della scelta della carta assistente");
            return null;
        }
    };

    // /////////////////////// DECLARE EVENTS
    private final MessageEvent EVENT_connect, EVENT_plan;

    public NetworkController(JTextArea jTextArea) {

        EVENT_connect = new MessageEvent("nickname", jTextArea);
        EVENT_plan = new MessageEvent("assistantcard", jTextArea);

        Controller e = new Controller("Controllore server principale", STATE_BEGIN);

        e.addTransition(STATE_BEGIN, EVENT_connect, STATE_SETUP);
        e.addTransition(STATE_SETUP, EVENT_plan, STATE_PLANNING);


    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        NetworkManager tf3 = new NetworkManager();
        NetworkManager tf2 = new NetworkManager();
        NetworkManager tf = new NetworkManager();
        new NetworkController(tf.getTextArea());
    }
}
