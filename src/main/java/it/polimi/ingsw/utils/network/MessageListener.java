package it.polimi.ingsw.utils.network;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Utilities;

/**
 * I messaggi inviati tramite TCPsocket vengono ricevuti sulla CLI, in particolare su una
 * TextArea textReceived. La seguente classe si mette in ascolto di questa zona di testo dell'
 * interfaccia grafica dove vengono ricevuti i nuovi in messaggio, e se vengono effettuate modifiche,
 * in particolare in caso di arrivo di nuovi messaggi, controlla che il nuovo messaggio ricevuto sia
 * uguale a quello dell'evento e "lancia" l'evento con event.fireStateEvent();
 *
 * @author Fernando
 */
public class MessageListener implements DocumentListener {

    private static JTextArea ta;
    private String message;
    private MessageEvent event;

    public MessageListener(String message, JTextArea ta, MessageEvent event) {
        this.ta=ta;
        this.message=message;
        this.event = event;
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
            int start = Utilities.getWordStart(ta, ta.getCaretPosition());
            int end = Utilities.getWordEnd(ta, ta.getCaretPosition());
            String text = ta.getDocument().getText(start, end - start);
            System.out.println("Expected to trigger event: "+this.message);
            if (this.message.equals( text)){
                System.out.println("Ricevuto: "+text);
                event.fireStateEvent();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
