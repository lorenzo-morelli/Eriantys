package it.polimi.ingsw.client;

import it.polimi.ingsw.client.states.*;
import it.polimi.ingsw.client.view.*;
import it.polimi.ingsw.server.states.Idle;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;
import it.polimi.ingsw.client.background_activites.*;

public class ClientController {
    private Model model;      // modello dati di questa semplice demo
    private View view;       // vista (per il momento solo command line interface)

    // Dichiarazione degli stati necessari
    // La maggior parte delle volte uno stato rappresenta una schermata (che sia essa di gui o di cli)
    // ma più in generale potrebbe significare delle azioni da compiere a fronte di eventi
    private final Idle idle;    // modo elegante di far partire il controllore
    private final WelcomeScreen waitStart;
    private final Read_from_terminal askUserinfo;
    private final Read_from_terminal askGAMECODE;
    private final Read_from_terminal askGameInfo;
    private final Read_from_terminal askCardChoosed;
    private final Read_from_terminal askwitchStudent;
    private final Read_from_terminal askwitchIsland;
    private final Decision islandOrSchool;
    private final ConnectToServer connectionToServer;
    private final Send_to_Server sendGameInfo;
    private final Send_to_Server sendCardChoosed;
    private final Send_to_Server sendStudent_toSchool;
    private final Send_to_Server sendStudent_toIsland;
    private final Decision createOrConnect;
    private final ConnectGame connectGame;
    private final Event start;
    private final WaitForTurn wait;
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
        askUserinfo= new Read_from_terminal(view,model,3,"USERINFO");
        askGAMECODE= new Read_from_terminal(view,model,1,"GAMECODE");
        askGameInfo= new Read_from_terminal(view,model,2,"GAMEINFO");
        askCardChoosed =new Read_from_terminal(view,model,1,"WICHCARD");
        askwitchStudent=new Read_from_terminal(view,model,1,"WICHSTUDENT");
        askwitchIsland=new Read_from_terminal(view,model,1,"WICHISLAND");
        connectionToServer = new ConnectToServer(view,model);
        createOrConnect = new Decision(view,model,"CREATEORCONNECT");
        islandOrSchool=new Decision(view,model,"ISLANDORSCHOOL");
        sendGameInfo = new Send_to_Server(view, model,"CREATIONPARAMETERS");
        sendCardChoosed = new Send_to_Server(view, model, "CARDCHOOSED");
        sendStudent_toSchool= new Send_to_Server(view,model,"STUDENT_TOSCHOOL");
        sendStudent_toIsland= new Send_to_Server(view,model,"STUDENT_TOISLAND");
        connectGame = new ConnectGame(view, model);
        wait = new WaitForTurn(view, model);
        end = new EndGame();

        // Dichiarazione delle transizioni tra gli stati
        fsm.addTransition(idle, start, waitStart);

        // Schermata di benvenuto
        fsm.addTransition(waitStart, waitStart.start(), askUserinfo);
        fsm.addTransition(waitStart, waitStart.notStart(), waitStart);

        // Schermata di richiesta di nickname, ip e porta + Trying to connect to server via TCP socket
        fsm.addTransition(askUserinfo, askUserinfo.insertedParameters(), connectionToServer);
        fsm.addTransition(askUserinfo, askUserinfo.numberOfParametersIncorrect(), askUserinfo);

        fsm.addTransition(connectionToServer, connectionToServer.Connection_to_server_failed(), connectionToServer);
        fsm.addTransition(connectionToServer, connectionToServer.Connected_to_server(), createOrConnect);

        // Choose if create a new game or connect to an existing one
        fsm.addTransition(createOrConnect, createOrConnect.haScelto1(), askGAMECODE);
        fsm.addTransition(askGAMECODE, askGAMECODE.insertedParameters(), connectGame);
        fsm.addTransition(askGAMECODE, askGAMECODE.numberOfParametersIncorrect(), askGAMECODE);
        fsm.addTransition(connectGame, connectGame.Game_Started(), wait);
        fsm.addTransition(connectGame, connectGame.Connection_failed(), connectGame);

        fsm.addTransition(createOrConnect, createOrConnect.haScelto2(), askGameInfo);
        fsm.addTransition(askGameInfo, askGameInfo.insertedParameters(), sendGameInfo);
        fsm.addTransition(askGameInfo, askGameInfo.numberOfParametersIncorrect(), askGameInfo);
        fsm.addTransition(sendGameInfo, sendGameInfo.Recevied_ack(), connectGame);
        fsm.addTransition(sendGameInfo, sendGameInfo.send_failed(), sendGameInfo);

        fsm.addTransition(createOrConnect, createOrConnect.sceltaNonValida(), createOrConnect);

        //GAME
        Thread background_thread= new Thread(new receive_view_from_server(this.view));
        background_thread.start();


        fsm.addTransition(wait, wait.go_to_assistantcardphase(), askCardChoosed);
        fsm.addTransition(askCardChoosed, askCardChoosed.insertedParameters(), sendCardChoosed);
        fsm.addTransition(askCardChoosed, askCardChoosed.numberOfParametersIncorrect(), askCardChoosed);
        fsm.addTransition(sendCardChoosed, sendCardChoosed.Recevied_ack(), wait);
        fsm.addTransition(sendCardChoosed, sendCardChoosed.send_failed(), sendCardChoosed);

        fsm.addTransition(wait, wait.go_to_studentphase() , askwitchStudent);
        fsm.addTransition(askwitchStudent , askwitchStudent.insertedParameters(), islandOrSchool);
        fsm.addTransition(askwitchStudent, askwitchStudent.numberOfParametersIncorrect(), askwitchStudent);
        fsm.addTransition(islandOrSchool, islandOrSchool.haScelto1(), askwitchIsland);
        fsm.addTransition(islandOrSchool, islandOrSchool.haScelto2(), sendStudent_toSchool);
        fsm.addTransition(islandOrSchool, islandOrSchool.sceltaNonValida(), islandOrSchool);
        fsm.addTransition(askwitchIsland , askwitchIsland.insertedParameters(), sendStudent_toIsland);
        fsm.addTransition(askwitchIsland, askwitchIsland.numberOfParametersIncorrect(), askwitchIsland);

        fsm.addTransition(sendStudent_toSchool, sendStudent_toSchool.Recevied_ack(), wait);
        fsm.addTransition(sendStudent_toSchool, sendStudent_toSchool.send_failed(), sendStudent_toSchool);
        fsm.addTransition(sendStudent_toIsland, sendStudent_toIsland.Recevied_ack(), wait);
        fsm.addTransition(sendStudent_toIsland, sendStudent_toIsland.send_failed(), sendStudent_toIsland);

        //fsm.addTransition(wait, wait.go_to_endgame() , end );
        //fsm.addTransition(wait, wait.go_to_cloudphase() , chooseCloud);
        // fsm.addTransition(ChooseCloud, ChooseCloudPhase.go_to_wait(), wait);

        // L'evento di start è l'unico che deve essere fatto partire manualmente
        start.fireStateEvent();
    }

}
