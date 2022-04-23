package it.polimi.ingsw.client;

import it.polimi.ingsw.client.states.*;
import it.polimi.ingsw.client.view.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class ClientController {
    Model model;      // modello dati di questa semplice demo
    View view;       // vista (per il momento solo command line interface)


    // IDLE : stato in cui l'interazione con l'utente non è ancora partita
    private final Idle idle;
    private final RiconosciCiao waitCiao;
    private final AskNickname askNickname;
    private final CheckNickname checkNickname;
    private final Final finalState;



    // unico evento che deve avere il controllore per partire
    private final Event  start;


    public ClientController(View view) throws IOException, InterruptedException {

        // decommentare se si vogliono vedere le info di debugging
        // CommandPrompt.setDebug();

        this.model = new Model();
        this.view = view;

        idle = new Idle();
        start = new Event("Start");
        Controller fsm = new Controller("Controllore di test", idle);

        waitCiao = new RiconosciCiao(view);
        askNickname = new AskNickname(view,model);
        checkNickname = new CheckNickname(view,model);
        finalState = new Final(view, model);


        fsm.addTransition(idle, start, waitCiao);
        fsm.addTransition(waitCiao, waitCiao.insertedCiao(), askNickname);
        fsm.addTransition(waitCiao, waitCiao.notInsertedCiao(), waitCiao);
        fsm.addTransition(askNickname, askNickname.insertedNickname(), checkNickname);
        fsm.addTransition(checkNickname, checkNickname.insertedSi(), finalState);
        fsm.addTransition(checkNickname, checkNickname.insertedNo(), askNickname);
        fsm.addTransition(checkNickname, checkNickname.insertedNeSineNo(), checkNickname);

        // L'evento di start è l'unico che deve essere fatto partire manualmente
        start.fireStateEvent();
    }
}
