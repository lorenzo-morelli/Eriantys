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
 * @author Ignazio Neto Dell'Acqua
 * @author Fernando Morea
 */
public class ClientController {

    public ClientController(View view) throws Exception {
        ClientModel clientModel = new ClientModel();

        Idle idle = new Idle();
        Event start = new Event("[Controller Started]");
        Controller fsm = new Controller("Controllore del Client", idle);
        WelcomeScreen waitStart = new WelcomeScreen(view);
        ConnectToServer connectionToServer = new ConnectToServer(view, clientModel);
        AmIFirst amIFirst = new AmIFirst(clientModel, fsm);
        ReadUserInfo askUserinfo = new ReadUserInfo(view, clientModel, fsm);
        ReadGameInfo askGameInfo = new ReadGameInfo(view, clientModel, fsm);
        ReadNickname askNewNickname = new ReadNickname(view, clientModel, fsm);
        Wait wait = new Wait(clientModel, view, fsm);
        SendToServer sendToServer = new SendToServer(clientModel, fsm);
        SendNicknameToServer sendNicknameToServer = new SendNicknameToServer(clientModel, fsm);

        fsm.addTransition(idle, start, waitStart);
        fsm.addTransition(waitStart, waitStart.start(), askUserinfo);
        fsm.addTransition(waitStart, waitStart.notStart(), waitStart);
        fsm.addTransition(askUserinfo, askUserinfo.insertedParameters(), connectionToServer);
        fsm.addTransition(askUserinfo, askUserinfo.numberOfParametersIncorrect(), askUserinfo);
        fsm.addTransition(connectionToServer, connectionToServer.connectionToServerFailed(), askUserinfo);
        fsm.addTransition(connectionToServer, connectionToServer.connectedToServer(), amIFirst);
        fsm.addTransition(amIFirst, amIFirst.yes(), askGameInfo);
        fsm.addTransition(askGameInfo, askGameInfo.insertedParameters(), sendToServer);
        fsm.addTransition(sendToServer, sendToServer.getAck(), wait);
        fsm.addTransition(amIFirst, amIFirst.no(), wait);
        fsm.addTransition(amIFirst, amIFirst.nicknameAlreadyPresent(), askNewNickname);
        fsm.addTransition(askNewNickname, askNewNickname.insertedParameters(), sendNicknameToServer);
        fsm.addTransition(sendNicknameToServer, sendNicknameToServer.nickAlreadyExistent(), askNewNickname);
        fsm.addTransition(sendNicknameToServer, sendNicknameToServer.nickUnique(), wait);
        fsm.addTransition(wait, wait.messaggioGestito(), wait);
        fsm.addTransition(wait,wait.Reset(),askUserinfo);
        start.fireStateEvent();

    }
}
