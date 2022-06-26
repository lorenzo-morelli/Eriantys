package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.stateMachine.State;

public interface View {
    /**
     * Il metodo serve alla Vista per memorizzare una reference allo stato
     * (schermata video/cli) che ha aggiornato la vista
     * @param callingState Stato del controllore che ha aggiornato la vista
     */
    void setCallingState(State callingState);
    void setClientModel(ClientModel clientModel);
    void askToStart() throws InterruptedException;
    void askDecision(String option1, String option2) throws InterruptedException;
    void askParameters() throws InterruptedException;

    /**
     * How I client react if the server makes me a request to interact via the terminal
     */
    void requestToMe() throws InterruptedException;

    /**
     * How do I client react if another client receives an interaction request from the terminal
     */
    void requestToOthers();

    /**
     * How do I client react in case of another client's reply to the server
     */
    void response();

    void requestPing();
}
