package it.polimi.ingsw.utils.network;

import it.polimi.ingsw.utils.StateMachine.Event;

import javax.swing.*;
/**
 * Un evento di tipo messaggio non e' altro che un messaggio (Stringa) arrivato attraverso socket TCP
 * il cui contenuto corrisponde esattamente a quello di un messaggio da noi definito (di cui il significato e'
 * a priori chiaro). Per implementare cio' occorre:
 * a) Il posto dove vengono ricevuti i messaggi: JTextArea textReceived
 * b) Qualcuno che monitori se ci sono messaggi in arrivo: MyDocumentListener listener
 * c) Qualcuno che controlli che il contenuto del messaggio arrivato e' esattamente quello che ci aspettavamo
 * per far triggherare l'evento: sempre il listener MyDocumentListener
 *
 * @author Fernando
 */

class MessageEvent extends Event {

    private final String message;
    private JTextArea textReceived;
    private MessageListener listener;

    public MessageEvent(String message, JTextArea textReceived) {
        super("Message Event " + message);
        this.message = message;
        this.textReceived=textReceived;
        listener = new MessageListener(message,textReceived,this);
        textReceived.getDocument().addDocumentListener(listener);
    }
}
