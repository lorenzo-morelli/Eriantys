package it.polimi.ingsw.server.controller_server;

/**
 * Notifica il cambiamento di stato (finalita' di logging)
 */
public interface ITransitionListener  {

    public void newState(IState state, IEvent cause);
}