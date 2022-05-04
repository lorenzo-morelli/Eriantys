package it.polimi.ingsw.client;

import it.polimi.ingsw.client.states.*;
import it.polimi.ingsw.client.view.*;
import it.polimi.ingsw.server.states.Idle;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class ClientController {

    public ClientController(View view) throws IOException, InterruptedException {

        CommandPrompt.setDebug();
        // modello dati di questa semplice demo
        ClientModel clientModel = new ClientModel();
        // vista (per il momento solo command line interface)

        // Dichiarazione degli stati necessari
        // La maggior parte delle volte uno stato rappresenta una schermata (che sia essa di gui o di cli)
        // ma più in generale potrebbe significare delle azioni da compiere a fronte di eventi

        // modo elegante di far partire il controllore
        Idle idle = new Idle();
        Event start = new Event("[Controller Started]");
        Controller fsm = new Controller("Controllore del Client", idle);

        // Costruzioni degli stati necessari

        // La welcomeScreen richiede all'utente una interazione scrivendo (o cliccando il bottone) start
        WelcomeScreen waitStart = new WelcomeScreen(view);

        // Stato di connessione al server
        ConnectToServer connectionToServer = new ConnectToServer(view, clientModel);

        // Chiede al server se è il primo client ad essersi collegato
        AmIFirst amIFirst = new AmIFirst(clientModel,fsm);

        // Reading stuffs from terminal
        ReadUserInfo askUserinfo = new ReadUserInfo(view, clientModel, fsm);
        ReadGameInfo askGameInfo = new ReadGameInfo(view, clientModel, fsm);

        // Wait state, per il momento uno stato stupido while(true){ }
        Wait wait = new Wait();

        // Stati per inviare informazioni al server
        SendToServer sendToServer = new SendToServer(clientModel,fsm);
        SendNicknameToServer sendNicknameToServer = new SendNicknameToServer(clientModel,fsm);

        // Stati di lettura da terminale
        ReadWichCard askCardChoosed = new ReadWichCard(view, clientModel, fsm);
        ReadWichStudent askwitchStudent = new ReadWichStudent(view, clientModel, fsm);
        ReadWichIsland askwitchIsland = new ReadWichIsland(view, clientModel, fsm);
        ReadNickname askNewNickname = new ReadNickname(view,clientModel,fsm);
        ReadWhereToMoveMotherNature askwheremovemother = new ReadWhereToMoveMotherNature(view, clientModel, fsm);
        ReadWichCloud askwitchcloud = new ReadWichCloud(view, clientModel, fsm);

        // Stati di decisione tra due opzioni
        Decision connectOrCreate = new CreateOrConnect(view, clientModel);
        Decision islandOrSchool = new IslandsOrSchool(view, clientModel);

        // Varie ed eventuali
        WaitForStartGame waitstartgame = new WaitForStartGame(view, clientModel, "GAMESTART");
        WaitForTurn waitAssistantCardphase = new WaitForTurn(view, clientModel,"ASSISTANTCARDPHASE");
        WaitForTurn waitStudent_and_Mother_Phase = new WaitForTurn(view, clientModel,"STUDENTPHASE_OR_MOVEMOTHERPHASE");
        WaitForTurn waitCloudPhase = new WaitForTurn(view, clientModel,"CLOUDPHASE");
        EndGame end = new EndGame(view, clientModel);

        // Dichiarazione delle transizioni tra gli stati
        fsm.addTransition(idle, start, waitStart);

        // Schermata di benvenuto
        fsm.addTransition(waitStart, waitStart.start(), askUserinfo);
        fsm.addTransition(waitStart, waitStart.notStart(), waitStart);

        // Schermata di richiesta di nickname, ip e porta + Trying to connect to server via TCP socket
        fsm.addTransition(askUserinfo, askUserinfo.insertedParameters(), connectionToServer);
        fsm.addTransition(askUserinfo, askUserinfo.numberOfParametersIncorrect(), askUserinfo);

        // Tentativo di connessione al server sull' IP/Porta specificata dall'utente
        fsm.addTransition(connectionToServer, connectionToServer.connectionToServerFailed(), askUserinfo);
        fsm.addTransition(connectionToServer, connectionToServer.connectedToServer(), amIFirst);

        // Se sono il primo client allora devo scegliere il gamemode e il numero dei partecipanti ed inviare al server
        fsm.addTransition(amIFirst, amIFirst.yes(), askGameInfo);
        fsm.addTransition(askGameInfo,askGameInfo.insertedParameters(),sendToServer);
        fsm.addTransition(sendToServer, sendToServer.getAck(),wait);

        // Altrimenti se non sono il primo client vado direttamente nello stato di attesa comandi
        fsm.addTransition(amIFirst, amIFirst.no(), wait);

        // Altrimenti il server mi intima di scegliere un nuovo nickname
        fsm.addTransition(amIFirst,amIFirst.nicknameAlreadyPresent(), askNewNickname);
        fsm.addTransition(askNewNickname,askNewNickname.insertedParameters(),sendNicknameToServer);
        fsm.addTransition(sendNicknameToServer, sendNicknameToServer.nickAlreadyExistent(), askNewNickname);
        fsm.addTransition(sendNicknameToServer,sendNicknameToServer.getAck(),wait);

        // L'evento di start è l'unico che deve essere fatto partire manualmente
        start.fireStateEvent();
    }

}
