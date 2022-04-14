package it.polimi.ingsw.utils.stateMachine;

/**
 * Notifica il cambiamento di stato (finalita' di logging)
 */
public interface ITransitionListener  {

    public void newState(IState state, IEvent cause);
}