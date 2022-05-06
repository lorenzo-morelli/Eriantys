package it.polimi.ingsw.server;

import it.polimi.ingsw.server.states.*;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;

import java.io.IOException;

public class ServerController{

    public ServerController() throws IOException, InterruptedException {
        ConnectionModel connectionModel = new ConnectionModel();

        // modo elegante di far partire il controllore
        Idle idle = new Idle();
        Event start = new Event("[Controller Started]");

        Controller fsm = new Controller("Controllore del Server", idle);

        //Costruzione degli stati necessari
        SpecifyPortScreen specifyPortScreen = new SpecifyPortScreen();
        WaitFirstPlayer waitFirstPlayer = new WaitFirstPlayer( fsm, connectionModel);
        WaitFirstPlayerGameInfo waitFirstPlayerGameInfo = new WaitFirstPlayerGameInfo(fsm,connectionModel);
        WaitOtherClients waitOtherClients = new WaitOtherClients(connectionModel,fsm);
        AskHelloToPlayerOne askHelloToPlayerOne = new AskHelloToPlayerOne(connectionModel);

        // Dichiarazione delle transizioni tra gli stati
        fsm.addTransition(idle, start, specifyPortScreen);
        fsm.addTransition(specifyPortScreen, specifyPortScreen.portSpecified(), waitFirstPlayer);
        fsm.addTransition(waitFirstPlayer,waitFirstPlayer.gotFirstMessage(),waitFirstPlayerGameInfo);
        fsm.addTransition(waitFirstPlayerGameInfo,waitFirstPlayerGameInfo.gotNumOfPlayersAndGamemode(),waitOtherClients);
        fsm.addTransition(waitOtherClients, waitOtherClients.allClientsConnected(), askHelloToPlayerOne);
        // L'evento di start è l'unico che deve essere fatto partire manualmente
        start.fireStateEvent();
    }

    public static void main(String[] args) {
        try {
            new ServerController();
        } catch (InterruptedException | IOException e){e.printStackTrace();}
    }
}
