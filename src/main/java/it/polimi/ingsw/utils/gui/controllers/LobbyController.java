package it.polimi.ingsw.utils.gui.controllers;

import com.google.gson.Gson;
import it.polimi.ingsw.utils.gui.GUI;
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
        int connectedPlayers = 2;
        //todo: far arrivare numofplayer dal server + aggiornare connectedplayers
        this.otherPlayersLabel.setText("Waiting for other players... " + connectedPlayers + "/" + this.gui.getClientModel().getNumofplayer());
        //todo:
    }
}
