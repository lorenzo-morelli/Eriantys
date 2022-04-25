package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.states.*;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utils.gui.Gui;
import it.polimi.ingsw.utils.gui.ImagePanel;
import it.polimi.ingsw.utils.stateMachine.State;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GuiView implements View, ActionListener {

    // La vista matiene una reference allo stato chiamante (schermata video/command line) ed al precedente.
    private State callingState;
    private State precedentCallingState;

    // Elementi grafici
    JFrame window;
    JPanel titleNamePanel, startButtonPanel, askNicknamePanel, askNicknameConfirmationPanel, confirmationPanel;
    JLabel titleNameLabel, nicknameLabel, backgroundLabel;
    JButton startButton, siButton, noButton;
    JTextArea askNicknameArea, askNicknameConfirmationArea, confirmationArea;
    JTextField nickname;

    Image background = (new ImageIcon(getClass().getResource("/GuiResources/background.jpg"))).getImage();


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
    public void askConnectionInfo() {

    }

    @Override
    public void setCallingState(State callingState) {
        this.precedentCallingState = this.callingState;
        this.callingState = callingState;
    }

    @Override
    public void askToStart() {
        window.setVisible(true);
        window.setResizable(false);
    }

    @Override
    public void askConnectOrCreate() {

    }


    @Override
    public void showConfirmation(String nickname) {

        confirmationPanel = new JPanel();
        confirmationPanel.setLayout(null);
        confirmationPanel.setBounds(0,0,800,600);
        Gui.getContainer().add(confirmationPanel);

        confirmationArea = new JTextArea("Bene, allora ti chiamerò " +
                nickname+".");
        confirmationArea.setEditable(false);
        confirmationArea.setBounds(100,100,800,250);
        confirmationArea.setBackground(Color.WHITE);
        confirmationArea.setForeground(Color.BLACK);
        confirmationArea.setFont(Gui.getNormalFont());
        confirmationArea.setLineWrap(true);
        confirmationPanel.add(confirmationArea);
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
