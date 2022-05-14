package it.polimi.ingsw.utils.gui.controllers;

import it.polimi.ingsw.client.controller.states.WelcomeScreen;
import it.polimi.ingsw.utils.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class MenuController {
    private final GUI gui = new GUI();

    @FXML
    private TextField nickname;
    @FXML
    private TextField ip;
    @FXML
    private TextField port;
    @FXML
    private Label notice;

    @FXML
    private Label connectedOnIp;
    @FXML
    private Label connectedOnPort;

    @FXML
    private int numberOfPlayers;
    @FXML
    private Label otherPlayersLabel;
    @FXML
    public Label numberOfPlayersLabel;
    @FXML
    public Label gameModeLabel;

    public void play(MouseEvent mouseEvent) throws IOException {
        this.gui.changeScene("SetupConnection", mouseEvent);
    }

    public void exit() {
        System.exit(0);
    }

    public void connect() throws Exception {
        String nickname = this.nickname.getText();
        String ipText = this.ip.getText();
        String portText = this.port.getText();

        if (nickname.equals("") || ipText.equals("") || portText.equals("")) {
            this.notice.setText("FAILURE: missing parameters!");
        } else if (this.nickname.getText().length() > 13) {
            this.notice.setText("FAILURE: nickname must be less than 13 characters!");
        } else if (ipText.contains(" ") || portText.contains(" ")) {
            this.notice.setText("FAILURE: ip and port can't contain any spaces!");
        } else {
            try {
                int port = Integer.parseInt(portText);
            } catch (Exception e) {
                this.notice.setText("FAILURE: ip and port MUST be numbers!");
            }

            this.notice.setText("ip: " + ip + "\nport: " + port);
            //TODO: connection verification and game connection with socket
            ((WelcomeScreen) gui.callingState).start().fireStateEvent();

        }
    }

    public void set2Players(MouseEvent mouseEvent) {
        this.numberOfPlayers = 2;
        numberOfPlayersLabel.setText("Number of players: 2");
    }

    public void set3Players(MouseEvent mouseEvent) {
        this.numberOfPlayers = 3;
        numberOfPlayersLabel.setText("Number of players: 3");
    }

    public void set4Players(MouseEvent mouseEvent) {
        this.numberOfPlayers = 4;
        numberOfPlayersLabel.setText("Number of players: 4");
    }

    public void setPrincipiant(MouseEvent mouseEvent) {
        this.gameModeLabel.setText("Game mode: principiant");
    }

    public void setExpert(MouseEvent mouseEvent) {
        this.gameModeLabel.setText("Game mode: expert");
    }

    public void start(MouseEvent mouseEvent) {
        //TODO: create game
        this.otherPlayersLabel.setText("Waiting for other players... " + 1 + "/" + 4);
        //if raggiunto il totale -> inizia partita
    }
}
