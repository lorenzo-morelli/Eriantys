package it.polimi.ingsw.utils.StateMachine;

import it.polimi.ingsw.utils.StateMachine.IEvent;
import it.polimi.ingsw.utils.StateMachine.IState;

public class State implements IState {

    private final String name;

    /**
     * Costruttore per forzare l'utilizzatore a dare un nome allo stato
     * @param name name used for logging purposes
     */
    public State(String name) {
        this.name = name;
    }

    /**
     * Questo evento verrà chiamato quando si passa a questo stato. Se questo
     * stato è lo stato Iniziale (passato al costruttore ControllerServer)
     * non verrà chiamato.
     *
     * Se viene restituito un valore, questo stato + l'evento restituito DEVE
     * essere nella tabella delle transazioni (per garantire che la tabella delle transazioni sia completa)
     *
     *
     * @param cause
     * @return any event but NULL causes the system to go straight to that state.
     */
    public IEvent entryAction(IEvent cause) {
        return null;
    }

    /**
     * Come entryActon, ma chiamato mentre stiamo uscendo da questo stato.
     * Questo non può causare un cambiamento di stato restituendo un evento
     * @param cause the event that caused us to exit this state
     */
    public void exitAction(IEvent cause) {

    }

    /**
     * for logging purposes
     * @return the name passed into the constructor.
     */
    public String toString() {
        return name;
    }
}

