package it.polimi.ingsw.client.view.cli.cliController;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.*;
import it.polimi.ingsw.client.view.cli.cliController.states.*;
import it.polimi.ingsw.server.controller.states.Idle;
import it.polimi.ingsw.utils.stateMachine.*;
/**
 * The formalism adopted for the finite state machine of the client is that of an event based fsm,
 * very similar to those used in literature in automatic manufacturing systems, where some events trigger actions.
 * In our use case, a state represents a particular possible screen of the user interface, and it is the state
 * itself that contains the actions that must be performed when an event occurs (they will be "entry actions" and
 * "exit actions" ).
 * In this way the server only keeps track of the table that determines the transition
 * (current state x event) -> target state.
 * This table is created with the "addTransition" method.
 */
public class ClientController {

    public ClientController(View view) throws Exception {
        // Client data (can be sent via network)
        ClientModel clientModel = new ClientModel();

        // Declaration of necessary states: a state represents a screen (whether it is a gui or a cli)
        // but more generally it could mean actions to be taken in the face of events
        Idle idle = new Idle();     // elegant way of starting the controller
        Event start = new Event("[Controller Started]");
        Controller fsm = new Controller("Controllore del Client", idle);
        //fsm.showDebugMessages();

        // The welcomeScreen asks the user for an interaction by writing (or clicking the button) start
        WelcomeScreen waitStart = new WelcomeScreen(view);
        // State of connection to the server
        ConnectToServer connectionToServer = new ConnectToServer(view, clientModel);
        AmIFirst amIFirst = new AmIFirst(clientModel, fsm);
        // Reading stuffs from terminal
        ReadUserInfo askUserinfo = new ReadUserInfo(view, clientModel, fsm);
        ReadGameInfo askGameInfo = new ReadGameInfo(view, clientModel, fsm);
        ReadNickname askNewNickname = new ReadNickname(view, clientModel, fsm);
        // Wait state, state in which I only receive view update commands
        Wait wait = new Wait(clientModel, view, fsm);
        // States to send information to the server
        SendToServer sendToServer = new SendToServer(clientModel, fsm);
        SendNicknameToServer sendNicknameToServer = new SendNicknameToServer(clientModel, fsm);

        // Declaration of the transitions between states
        fsm.addTransition(idle, start, waitStart);
        // Welcome screen
        fsm.addTransition(waitStart, waitStart.start(), askUserinfo);
        fsm.addTransition(waitStart, waitStart.notStart(), waitStart);
        // Screen for requesting nickname, ip and port + Trying to connect to server via TCP socket
        fsm.addTransition(askUserinfo, askUserinfo.insertedParameters(), connectionToServer);
        fsm.addTransition(askUserinfo, askUserinfo.numberOfParametersIncorrect(), askUserinfo);
        // Attempt to connect to the server on the IP / Port specified by the user
        fsm.addTransition(connectionToServer, connectionToServer.connectionToServerFailed(), askUserinfo);
        fsm.addTransition(connectionToServer, connectionToServer.connectedToServer(), amIFirst);
        // If I am the first client then I have to choose the game mode and the number of participants and send to the server
        fsm.addTransition(amIFirst, amIFirst.yes(), askGameInfo);
        fsm.addTransition(askGameInfo, askGameInfo.insertedParameters(), sendToServer);
        fsm.addTransition(sendToServer, sendToServer.getAck(), wait);
        // Otherwise, if I am not the first client, I go directly to the command waiting state
        fsm.addTransition(amIFirst, amIFirst.no(), wait);
        // Otherwise, the server asks me to choose a new nickname
        fsm.addTransition(amIFirst, amIFirst.nicknameAlreadyPresent(), askNewNickname);
        fsm.addTransition(askNewNickname, askNewNickname.insertedParameters(), sendNicknameToServer);
        fsm.addTransition(sendNicknameToServer, sendNicknameToServer.nickAlreadyExistent(), askNewNickname);
        fsm.addTransition(sendNicknameToServer, sendNicknameToServer.nickUnique(), wait);
        fsm.addTransition(wait, wait.messaggioGestito(), wait);
        fsm.addTransition(wait,wait.Reset(),askUserinfo);
        // The start event is the only one that must be started manually
        start.fireStateEvent();

    }
}
