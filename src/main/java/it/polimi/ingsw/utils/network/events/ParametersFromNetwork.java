package it.polimi.ingsw.utils.network.events;

import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.Arrays;

public class ParametersFromNetwork extends Event implements DocumentListener {
    private static JTextArea ta;
    private int numberOfStrings;
    private ArrayList<String> parsedStrings;
    private boolean parametersReceived = false;

    private boolean enabled = false;

    public ParametersFromNetwork(int numberOfStrings) {

        super("[Ricezione di "+numberOfStrings+" parametri da network]");
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
            parsedStrings = new ArrayList(Arrays.asList(ta.getText().split(" ")));
            if (parsedStrings.size() == numberOfStrings){
                this.parametersReceived = true;
                enabled = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized String getParameter(int i){
        return parsedStrings.get(i);
    }

    public synchronized boolean parametersReceived() {
        return parametersReceived;
    }

    public synchronized void enable(){
        enabled = true;
    }

    public synchronized void disable(){
        enabled = false;
        parametersReceived = false;
    }

}
