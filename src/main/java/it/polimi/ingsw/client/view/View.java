package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public interface View {
    /**
     * Il metodo serve alla Vista per memorizzare una reference allo stato
     * (schermata video/cli) che ha aggiornato la vista
     * @param callingState Stato del controllore che ha aggiornato la vista
     */
    void setCallingState(State callingState);
    void setClientModel(ClientModel clientModel);
    void askToStart();
    void askDecision(String option1, String option2);
    void askParameters();

    /**
     * Come reagisco io client se il server mi fa una richiesta di interagire tramite terminale
     */
    void requestToMe() throws InterruptedException;

    /**
     * Come reagisco io client se un altro client riceve una richiesta di interazione da terminale
     */
    void requestToOthers() throws IOException;

    /**
     * Come reagisco io client in caso di risposta di un altro client al server
     * @throws IOException
     */
    void response() throws IOException;

    void requestPing();
}
