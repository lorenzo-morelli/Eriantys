package it.polimi.ingsw.utils.commandLine;

import it.polimi.ingsw.utils.commons.events.DetectString;
import it.polimi.ingsw.utils.commons.events.StartEvent;
import it.polimi.ingsw.utils.commons.events.RecognizeString;
import it.polimi.ingsw.utils.stateMachine.*;

import java.io.IOException;

public class ControlloreDiInput {
    private final State IDLE = new State("IDLE");
    private final State WAIT_ciao = new State("STATO di attesa di \"ciao\""){
        public IEvent entryAction(IEvent cause) throws IOException{
            CommandPrompt.ask("Scrivi ciao","SALUTAMI> ");
            return null;
        }
    };
    private final State ASK_nickname = new State("STATO di attesa di \"nickname\""){
        public IEvent entryAction(IEvent cause) throws IOException{
            CommandPrompt.ask("Scrivi il tuo nickname","NICKNAME> ");
            return null;
        }
    };
    private final State CHECK_NICKNAME = new State("STATO di attesa di \"si/no\""){
        public IEvent entryAction(IEvent cause) throws IOException{
            CommandPrompt.ask("Il tuo nickname è proprio " + CommandPrompt.gotFromTerminal()+"?","si/no> ");
            return null;
        }
    };
    private final State YES = new State("STATO finale"){
        public IEvent entryAction(IEvent cause) throws IOException{
            CommandPrompt.println("Bene, allora ti chiamerò "+CommandPrompt.gotFromTerminal());
            return null;
        }
    };


    /**
     * elenco tutti gli eventi che mi serviranno
     */
    private final Event  EVENT_start,EVENT_ciao,EVENT_nickname,EVENT_yes;


    public ControlloreDiInput() throws IOException, InterruptedException {

        Controller e = new Controller("Controllore di test", IDLE);
        EVENT_start=new StartEvent();
        EVENT_ciao= new RecognizeString("ciao");
        EVENT_nickname = new DetectString();
        EVENT_yes=new RecognizeString("si");

        e.addTransition(IDLE, EVENT_start, WAIT_ciao);
        e.addTransition(WAIT_ciao, EVENT_ciao, ASK_nickname);
        e.addTransition(ASK_nickname, EVENT_nickname, CHECK_NICKNAME);
        e.addTransition(CHECK_NICKNAME, EVENT_yes, YES);

        /**
         * L'evento di start è l'unico che deve essere fatto partire manualmente
         */
        EVENT_start.fireStateEvent();
    }
}
