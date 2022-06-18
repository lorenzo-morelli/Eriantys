package it.polimi.ingsw.utils.stateMachine;

/**
 * Status change notification (logging purpose)
 */
public interface ITransitionListener  {

    public void newState(IState state, IEvent cause);
}