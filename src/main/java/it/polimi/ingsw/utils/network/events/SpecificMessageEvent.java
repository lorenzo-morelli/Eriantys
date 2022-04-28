package it.polimi.ingsw.utils.network.events;

import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.Event;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Se vengono effettuate modifiche,
 * in particolare in caso di arrivo di nuovi messaggi, controlla che il nuovo messaggio ricevuto sia
 * uguale a quello dell'evento e "lancia" l'evento con event.fireStateEvent();
 *
 * @author Fernando
 */
public class SpecificMessageEvent extends Event implements DocumentListener {

    private static JTextArea ta;
    private String toListen;

    private boolean messageReceived = false;

    public SpecificMessageEvent(String toListen) {
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
      checkLastMessage();
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
    private void checkLastMessage() {
        try {
            String text = ta.getText();
            if (this.toListen.equals( text)){
                System.out.println("[Ricevuto: "+ text +"]");
                this.messageReceived = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}