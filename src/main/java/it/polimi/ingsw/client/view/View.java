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

    void askConnectOrCreate();

    void showTryToConnect();

    void showCreatingGame();

}
