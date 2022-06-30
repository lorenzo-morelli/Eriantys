package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.stateMachine.State;

public interface View {
    /**
     * The method is for the View to store a reference to the state
     * (video / cli screen) that updated the view
     * @param callingState Status of the controller that updated the view
     */
    void setCallingState(State callingState);
    void setClientModel(ClientModel clientModel);
    @SuppressWarnings("unused")
    void askToStart() throws InterruptedException;
    void askDecision(String option1, String option2) throws InterruptedException;
    void askParameters() throws InterruptedException;

    /**
     * How I react if the server makes me a request to interact via the terminal
     */
    void requestToMe() throws InterruptedException;

    /**
     * How do I react if another client receives an interaction request from the terminal
     */
    void requestToOthers();

    /**
     * How do I react in case of another client's reply to the server
     */
    void response();

    void requestPing();
}
