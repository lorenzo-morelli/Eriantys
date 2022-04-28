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
    private final CreateOrConnectScreen CreateOrConnect;
    private final CreateGameScreen CreateGame;
    private final ConnectGameScreen ConnectGame;
    private final Event start;
    private final WaitForturn wait;
    private final ChooseAssistentCard chooseCard;
    private final MoveStudentPhase MoveStudent;
    private final MoveMotherPhase MoveMother;
    private final ChooseCloudPhase ChooseCloud;
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
        CreateOrConnect = new CreateOrConnectScreen(view,model);
        CreateGame = new CreateGameScreen(view, model);
        ConnectGame = new ConnectGameScreen(view, model);
        wait = new WaitForturn(view, model);
        chooseCard = new ChooseAssistentCard();
        MoveStudent = new MoveStudentPhase();
        MoveMother = new MoveMotherPhase();
        ChooseCloud = new ChooseCloudPhase();
        end = new EndGame();

        // Dichiarazione delle transizioni tra gli stati
        fsm.addTransition(idle, start, waitStart);

        // Schermata di benvenuto
        fsm.addTransition(waitStart, waitStart.start(), askConnectionInfo);
        fsm.addTransition(waitStart, waitStart.notStart(), waitStart);

        // Schermata di richiesta di nickname, ip e porta
        fsm.addTransition(askConnectionInfo, askConnectionInfo.insertedUserInfo(), connectionToServer);
        fsm.addTransition(askConnectionInfo, askConnectionInfo.numberOfParametersIncorrect(), askConnectionInfo);

        fsm.addTransition(connectionToServer, connectionToServer.connected(), CreateOrConnect);
        fsm.addTransition(connectionToServer, connectionToServer.notConnected(), askConnectionInfo);

        // Scelta tra creazione di una nuova partita o connessione ad una esistente
        fsm.addTransition(CreateOrConnect, CreateOrConnect.haSceltoConnetti(), ConnectGame);
        fsm.addTransition(CreateOrConnect, CreateOrConnect.haSceltoCrea(), CreateGame);
        fsm.addTransition(CreateOrConnect, CreateOrConnect.sceltaNonValida(), CreateOrConnect);

        fsm.addTransition(ConnectGame, ConnectGame.go_to_wait(), wait);
        fsm.addTransition(CreateGame, CreateGame.go_to_wait(), wait);

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


        // L'evento di start è l'unico che deve essere fatto partire manualmente
        start.fireStateEvent();
    }

}
