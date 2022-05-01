package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utils.stateMachine.State;

public interface View {
    /**
     * Il metodo serve alla Vista per memorizzare una reference allo stato
     * (schermata video/cli) che ha aggiornato la vista
     * @param callingState Stato del controllore che ha aggiornato la vista
     */
    void setCallingState(State callingState);

    void askConnectionInfo();
    void askToStart();
    void askDecision(String option1, String option2);
    void showTryToConnect();
    void showConnectingGame();
    void showWaitingForOtherPlayer();
    void showGameStarted();
    void ComunicationError();
    void ask_carta_assistente();
    void askGameCode();
    void askGameInfo();
    void itsyourturn(String command);
    void ask_witch_student();
    void askIslandOrSchool();
    void ask_witch_island();
    void askWheremove();
    void askwitchCloud();
    void showendscreen(String winner);
}
