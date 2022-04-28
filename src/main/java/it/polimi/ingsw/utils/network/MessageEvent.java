package it.polimi.ingsw.utils.network;

import it.polimi.ingsw.utils.stateMachine.Event;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * I messaggi inviati tramite TCPsocket vengono ricevuti sulla CLI, in particolare su una
 * TextArea textReceived. La seguente classe si mette in ascolto di questa zona di testo dell'
 * interfaccia grafica dove vengono ricevuti i nuovi in messaggio, e se vengono effettuate modifiche,
 * in particolare in caso di arrivo di nuovi messaggi, controlla che il nuovo messaggio ricevuto sia
 * uguale a quello dell'evento e "lancia" l'evento con event.fireStateEvent();
 *
 * @author Fernando
 */
public class MessageEvent extends Event implements DocumentListener {

    private static JTextArea ta;
    private String toListen;

    private boolean messageReceived = false;

    public MessageEvent(String toListen) {
        super("[Messaggio == "+ toListen);
        this.ta = Network.checkNewMessages();
        this.toListen = toListen;
        ta.getDocument().addDocumentListener(this);
    }

    public boolean messageReceived() {
        return messageReceived;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
      checkLastWord();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      //checkLastWord();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
       //checkLastWord();
    }

    /**
     * Metodo che controlla se l'ultima stringa inviata dal client e' uguale a quella che ci aspettavamo
     * di trovare per lanciare il nostro evento
     */
    private void checkLastWord() {
        try {
            String text = ta.getText();
            if (this.toListen.equals( text)){
                System.out.println("Ricevuto: "+ text);
                this.messageReceived = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}