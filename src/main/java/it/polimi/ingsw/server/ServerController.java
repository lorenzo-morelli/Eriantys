package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.ConnectionInfo;
import it.polimi.ingsw.server.states.*;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;

import java.io.IOException;

public class ServerController{
    private final ConnectionInfo connectionInfo;

    private final Idle idle;    // modo elegante di far partire il controllore
    private final SpecifyPortScreen specifyPortScreen;

    //private final WaitSpecificMessage waitSpecificMessage;
    private final WaitGameCreation waitGameCreation;
    private final IsNicknameAlreadyExisting isNicknameAlreadyExisting;
    private final Event start;

    public ServerController() throws IOException, InterruptedException {
        CommandPrompt.setDebug();
        connectionInfo = new ConnectionInfo();

        idle = new Idle();
        start = new Event("[Controller Started]");

        Controller fsm = new Controller("Controllore del Server",idle);

        //Costruzione degli stati necessari
        specifyPortScreen = new SpecifyPortScreen();
        //waitSpecificMessage = new WaitSpecificMessage();
        waitGameCreation = new WaitGameCreation(connectionInfo);
        isNicknameAlreadyExisting = new IsNicknameAlreadyExisting(connectionInfo);

        // Dichiarazione delle transizioni tra gli stati
        fsm.addTransition(idle, start, specifyPortScreen);
        fsm.addTransition(specifyPortScreen, specifyPortScreen.portSpecified(), waitGameCreation);
        fsm.addTransition(waitGameCreation, waitGameCreation.getCreate(), isNicknameAlreadyExisting);

        // L'evento di start è l'unico che deve essere fatto partire manualmente
        start.fireStateEvent();
    }

    public static void main(String args[]) {
        try {
            new ServerController();
        } catch (InterruptedException | IOException e){}
    }
}
