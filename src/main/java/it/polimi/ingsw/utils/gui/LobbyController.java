package it.polimi.ingsw.utils.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {
    private final GUI gui = new GUI();
    private final Gson gson = new Gson();

    @FXML
    private Label otherPlayersLabel = new Label();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.otherPlayersLabel.setText("Waiting for other players to start the game...");
        //todo:
    }
}
