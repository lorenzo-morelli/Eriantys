package it.polimi.ingsw.client;

import it.polimi.ingsw.client.states.*;
import it.polimi.ingsw.client.view.*;
import it.polimi.ingsw.server.states.Idle;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class ClientController {
    private Model model;      // modello dati di questa semplice demo
    private View view;       // vista (per il momento solo command line interface)

    // Dichiarazione degli stati necessari
    // La maggior parte delle volte uno stato rappresenta una schermata (che sia essa di gui o di cli)
    // ma più in generale potrebbe significare delle azioni da compiere a fronte di eventi
    private final Idle idle;    // modo elegante di far partire il controllore
    private final WelcomeScreen waitStart;
    private final READ askUserinfo;
    private final READ askGAMECODE;
    private final READ askGameInfo;
    private final ConnectToServer connectionToServer;
    private final CreateOrConnectDecision createOrConnect;
    private final CreateGame createGame;
    private final ConnectGame connectGame;
    private final Event start;
    private final WaitForturn wait;
    private final ChooseAssistentCardPhase chooseCard;
    private final MoveStudentPhase moveStudent;
    private final MoveMotherPhase moveMother;
    private final ChooseCloudPhase chooseCloud;
    private final EndGame end;

    public ClientController(View view) throws IOException, InterruptedException {

        CommandPrompt.setDebug();
        this.model = new Model();
        this.view = view;

        idle = new Idle();
        start = new Event("[Controller Started]");
        Controller fsm = new Controller("Controllore del Client", idle);

        // Costruzioni degli stati necessari
        waitStart = new WelcomeScreen(view);
        askUserinfo= new READ(view,model,3);
        askGAMECODE= new READ(view,model,1);
        askGameInfo= new READ(view,model,2);
        connectionToServer = new ConnectToServer(view,model);
        createOrConnect = new CreateOrConnectDecision(view,model);
        createGame = new CreateGame(view, model);
        connectGame = new ConnectGame(view, model);
        wait = new WaitForturn(view, model);
        chooseCard = new ChooseAssistentCardPhase(view,model);
        moveStudent = new MoveStudentPhase();
        moveMother = new MoveMotherPhase();
        chooseCloud = new ChooseCloudPhase();
        end = new EndGame();

        // Dichiarazione delle transizioni tra gli stati
        fsm.addTransition(idle, start, waitStart);

        // Schermata di benvenuto
        fsm.addTransition(waitStart, waitStart.start(), askUserinfo);
        fsm.addTransition(waitStart, waitStart.notStart(), waitStart);

        // Schermata di richiesta di nickname, ip e porta + Trying to connect to server via TCP socket
        fsm.addTransition(askUserinfo, askUserinfo.insertedUserInfo(), connectionToServer);
        fsm.addTransition(askUserinfo, askUserinfo.numberOfParametersIncorrect(), askUserinfo);

        fsm.addTransition(connectionToServer, connectionToServer.Connection_to_server_failed(), connectionToServer);
        fsm.addTransition(connectionToServer, connectionToServer.Connected_to_server(), createOrConnect);

        // Choose if create a new game or connect to an existing one
        fsm.addTransition(createOrConnect, createOrConnect.haSceltoConnetti(), askGAMECODE);
        fsm.addTransition(askGAMECODE, askGAMECODE.insertedUserInfo(), connectGame);
        fsm.addTransition(askGAMECODE, askGAMECODE.numberOfParametersIncorrect(), askGAMECODE);

        fsm.addTransition(createOrConnect, createOrConnect.haSceltoCrea(), askGameInfo);
        fsm.addTransition(askGameInfo, askGameInfo.insertedUserInfo(), createGame);
        fsm.addTransition(askGameInfo, askGameInfo.numberOfParametersIncorrect(), askGAMECODE);

        fsm.addTransition(createOrConnect, createOrConnect.sceltaNonValida(), createOrConnect);

        fsm.addTransition(connectGame, connectGame.Game_Started(), wait);
        fsm.addTransition(connectGame, connectGame.Connection_failed(), connectGame);
        fsm.addTransition(createGame, createGame.Game_Started(), wait);
        fsm.addTransition(createGame, createGame.Creation_failed(), createGame);
        //todo


        fsm.addTransition(wait, wait.go_to_assistantcardphase(), chooseCard);
       // fsm.addTransition(chooseCard, chooseCard.go_to_wait(), wait);

        fsm.addTransition(wait, wait.go_to_studentphase() , moveStudent);
       // fsm.addTransition(MoveStudent , MoveStudent.go_to_wait(), wait);

       // fsm.addTransition(MoveStudent , MoveStudent.go_to_movemotherphase(), moveMother);
       // fsm.addTransition(MoveMother , MoveMother.go_to_wait(), wait);

        fsm.addTransition(wait, wait.go_to_endgame() , end );
        fsm.addTransition(wait, wait.go_to_cloudphase() , chooseCloud);
       // fsm.addTransition(ChooseCloud, ChooseCloudPhase.go_to_wait(), wait);

        // L'evento di start è l'unico che deve essere fatto partire manualmente
        start.fireStateEvent();
    }

}
