package it.polimi.ingsw.utils.StateMachine;

import it.polimi.ingsw.utils.StateMachine.Controller;

/**
 * Per fare l'override si puo' usare setStateEventListener (set invece di add
 * perche' non c'e' bisogno di piu' di un listener)
 *
 * Si dovrebbe reimplementare toString per ritornare qualche informazione utile.

 */
public interface IEvent {

    public void fireStateEvent();
    public void setStateEventListener(Controller engine);
}

