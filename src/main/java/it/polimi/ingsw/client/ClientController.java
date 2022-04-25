package it.polimi.ingsw.client;

import it.polimi.ingsw.client.states.*;
import it.polimi.ingsw.client.view.*;
import it.polimi.ingsw.utils.cli.CommandPrompt;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;

public class ClientController {
    private Model model;      // modello dati di questa semplice demo
    private View view;       // vista (per il momento solo command line interface)

    // Dichiarazione degli stati necessari
    // La maggior parte delle volte uno stato rappresenta una schermata (che sia essa di gui o di cli)
    // ma più in generale potrebbe significare delle azioni da compiere a fronte di eventi
    // ogni stato avrà un getter che sarà utilizzato dalla GuiView
    private final Idle idle;    // modo elegante di far partire il controllore
    private final WelcomeScreen waitStart;
    private final AskNicknameScreen askNicknameScreen;
    /*private final CheckNicknameScreen checkNicknameScreen;
    private final FinalScreen finalScreen;*/

    // unico evento che deve avere il controllore per partire
    private final Event start;

    public ClientController(View view) throws IOException, InterruptedException {
        CommandPrompt.setDebug();
        this.model = new Model();
        this.view = view;

        idle = new Idle();
        start = new Event("[Controller Started]");
        Controller fsm = new Controller("Controllore del Client", idle);

        waitStart = new WelcomeScreen(view);
        askNicknameScreen = new AskNicknameScreen(view,model);
        checkNicknameScreen = new CheckNicknameScreen(view,model);
        finalScreen = new FinalScreen(view, model);

        fsm.addTransition(idle, start, waitStart);
        fsm.addTransition(waitStart, waitStart.start(), askNicknameScreen);
        fsm.addTransition(waitStart, waitStart.notStart(), waitStart);
        fsm.addTransition(askNicknameScreen, askNicknameScreen.nickname(), checkNicknameScreen);
        fsm.addTransition(checkNicknameScreen, checkNicknameScreen.si(), finalScreen);
        fsm.addTransition(checkNicknameScreen, checkNicknameScreen.no(), askNicknameScreen);
        fsm.addTransition(checkNicknameScreen, checkNicknameScreen.neSineNo(), checkNicknameScreen);

        // L'evento di start è l'unico che deve essere fatto partire manualmente
        start.fireStateEvent();
    }

}
