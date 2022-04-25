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
    public void askNickname() {
        askNicknamePanel = new JPanel();
        askNicknamePanel.setLayout(null);
        askNicknamePanel.setBounds(0, 0, 800, 600);
        Gui.getContainer().add(askNicknamePanel);

        if (precedentCallingState instanceof WelcomeScreen){
            askNicknameArea = new JTextArea("Benvenuto nel magico mondo di Eriantys,\nc'è qualche dettaglio che" +
                    " ti dobbiamo chiedere\n\n" +
                    "Scrivi il tuo nickname qui sotto \nQuando hai finito premi invio");
        }
        else{
            askNicknameArea = new JTextArea("Correggi il tuo nickname:\n\n" +
                    "Scrivi il tuo nickname qui sotto \nQuando hai finito premi invio");
        }


        askNicknameArea.setEditable(false);
        askNicknameArea.setBounds(100, 100, 800, 250);
        askNicknameArea.setBackground(Color.WHITE);
        askNicknameArea.setForeground(Color.BLACK);
        askNicknameArea.setFont(Gui.getNormalFont());
        askNicknameArea.setLineWrap(true);
        askNicknamePanel.add(askNicknameArea);

        nicknameLabel = new JLabel("Nickname");
        nicknameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nicknameLabel.setFont(Gui.getButtonFont());
        nicknameLabel.setSize(600, 25);
        nicknameLabel.setLocation(100, 425);
        askNicknamePanel.add(nicknameLabel);
        nickname = new JTextField();
        nickname.setSize(300, 25);
        nickname.setLocation(250, 450);
        nickname.addActionListener(this);
        askNicknamePanel.add(nickname);
    }

    @Override
    public void askNicknameConfirmation(String nickname) {
        askNicknameConfirmationPanel = new JPanel();
        askNicknameConfirmationPanel.setLayout(null);
        askNicknameConfirmationPanel.setBounds(0, 0, 800, 600);
        Gui.getContainer().add(askNicknameConfirmationPanel);
        askNicknameConfirmationArea = new JTextArea("Ok, se ho capito bene il tuo nickname è\n" +
                nickname + ", mi confermi?");
        askNicknameConfirmationArea.setEditable(false);
        askNicknameConfirmationArea.setBounds(100, 100, 800, 250);
        askNicknameConfirmationArea.setBackground(Color.WHITE);
        askNicknameConfirmationArea.setForeground(Color.BLACK);
        askNicknameConfirmationArea.setFont(Gui.getNormalFont());
        askNicknameConfirmationArea.setLineWrap(true);
        askNicknameConfirmationPanel.add(askNicknameConfirmationArea);
        siButton = new JButton("Si");
        siButton.setSize(300, 25);
        siButton.setLocation(100, 400);
        siButton.addActionListener(this);
        askNicknameConfirmationPanel.add(siButton);
        noButton = new JButton("No");
        noButton.setSize(300, 25);
        noButton.setLocation(400, 400);
        noButton.addActionListener(this);
        askNicknameConfirmationPanel.add(noButton);
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

            if (event.getSource() == nickname) {
                if (!nickname.getText().equals("")) {
                    System.out.println(nickname.getText());
                    ((AskNicknameScreen) callingState).setNickname(nickname.getText());
                    ((AskNicknameScreen) callingState).nickname().fireStateEvent();
                    askNicknamePanel.setVisible(false);
                }
            }
            if (event.getSource() == noButton) {
                ((CheckNicknameScreen) callingState).no().fireStateEvent();
                askNicknameConfirmationPanel.setVisible(false);
            }
            if (event.getSource() == siButton) {
                ((CheckNicknameScreen) callingState).si().fireStateEvent();
                askNicknameConfirmationPanel.setVisible(false);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
