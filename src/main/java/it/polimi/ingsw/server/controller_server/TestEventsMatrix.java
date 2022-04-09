package it.polimi.ingsw.server.controller_server;

import javax.swing.JTextArea;

public class TestEventsMatrix implements ITransitionListener {

    private final Object x = ControllerServer.STATE_DO_NOTHING;
    private TestFrame frame;

    public TestEventsMatrix(TestFrame frame) {
        this.frame = frame;
        JTextArea jTextArea = frame.getTextArea();
        ///////////////////  Create Events  /////////////////////////////
        final Event ev_bonus = new Event("Bonus Event");
        final LetterEvent ev_b = new LetterEvent("b", jTextArea);
        final LetterEvent ev_i = new LetterEvent("i", jTextArea);
        final LetterEvent ev_l = new LetterEvent("l", jTextArea);
        final LetterEvent ev_capL = new LetterEvent("L", jTextArea);
        final LetterEvent ev_period = new LetterEvent(".", jTextArea);
        final LetterEvent ev_r = new LetterEvent("r", jTextArea);
        final LetterEvent ev_x = new LetterEvent("x", jTextArea);

        //////////////////////// Create States /////////////////////////////
        final State st_begin = new State("Begin");
        final State st_b = new State("B") {

            public IEvent entryAction(IEvent cause) {
                System.out.println("This is the stuff that takes place in state B");
                return null;
            }
        };

        final State st_bi = new State("BI"),  st_bil = new State("BIL"),  st_bill = new State("BILL");

        final State st_bonus = new State("Bonus") {

            public IEvent entryAction(IEvent cause) {
                System.out.println("Back to main line");
                return ev_bonus;
            }
        };
        final State st_exit = new State("Exit") {

            public IEvent entryAction(IEvent cause) {
                System.exit(0);
                return null;
            }
        };

        // Bring it all together, add listeners, etc.
        ControllerServer se=new ControllerServer("StateMatrix",  new Object[]{

//////////////// Define and activate Controller Matrix  ////////////////
                st_begin, st_b,     st_bi,    st_bil,   st_bill,  st_bonus, st_exit,
                ev_bonus,  x,        x,        x,        x,        x,        st_bil,   x,
                ev_b,      st_b,     x,        x,        x,        x,        x,        x,
                ev_i,      x,        st_bi,    x,        x,        x,        x,        x,
                ev_l,      x,        x,        st_bil,   st_bill,  x,        x,        x,
                ev_capL,   x,        x,        st_bonus, x,        x,        x,        x,
                ev_period, x,        x,        x,        x,        st_bill,  x,        x,
                ev_r,      x,        st_begin, st_begin, st_begin, st_begin, x,        x,
                ev_x,      x,        x,        x,        x,        st_exit,  x,        x

        }
        );
        // Optional, just reports when a transition occurs.
        se.addStateEngineTransactionListener(this);
    }

    public void newState(IState state, IEvent cause) {
        frame.setStatus("State >" + state.toString());
    }
    public static void main(String[] args) {
        new TestEventsMatrix(new TestFrame());
    }
}

