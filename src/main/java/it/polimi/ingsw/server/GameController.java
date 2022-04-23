package it.polimi.ingsw.server;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.cli.CliView;
import it.polimi.ingsw.server.states.*;
import it.polimi.ingsw.utils.commons.events.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class GameController {
    Model model;      // modello dati di questa semplice demo
    View view;       // vista (per il momento solo command line interface)


    // IDLE : stato in cui l'interazione con l'utente non è ancora partita
    private final Idle idle;
    private final RiconosciCiao waitCiao;
    private final AskNickname askNickname;
    private final CheckNickname checkNickname;
    private final Final finalState;



    // elenco tutti gli eventi che mi serviranno
    private final Event  start;


    public GameController(View view) throws IOException, InterruptedException {

        // decommentare se si vogliono vedere le info di debugging
        // CommandPrompt.setDebug();

        this.model = new Model();
        this.view = view;


        idle = new Idle();
        start = new StartEvent();
        Controller e = new Controller("Controllore di test", idle);

        waitCiao = new RiconosciCiao(view);
        askNickname = new AskNickname(view,model);
        checkNickname = new CheckNickname(view,model);
        finalState = new Final(view, model);



        e.addTransition(idle, start, waitCiao);
        e.addTransition(waitCiao, waitCiao.insertedCiao(), askNickname);
        e.addTransition(waitCiao, waitCiao.notInsertedCiao(), waitCiao);
        e.addTransition(askNickname, askNickname.insertedNickname(), checkNickname);
        e.addTransition(checkNickname, checkNickname.insertedSi(), finalState);
        e.addTransition(checkNickname, checkNickname.insertedNo(), askNickname);
        e.addTransition(checkNickname, checkNickname.insertedNeSineNo(), checkNickname);

        // L'evento di start è l'unico che deve essere fatto partire manualmente
        start.fireStateEvent();
    }
    public static void main(String[] args) throws Exception {
        new GameController(new CliView());
    }
}
