package it.polimi.ingsw.utils.stateMachine;

import java.io.IOException;

/**
        * To override you can use setStateEventListener (set instead of add
        * because you don't need more than one listener)
        *
        * You should re-implement toString to return some useful information.

 */
public interface IEvent {

    public void fireStateEvent() throws Exception;
    public void setStateEventListener(Controller engine);
}

