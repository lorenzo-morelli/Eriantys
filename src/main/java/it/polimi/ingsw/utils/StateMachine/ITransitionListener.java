package it.polimi.ingsw.utils.StateMachine;

import it.polimi.ingsw.utils.StateMachine.IEvent;
import it.polimi.ingsw.utils.StateMachine.IState;

/**
 * Notifica il cambiamento di stato (finalita' di logging)
 */
public interface ITransitionListener  {

    public void newState(IState state, IEvent cause);
}