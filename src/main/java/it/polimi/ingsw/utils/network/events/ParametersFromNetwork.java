package it.polimi.ingsw.utils.network.events;

import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.other.DoubleObject;
import it.polimi.ingsw.utils.stateMachine.Event;
import javafx.application.Platform;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.Arrays;

public class ParametersFromNetwork extends Event implements DocumentListener {
    private static JTextArea ta;
    private final int numberOfStrings;
    private ArrayList<String> parsedStrings;
    private boolean parametersReceived = false;
    public static final Object PAUSE_KEY = new Object();

    private boolean enabled = false;

    public ParametersFromNetwork(int numberOfStrings) {

        super("[Receiving " + numberOfStrings + " parameters from network]");
        ta = Network.checkNewMessages();
        this.numberOfStrings = numberOfStrings;
        ta.getDocument().addDocumentListener(this);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        if (enabled) {
            checkLastMessage();
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    private synchronized void checkLastMessage() {
        try {
            parsedStrings = new ArrayList<>(Arrays.asList(ta.getText().split(" ")));
            if (parsedStrings.size() == numberOfStrings) {
                this.parametersReceived = true;
                notifyAll();
                enabled = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized String getParameter(int i) {
        if (this.parsedStrings!=null){
            return parsedStrings.get(i);
        }
        else {
            return null;
        }
    }

    public synchronized boolean parametersReceived() {
        return parametersReceived;
    }

    public synchronized void waitParametersReceived() throws InterruptedException {
        while (!parametersReceived) {
            wait();
        }
    }

    public synchronized boolean waitParametersReceived(int sec) throws InterruptedException {
        long start = System.currentTimeMillis();
        long end = start + sec * 1000L;
        while (!parametersReceived && System.currentTimeMillis() < end) {
            wait(sec * 1000L);
        }
        return System.currentTimeMillis() >= end;
    }

    public synchronized void waitParametersReceivedGUI(long time) throws InterruptedException {

        while (!parametersReceived) {
            if (System.currentTimeMillis() >= time) {
                Platform.runLater(() -> Platform.exitNestedEventLoop(PAUSE_KEY, new DoubleObject(this, true)));
                return;
            }
            wait(5000);

        }
        Platform.runLater(() -> Platform.exitNestedEventLoop(PAUSE_KEY, new DoubleObject(this, false)));
    }

    public synchronized void enable() {
        enabled = true;
    }

    public synchronized void disable() {
        enabled = false;
        parametersReceived = false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
