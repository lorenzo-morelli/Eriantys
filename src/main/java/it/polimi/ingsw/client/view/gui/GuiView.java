package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.controller.states.WelcomeScreen;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.stateMachine.State;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GuiView implements View, ActionListener {

    // La vista matiene una reference allo stato chiamante (schermata video/command line) ed al precedente.
    private State callingState;
    private State precedentCallingState;

    // Uno stato che vuole chiamare un metodo della vista si registra prima chiamando questo metodo
    // ad esempio sono nello stato WelcomeScreen e faccio "view.setCallingState(this)"
    // Non è altro che il pattern Observer riadattato per il pattern State
    @Override
    public void setCallingState(State callingState) {
        this.precedentCallingState = this.callingState;
        this.callingState = callingState;
    }

    @Override
    public void setClientModel(ClientModel clientModel) {

    }

    // Elementi grafici (finestre, bottoni, fuochi d'artificio...) (libreria SWING)
    JFrame window;
    JPanel titleNamePanel, startButtonPanel, userInfoPanel;
    JLabel titleNameLabel, nicknameLabel, ipLabel, portLabel;
    JButton startButton, sendButton;

    JTextField nickname,ip, port;


    public GuiView(){
        window = new JFrame("Eriantys");
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.WHITE);
        window.setLayout(null);
        window.setVisible(true);
        Gui.setContainer(window.getContentPane());


        titleNamePanel = new ImagePanel(background);
        window.repaint();
        titleNamePanel.setBounds(0, 0, 800, 600);
        titleNameLabel = new JLabel("Eriantys");
        titleNameLabel.setForeground(Color.BLUE);
        titleNameLabel.setFont(Gui.getTitleFont());


        startButtonPanel = new JPanel();
        startButtonPanel.setBounds(350, 500, 100, 50);
        startButtonPanel.setBackground(Color.WHITE);

        startButton = new JButton("START");
        startButton.setBackground(Color.WHITE);
        startButton.setForeground(Color.BLACK);
        startButton.setOpaque(true);
        startButton.setBorderPainted(false);
        startButton.setFont(Gui.getNormalFont());
        startButton.addActionListener(this);

        titleNamePanel.add(titleNameLabel);
        startButtonPanel.add(startButton);

        Gui.getContainer().add(titleNamePanel);
        Gui.getContainer().add(startButtonPanel);

    }

    @Override
    public void askToStart() {
        window.setVisible(true);
        window.setResizable(false);
    }

    @Override
    public void askDecision(String option1, String option2) {

    }

    @Override
    public void askParameters() {

    }

    @Override
    public void requestToMe() throws InterruptedException {

    }

    @Override
    public void requestToOthers() throws IOException {

    }

    @Override
    public void response() throws IOException {

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (event.getSource() == startButton) {
                ((WelcomeScreen) callingState).start().fireStateEvent();
                titleNamePanel.setVisible(false);
                startButtonPanel.setVisible(false);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}