package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.ConnectionInfo;
import it.polimi.ingsw.server.states.*;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;

import java.io.IOException;

public class ServerController{

    public ServerController() throws IOException, InterruptedException {
        CommandPrompt.setDebug();
        ConnectionInfo connectionInfo = new ConnectionInfo();

        // modo elegante di far partire il controllore
        Idle idle = new Idle();
        Event start = new Event("[Controller Started]");

        Controller fsm = new Controller("Controllore del Server", idle);

        //Costruzione degli stati necessari
        SpecifyPortScreen specifyPortScreen = new SpecifyPortScreen();
        //waitSpecificMessage = new WaitSpecificMessage();
        //private final WaitSpecificMessage waitSpecificMessage;
        WaitGameCreation waitGameCreation = new WaitGameCreation(connectionInfo);
        IsNicknameAlreadyExisting isNicknameAlreadyExisting = new IsNicknameAlreadyExisting(connectionInfo);

        // Dichiarazione delle transizioni tra gli stati
        fsm.addTransition(idle, start, specifyPortScreen);
        fsm.addTransition(specifyPortScreen, specifyPortScreen.portSpecified(), waitGameCreation);
        fsm.addTransition(waitGameCreation, waitGameCreation.getCreate(), isNicknameAlreadyExisting);

        // L'evento di start è l'unico che deve essere fatto partire manualmente
        start.fireStateEvent();
    }

    public static void main(String[] args) {
        try {
            new ServerController();
        } catch (InterruptedException | IOException ignored){}
    }
}
