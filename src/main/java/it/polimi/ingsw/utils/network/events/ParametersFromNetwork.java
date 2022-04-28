package it.polimi.ingsw.utils.network.events;

import it.polimi.ingsw.utils.network.Network;
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

    public ParametersFromNetwork(int numberOfStrings) {

        super("[Ricezione di "+numberOfStrings+" parametri da network]");
        this.ta = Network.checkNewMessages();
        this.numberOfStrings = numberOfStrings;
        ta.getDocument().addDocumentListener(this);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        checkLastMessage();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    private void checkLastMessage() {
        try {
            parsedStrings = new ArrayList(Arrays.asList(ta.getText().split(" ")));
            if (parsedStrings.size() == numberOfStrings){
                System.out.println("[Ricevuto: "+ ta.getText() +"]" );
                this.parametersReceived = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean parametersReceived() {
        return parametersReceived;
    }
}
