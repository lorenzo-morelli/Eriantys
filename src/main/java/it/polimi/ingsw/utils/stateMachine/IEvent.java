package it.polimi.ingsw.utils.stateMachine;

/**
        * To override you can use setStateEventListener (set instead of add
        * because you don't need more than one listener)
        *
        * You should re-implement toString to return some useful information.

 */
@SuppressWarnings("unused")
public interface IEvent {

    void fireStateEvent() throws Exception;
    void setStateEventListener(Controller engine);
}

