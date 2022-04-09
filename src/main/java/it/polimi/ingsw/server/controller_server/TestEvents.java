package it.polimi.ingsw.server.controller_server;

import javax.swing.JTextArea;

/**
 * This is a StateMachine demo class
 */
public class TestEvents {
    // ////////////////////// DECLARE AND INSTANTIATE STATES
    // Each state can either inherit from the State base class when
    // it's instantiated (anynomous inner class)
    // OR they can be their own class (and have parent/child classes, etc). 
    // Actually any java construct will work.

    // These overriden Action methods are where the implementation is done.
    private final State STATE_BEGIN = new State("Begin");
    private final State STATE_B = new State("B") {

        // Note the event is passed in--and it can contain info about whatever
        // caused it.
        public IEvent entryAction(IEvent cause) {
            System.out.println("This is the stuff that takes place in state B");
            return null;
        }
    };
    // Just defining a few extra states, but states would generally be useless
    // without overriding an Action method
    private final State STATE_BI = new State("BI");
    private final State STATE_BIL = new State("BIL");
    private final State STATE_BILL = new State("BILL");
    private final State STATE_BONUS = new State("Bonus") {
        public IEvent entryAction(IEvent cause) {
            System.out.println("Back to main line");
            return EVENT_BONUS;
        }
    };

    // /////////////////////// DECLARE EVENTS
    private final LetterEvent EVENT_B,  EVENT_I,  EVENT_L,  EVENT_CAPL,  EVENT_PERIOD;
    private final Event EVENT_BONUS = new Event("Bonus Event");

    public TestEvents(JTextArea jTextArea) {
        // /////////////////// INSTANTIATE EVENTS
        // Event.fireStateEvent() must be called to make an event "execute"
        //
        // Events don't HAVE to be subclassed, but something has to execute
        // them.
        // They don't have to be instantiated in the class--could be done where
        // they are defined-- I did it here because I needed to pass them an
        // event source (jTextArea)
        // These "LetterEvents" just add a listener to jTextArea and if the
        // key typed is the one passed in here, they fire.
        EVENT_B = new LetterEvent("b", jTextArea);
        EVENT_I = new LetterEvent("i", jTextArea);
        EVENT_L = new LetterEvent("l", jTextArea);
        EVENT_CAPL = new LetterEvent("L", jTextArea);
        EVENT_PERIOD = new LetterEvent(".", jTextArea);

        // ///////////////////// CREATE STATE ENGINE
        // Must pass in a valid initial state when creating the state engine
        // itself.
        ControllerServer e = new ControllerServer("TestStates", STATE_BEGIN);

        // ///////////////////// declare every
        // These lines define the entire workflow of the engine. When in the
        // first state,
        // if the event in the middle slot fires, it moves to the state passed
        // in as the last parameter.

        // Any transition not incuded here will throw an exception telling you
        // what
        // state/event combination got you into trouble.
        e.addTransition(STATE_BEGIN, EVENT_B, STATE_B);
        e.addTransition(STATE_B, EVENT_I, STATE_BI);

        // The next two transitions have the same starting state but are
        // differentiated by event (and of course, the same event can transition
        // you from more than one state, or all states if you pass in null for
        // the beginning state (See the example for reset below)
        e.addTransition(STATE_BI, EVENT_L, STATE_BIL);
        e.addTransition(STATE_BI, EVENT_CAPL, STATE_BONUS);

        // if you hit a capital L instead of lower-case, it will send you to
        // a bonus state that will return an event immediately redirecting it to
        // STATE_BIL
        // without requiring the event to actually "fire" on its own.
        e.addTransition(STATE_BONUS, EVENT_BONUS, STATE_BIL);

        e.addTransition(STATE_BIL, EVENT_L, STATE_BILL);
        e.addTransition(STATE_BILL, EVENT_PERIOD, STATE_BILL);

        // I suppose if you really wanted to, you could avoid defining the
        // event variables and some state variables altogether (only for
        // states that are never referred to by another transtion of course.)
        // Like this:
        e.addTransition(STATE_BILL, new LetterEvent("x", jTextArea), new State(
                "Bonus") {

            public IEvent entryAction(IEvent cause) {
                System.exit(0);
                return null;

            }
        });

        // Reset from any state
        LetterEvent reset = new LetterEvent("r", jTextArea);
        e.addTransition(null, reset, STATE_BEGIN);
        // Except "Begin"
        e.addTransition(STATE_BEGIN, reset, null);

        // ///////////////////////// COMPLETELY DONE!
        // From now on the state machine will monitor the events,
        // transition states, and call the state methods
        // Note: to reclaim memory (if necessary), each listener must be
        // removed. Destroying the main frame in this demo should free this
        // entire
        // state engine
    }

    public static void main(String[] args) {
        TestFrame tf=new TestFrame();
        new TestEvents(tf.getTextArea());
        // I modified this for the Matrix version, don't feel like doing it here yet.
        tf.setStatus("See STDOUT");
    }
}

