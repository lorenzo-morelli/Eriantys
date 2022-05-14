package it.polimi.ingsw.utils.gui.controllers;

import it.polimi.ingsw.utils.gui.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainMenuController {
    private final GUI gui = new GUI();

    @FXML
    private TextField nickname;
    @FXML
    private TextField ip;
    @FXML
    private TextField port;
    @FXML
    private Label notice;

    public void play(MouseEvent mouseEvent) throws IOException {
        this.gui.changeScene("SetupGame", mouseEvent);
    }

    public void exit() {
        System.exit(0);
    }

    public void start() {
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
            this.notice.setText("Ok, per ora");
            int ip = Integer.parseInt(ipText);
            int port = Integer.parseInt(portText);
            //TODO: connection verification and game connection with socket
        }
    }
}
