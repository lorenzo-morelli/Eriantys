package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.states.*;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;

/**
 * The formalism adopted for the finite state machine of the server is that of an event based fsm,
 * very similar to those used in literature in automatic manufacturing systems, where some events trigger actions.
 * In our use case, a state represents a particular possible screen of the user interface, and it is the state
 * itself that contains the actions that must be performed when an event occurs (they will be "entry actions" and
 * "exit actions" ).
 * In this way the server only keeps track of the table that determines the transition
 * (current state x event) -> target state.
 * This table is created with the "addTransition" method.
 */
public class ServerController {
    private Model model;
    private final ConnectionModel connectionModel = new ConnectionModel();
    private static final Idle idle = new Idle();
    private static final Controller fsm = new Controller("Controllore del Server", idle);

    public ServerController() throws Exception {
        fsm.showDebugMessages(); // Todo comment when not needed

        SpecifyPortScreen specifyPortScreen = new SpecifyPortScreen();
        WaitFirstPlayer waitFirstPlayer = new WaitFirstPlayer(this);
        WaitFirstPlayerGameInfo waitFirstPlayerGameInfo = new WaitFirstPlayerGameInfo(this);
        WaitOtherClients waitOtherClients = new WaitOtherClients(this);
        CreateGame createGame = new CreateGame(this);
        AssistantCardPhase assistantCardPhase = new AssistantCardPhase(this);
        StudentPhase studentPhase = new StudentPhase(this);
        AskForTeamMate askForTeamMate = new AskForTeamMate(this);
        MotherPhase motherPhase = new MotherPhase(this);
        CloudPhase cloudPhase = new CloudPhase(this);
        EndTurn endTurn = new EndTurn(this);
        EndGame endGame = new EndGame(this);


        Event start = new Event("[Controller Started]");
        fsm.addTransition(idle, start, specifyPortScreen);
        fsm.addTransition(specifyPortScreen, specifyPortScreen.portSpecified(), waitFirstPlayer);
        fsm.addTransition(waitFirstPlayer, waitFirstPlayer.gotFirstMessage(), waitFirstPlayerGameInfo);
        fsm.addTransition(waitFirstPlayerGameInfo, waitFirstPlayerGameInfo.gotNumOfPlayersAndGamemode(),waitOtherClients);
        fsm.addTransition(waitOtherClients, waitOtherClients.twoOrThreeClientsConnected(), createGame);
        fsm.addTransition(waitOtherClients, waitOtherClients.fourClientsConnected(), askForTeamMate);
        fsm.addTransition(createGame, createGame.gameCreated(), assistantCardPhase);
        fsm.addTransition(createGame, createGame.fourPlayersGameCreated(), askForTeamMate);
        fsm.addTransition(askForTeamMate, askForTeamMate.teamMateChoosen(), assistantCardPhase);
        fsm.addTransition(assistantCardPhase, assistantCardPhase.cardsChoosen(), studentPhase);
        fsm.addTransition(assistantCardPhase, assistantCardPhase.gameEnd(), endGame);
        fsm.addTransition(studentPhase, studentPhase.studentPhaseEnded(), motherPhase);
        fsm.addTransition(studentPhase, studentPhase.gameEnd(), endGame);
        fsm.addTransition(motherPhase, motherPhase.GoToCloudPhase(), cloudPhase);
        fsm.addTransition(motherPhase, motherPhase.goToStudentPhase(), studentPhase);
        fsm.addTransition(motherPhase, motherPhase.gameEnd(), endGame);
        fsm.addTransition(motherPhase, motherPhase.GoToEndTurn(), endTurn);
        fsm.addTransition(cloudPhase, cloudPhase.GoToStudentPhase(), studentPhase);
        fsm.addTransition(cloudPhase, cloudPhase.GoToEndTurn(), endTurn);
        fsm.addTransition(cloudPhase, cloudPhase.gameEnd(), endGame);
        fsm.addTransition(endTurn, endTurn.goToAssistentCardPhase(), assistantCardPhase);
        fsm.addTransition(endGame, endGame.getRestart(), waitFirstPlayer);
        fsm.addTransition(waitFirstPlayerGameInfo, waitFirstPlayerGameInfo.getReset(), waitFirstPlayer);


        // Start event is the only one that has to be triggered manually.
        start.fireStateEvent();
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public static Controller getFsm() {
        return fsm;
    }

    public ConnectionModel getConnectionModel() {
        return connectionModel;
    }
}
