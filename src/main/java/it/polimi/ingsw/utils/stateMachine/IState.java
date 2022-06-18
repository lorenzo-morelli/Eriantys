package it.polimi.ingsw.utils.stateMachine;

import java.io.IOException;

/**

        * This abstract interface is used to implement the methods ("Actions" of a state on the model)
        * entryAction is executed when the state is accessed,
        * exitAction when you are about to exit the state and go to the next one
        *
        * If you use this interface you should override toString,
         * (for match logging)

          **/

public interface IState {
    /**
     * @param cause The event that caused the transition in this state
     * @return null unless you want to return an event
     */
    public IEvent entryAction(IEvent cause) throws Exception;

    /**
     *  As an entryaction, unlike this method is called when the controller is "exiting" the state
     */
    public void exitAction(IEvent cause) throws IOException;
}
