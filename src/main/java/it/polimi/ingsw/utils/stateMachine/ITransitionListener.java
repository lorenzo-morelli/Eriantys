package it.polimi.ingsw.utils.stateMachine;

/**
 * Status change notification (logging purpose)
 */
public interface ITransitionListener  {

    void newState(IState state, IEvent cause);
}