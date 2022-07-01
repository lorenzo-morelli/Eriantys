package it.polimi.ingsw.utils.network.events;

import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.Event;
import javafx.application.Platform;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class handle with the Event of reviving a parameters send over the network
 */
public class ParametersFromNetwork extends Event implements DocumentListener {
    private static JTextArea ta;
    private final int numberOfStrings;
    private ArrayList<String> parsedStrings;
    private boolean parametersReceived = false;
    public static final Object PAUSE_KEY = new Object();

    private boolean enabled = false;

    public ParametersFromNetwork(int numberOfStrings) {

        super("[Reception of " + numberOfStrings + " parameters from network]");
        ta = Network.checkNewMessages();
        this.numberOfStrings = numberOfStrings;
        ta.getDocument().addDocumentListener(this);
    }

    /**
     * This method is called when something is updated over the network and check if the message is arrived
     */
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

    /**
     * This method check if the message is arrived and notify all the waiting thread that needs this message
     */
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

    /**
     * This method return the parameters contained in the message
     */
    public synchronized String getParameter(int i) {
        if (this.parsedStrings!=null){
            return parsedStrings.get(i);
        }
        else {
            return null;
        }
    }

    /**
     * @return true if the message is arrived
     */
    public synchronized boolean parametersReceived() {
        return parametersReceived;
    }

    /**
     * This method goes in wait until the parameters is not received
     */
    public synchronized void waitParametersReceived() throws InterruptedException {
        while (!parametersReceived) {
            wait();
        }
    }

    /**
     * This method goes in wait until the parameters is not received with a timeout of @param sec (if the timeout expires this method return true, otherwise false)
     */
    public synchronized boolean waitParametersReceived(int sec) throws InterruptedException {
        long start = System.currentTimeMillis();
        long end = start + sec * 1000L;
        while (!parametersReceived && System.currentTimeMillis() < end) {
            wait(sec * 1000L);
        }
        return System.currentTimeMillis() >= end;
    }

    /**
     * This method goes in wait until the parameters is not received with a timeout of @param sec (if the timeout expires this method return true, otherwise false)
     * and is build to function with the javaFx Thread
     */
    public synchronized void waitParametersReceivedGUI(long time) throws InterruptedException {

        while (!parametersReceived) {
            if (System.currentTimeMillis() >= time) {
                Platform.runLater(() -> Platform.exitNestedEventLoop(PAUSE_KEY, new ResultOfWaiting(this, true)));
                return;
            }
            wait(5000);

        }
        Platform.runLater(() -> Platform.exitNestedEventLoop(PAUSE_KEY, new ResultOfWaiting(this, false)));
    }

    /**
     * Enable the message to be used
     */
    public synchronized void enable() {
        enabled = true;
    }

    /**
     * Disable the message to be used
     */
    public synchronized void disable() {
        enabled = false;
        parametersReceived = false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
