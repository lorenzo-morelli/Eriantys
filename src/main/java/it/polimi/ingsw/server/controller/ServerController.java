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
 * @author Ignazio Neto Dell'Acqua
 * @author Fernando Morea
 */
public class ServerController {
    private Model model;
    private final ConnectionModel connectionModel = new ConnectionModel();
    private static final Idle idle = new Idle();
    private static final Controller fsm = new Controller("Server Controller", idle);

    /**
     * Main constructor of the Server controller. Controller is FSM machine, created by using pattern State
     */
    public ServerController() throws Exception {
        fsm.showDebugMessages();

        SpecifyPortScreen specifyPortScreen = new SpecifyPortScreen();
        WaitFirstPlayer waitFirstPlayer = new WaitFirstPlayer(this);
        WaitFirstPlayerGameInfo waitFirstPlayerGameInfo = new WaitFirstPlayerGameInfo(this);
        WaitOtherClients waitOtherClients = new WaitOtherClients(this);
        Create2Or3PlayerGame create2Or3PlayerGame = new Create2Or3PlayerGame(this);
        AssistantCardPhase assistantCardPhase = new AssistantCardPhase(this);
        StudentPhase studentPhase = new StudentPhase(this);
        Create4PlayerGame create4PlayerGame = new Create4PlayerGame(this);
        MotherPhase motherPhase = new MotherPhase(this);
        CloudPhase cloudPhase = new CloudPhase(this);
        EndTurn endTurn = new EndTurn(this);
        EndGame endGame = new EndGame(this);


        Event start = new Event("[Controller Started]");
        fsm.addTransition(idle, start, specifyPortScreen);
        fsm.addTransition(specifyPortScreen, specifyPortScreen.portSpecified(), waitFirstPlayer);
        fsm.addTransition(waitFirstPlayer, waitFirstPlayer.gotFirstMessage(), waitFirstPlayerGameInfo);
        fsm.addTransition(waitFirstPlayerGameInfo, waitFirstPlayerGameInfo.gotNumOfPlayersAndGameMode(), waitOtherClients);
        fsm.addTransition(waitOtherClients, waitOtherClients.twoOrThreeClientsConnected(), create2Or3PlayerGame);
        fsm.addTransition(waitOtherClients, waitOtherClients.fourClientsConnected(), create4PlayerGame);
        fsm.addTransition(create2Or3PlayerGame, create2Or3PlayerGame.gameCreated(), assistantCardPhase);
        fsm.addTransition(create2Or3PlayerGame, create2Or3PlayerGame.fourPlayersGameCreated(), create4PlayerGame);
        fsm.addTransition(create4PlayerGame, create4PlayerGame.teamMateChosen(), assistantCardPhase);
        fsm.addTransition(assistantCardPhase, assistantCardPhase.cardsChosen(), studentPhase);
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
        fsm.addTransition(endTurn, endTurn.goToAssistantCardPhase(), assistantCardPhase);
        fsm.addTransition(endGame, endGame.getRestart(), waitFirstPlayer);
        fsm.addTransition(waitFirstPlayerGameInfo, waitFirstPlayerGameInfo.getReset(), waitFirstPlayer);
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
