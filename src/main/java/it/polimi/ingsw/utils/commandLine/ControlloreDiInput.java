package it.polimi.ingsw.utils.commandLine;

import it.polimi.ingsw.utils.commons.events.StartEvent;
import it.polimi.ingsw.utils.commons.events.TerminalEvent;
import it.polimi.ingsw.utils.stateMachine.*;

import java.io.IOException;

public class ControlloreDiInput {
    /**
     * Elenco tutti gli stati che mi serviranno
     */

    private final State IDLE = new State("IDLE");
    private final State WAIT_ciao = new State("STATO di attesa di \"ciao\""){
        @Override
        /**
         * Azione di ingresso nello stato WAIT_ciao
         * Creo un nuovo Thread di richiesta di input da terminale e che l'utente scriva cioo
         */
        public IEvent entryAction(IEvent cause) throws IOException{
            InteractionThread richiestaDiInput = new InteractionThread("SUGGERIMENTO: Scrivi ciao e clicca invio");
            richiestaDiInput.start();
            return null;
        }
    };
    private final State END = new State("END");

    /**
     * elenco tutti gli eventi che mi serviranno
     */
    private final Event EVENT_ciao, EVENT_start;


    public ControlloreDiInput() throws IOException, InterruptedException {

        EVENT_ciao= new TerminalEvent("ciao");
        EVENT_start=new StartEvent();

        Controller e = new Controller("Controllore di test", IDLE);

        e.addTransition(IDLE, EVENT_start, WAIT_ciao);
        e.addTransition(WAIT_ciao, EVENT_ciao, END);

        /**
         * L'evento di start è l'unico che deve essere fatto partire manualmente
         */
        EVENT_start.fireStateEvent();


    }
}
