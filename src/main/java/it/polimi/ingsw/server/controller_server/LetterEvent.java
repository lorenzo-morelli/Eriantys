package it.polimi.ingsw.server.controller_server;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextArea;

class LetterEvent extends Event implements KeyListener {

    private final char key;

    public LetterEvent(String key, JTextArea jta) {
        super("Letter Event " + key);
        this.key = key.charAt(0);
        jta.addKeyListener(this);

    }

    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == key) {
            try {
                fireStateEvent();
            } catch (IllegalStateException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}


