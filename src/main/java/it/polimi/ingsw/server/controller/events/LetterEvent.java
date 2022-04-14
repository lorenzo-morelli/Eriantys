package it.polimi.ingsw.server.controller.events;

import it.polimi.ingsw.utils.stateMachine.Event;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.JTextArea;

public class LetterEvent extends Event implements KeyListener {

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
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}


