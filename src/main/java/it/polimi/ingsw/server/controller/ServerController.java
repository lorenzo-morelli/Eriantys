package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.states.*;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;

public class ServerController {
    Model model;
    ConnectionModel connectionModel = new ConnectionModel();
    Idle idle = new Idle();
    Event start = new Event("[Controller Started]");
    Controller fsm = new Controller("Controllore del Server", idle);


    public ServerController() throws Exception {
        fsm.showDebugMessages();

        //Costruzione degli stati necessari
        SpecifyPortScreen specifyPortScreen = new SpecifyPortScreen();
        WaitFirstPlayer waitFirstPlayer = new WaitFirstPlayer(this);
        WaitFirstPlayerGameInfo waitFirstPlayerGameInfo = new WaitFirstPlayerGameInfo(this);
        WaitOtherClients waitOtherClients = new WaitOtherClients(this);
        CreateGame createGame = new CreateGame(this);
        AssistantCardPhase assistantCardPhase = new AssistantCardPhase(this);
        StudentPhase studentPhase = new StudentPhase(this);
        AskForTeamMate askForTeamMate = new AskForTeamMate(this);
        MotherPhase motherPhase=new MotherPhase(this);
        CloudPhase cloudPhase=new CloudPhase(this);
        EndTurn endTurn=new EndTurn(this);
        EndGame endGame=new EndGame(this);

        // Dichiarazione delle transizioni tra gli stati
        fsm.addTransition(idle, start, specifyPortScreen);
        fsm.addTransition(specifyPortScreen, specifyPortScreen.portSpecified(), waitFirstPlayer);
        fsm.addTransition(waitFirstPlayer, waitFirstPlayer.gotFirstMessage(), waitFirstPlayerGameInfo);
        fsm.addTransition(waitFirstPlayerGameInfo, waitFirstPlayerGameInfo.gotNumOfPlayersAndGamemode(), waitOtherClients);
        fsm.addTransition(waitOtherClients, waitOtherClients.twoOrThreeClientsConnected(), createGame);
        fsm.addTransition(waitOtherClients,waitOtherClients.fourClientsConnected(),askForTeamMate);
        fsm.addTransition(createGame, createGame.gameCreated(), assistantCardPhase);
        fsm.addTransition(createGame, createGame.fourPlayersGameCreated(),askForTeamMate);
        fsm.addTransition(askForTeamMate, askForTeamMate.teamMateChoosen(), assistantCardPhase);
        fsm.addTransition(assistantCardPhase, assistantCardPhase.cardsChoosen(), studentPhase);
        fsm.addTransition(studentPhase, studentPhase.studentPhaseEnded(), motherPhase);
        fsm.addTransition(motherPhase, motherPhase.GoToCloudPhase(), cloudPhase);
        fsm.addTransition(motherPhase, motherPhase.goToStudentPhase(), studentPhase);
        fsm.addTransition(motherPhase, motherPhase.gameEnd(),endGame );
        fsm.addTransition(cloudPhase, cloudPhase.GoToStudentPhase(), studentPhase);
        fsm.addTransition(cloudPhase, cloudPhase.GoToEndTurn(),endTurn );
        fsm.addTransition(endTurn,endTurn.goToAssistentCardPhase(),assistantCardPhase);
        fsm.addTransition(endGame,endGame.getRestart(),waitFirstPlayer);

        // L'evento di start è l'unico che deve essere fatto partire manualmente
        start.fireStateEvent();
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Controller getFsm() {
        return fsm;
    }

    public ConnectionModel getConnectionModel() {
        return connectionModel;
    }

}
