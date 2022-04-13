package it.polimi.ingsw.utils.StateMachine;

import it.polimi.ingsw.utils.StateMachine.IEvent;

/**

 * Questa Interfaccia astratta serve a implementare i metodi ("Azioni" di uno stato sul modello)
 * entryAction viene eseguito quando lo stato viene acceduto,
 * exitAction quando si sta per uscire dallo stato e andare nel successivo
 *
 * Se si usa quest'interfaccia bisognerebbe fare l'override di toString,
 * (per il logging della partita)

 **/

public interface IState {
    /**
     * @param cause L'evento che ha causato la transizione in questo stato
     * @return null a meno che tu non voglia forzare la transizione verso un nuovo stato
     */
    public IEvent entryAction(IEvent cause);

    /**
     * Come entryaction, a differenza che questo metodo viene chiamato quando il controllore "sta uscendo" dallo stato
     */
    public void exitAction(IEvent cause);
}
