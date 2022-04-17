package it.polimi.ingsw.server;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.cli.CliView;
import it.polimi.ingsw.utils.commandLine.CommandPrompt;
import it.polimi.ingsw.utils.commons.events.*;
import it.polimi.ingsw.utils.stateMachine.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameController {
    Model model;      // modello dati di questa semplice demo
    View view;       // vista (per il momento solo command line interface)


    // IDLE : stato in cui l'interazione con l'utente non è ancora partita
    private final State IDLE = new State("IDLE");

    // Stato di attesa della parola "ciao" da terminale
    private final State WAIT_ciao = new State("STATO di attesa di \"ciao\""){
        public IEvent entryAction(IEvent cause) throws IOException{
            view.askCiao();
            return null;
        }
    };

    // Stato di attesa del nickname dell'utente
    private final State ASK_nickname = new State("STATO di attesa di \"nickname\""){
        public IEvent entryAction(IEvent cause) throws IOException{
            model.setNickname(view.askNickname());
            return null;
        }
    };

    // Stato di attesa della conferma di corretto inserimento del nickname
    private final State CHECK_NICKNAME = new State("STATO di attesa di \"si/no\""){
        public IEvent entryAction(IEvent cause) throws IOException{
            neSineNo.enable();
            view.askNicknameConfirmation(model.getNickname());
            return null;
        }
        public void exitAction(IEvent cause) throws IOException{
            neSineNo.disable();
        }
    };

    // Stato finale di interazione con l'utente
    private final State YES = new State("STATO finale"){
        public IEvent entryAction(IEvent cause) throws IOException{
            view.showConfirmation(model.getNickname());
            return null;
        }
    };


    // elenco tutti gli eventi che mi serviranno
    private final Event  start;
    private final RecognizedString ciao, si, no;
    private final NotRecognizedString notciao;
    public final InputString nick;
    private final NotRecognizedSetOfStrings neSineNo;


    public GameController(View view) throws IOException, InterruptedException {

        // decommentare se si vogliono vedere le info di debugging
        //CommandPrompt.setDebug();

        this.view = view;
        this.model = new Model();
        Controller e = new Controller("Controllore di test", IDLE);
        start = new StartEvent();
        ciao = new RecognizedString("ciao");
        notciao = new NotRecognizedString("ciao");
        si = new RecognizedString("si");
        no = new RecognizedString("no");
        neSineNo = new NotRecognizedSetOfStrings(new ArrayList<>(Arrays.asList("no", "si")));
        nick = new InputString();

        e.addTransition(IDLE, start, WAIT_ciao);
        e.addTransition(WAIT_ciao, ciao, ASK_nickname);
        e.addTransition(WAIT_ciao, notciao, WAIT_ciao);
        e.addTransition(ASK_nickname, nick, CHECK_NICKNAME);
        e.addTransition(CHECK_NICKNAME, si, YES);
        e.addTransition(CHECK_NICKNAME, no, ASK_nickname);
        e.addTransition(CHECK_NICKNAME, neSineNo, CHECK_NICKNAME);

        // L'evento di start è l'unico che deve essere fatto partire manualmente
        start.fireStateEvent();
    }
    public static void main(String[] args) throws Exception {
        new GameController(new CliView());
    }
}
