package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.utils.cli.CommandPrompt;
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
    void showConnectingGame();
    void showWaitingForOtherPlayer();
    void showGameStarted();
    void itsyourturn(String command);
    void showendscreen(String winner);
    void askParameters();

    void requestToMe();

    void requestToOthers() throws IOException;

    void response() throws IOException;
}
