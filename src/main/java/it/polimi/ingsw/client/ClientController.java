package it.polimi.ingsw.client;

import it.polimi.ingsw.client.states.*;
import it.polimi.ingsw.client.view.*;
import it.polimi.ingsw.server.states.Idle;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.network.Network;
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
    private final AskConnectionInfoScreen askConnectionInfo;
    private final ConnectionToServer connectionToServer;
    private final CreateOrConnectScreen createOrConnect;
    private final CreateGameScreen createGame;
    private final ConnectGameScreen connectGame;
    private final Event start;
    private final WaitForturn wait;
    private final ChooseAssistentCard chooseCard;
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
        askConnectionInfo = new AskConnectionInfoScreen(view,model);
        connectionToServer = new ConnectionToServer(view, model);
        createOrConnect = new CreateOrConnectScreen(view,model);
        createGame = new CreateGameScreen(view, model);
        connectGame = new ConnectGameScreen(view, model);
        wait = new WaitForturn(view, model);
        chooseCard = new ChooseAssistentCard();
        moveStudent = new MoveStudentPhase();
        moveMother = new MoveMotherPhase();
        chooseCloud = new ChooseCloudPhase();
        end = new EndGame();

        // Dichiarazione delle transizioni tra gli stati
        fsm.addTransition(idle, start, waitStart);

        // Schermata di benvenuto
        fsm.addTransition(waitStart, waitStart.start(), askConnectionInfo);
        fsm.addTransition(waitStart, waitStart.notStart(), waitStart);

        // Schermata di richiesta di nickname, ip e porta
        fsm.addTransition(askConnectionInfo, askConnectionInfo.insertedUserInfo(), connectionToServer);
        fsm.addTransition(askConnectionInfo, askConnectionInfo.numberOfParametersIncorrect(), askConnectionInfo);

        // Trying to connect to server via TCP socket
        fsm.addTransition(connectionToServer, connectionToServer.connected(), createOrConnect);
        fsm.addTransition(connectionToServer, connectionToServer.notConnected(), askConnectionInfo);

        // Choose if create a new game or connect to an existing one
        fsm.addTransition(createOrConnect, createOrConnect.haSceltoConnetti(), connectGame);
        fsm.addTransition(createOrConnect, createOrConnect.haSceltoCrea(), createGame);
        fsm.addTransition(createOrConnect, createOrConnect.sceltaNonValida(), createOrConnect);

        fsm.addTransition(connectGame, connectGame.creationSuccessful(), wait);
        /*
        fsm.addTransition(createGame, createGame.go_to_wait(), wait);

        //todo


        fsm.addTransition(wait, wait.go_to_assistantcardphase(), chooseCard);
       // fsm.addTransition(chooseCard, chooseCard.go_to_wait(), wait);

        fsm.addTransition(wait, wait.go_to_studentphase() , MoveStudent);
       // fsm.addTransition(MoveStudent , MoveStudent.go_to_wait(), wait);

       // fsm.addTransition(MoveStudent , MoveStudent.go_to_movemotherphase(), MoveMother);
       // fsm.addTransition(MoveMother , MoveMother.go_to_wait(), wait);

        fsm.addTransition(wait, wait.go_to_endgame() , end );
        fsm.addTransition(wait, wait.go_to_cloudphase() , ChooseCloud);
       // fsm.addTransition(ChooseCloud, ChooseCloudPhase.go_to_wait(), wait);
    */

        // L'evento di start è l'unico che deve essere fatto partire manualmente
        start.fireStateEvent();
    }

}
