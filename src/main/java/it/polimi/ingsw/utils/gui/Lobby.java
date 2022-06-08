package it.polimi.ingsw.utils.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.GUI.currNode;

public class Lobby implements Initializable {
    private final GUI gui = new GUI();
    private final Gson gson = new Gson();
    private ParametersFromNetwork response;

    @FXML
    private Label otherPlayersLabel = new Label();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currNode = otherPlayersLabel;
        this.otherPlayersLabel.setText("Waiting for other players to start the game...");

        response = new ParametersFromNetwork(1);
        response.enable();
        while (!response.parametersReceived()) {
            System.out.println("waiting...");
        }
        this.gui.setClientModel(gson.fromJson(response.getParameter(0), ClientModel.class));

        if (!this.gui.getClientModel().isGameStarted()) { // todo: bah...
            try {
                this.gui.changeScene("Game");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //todo:
    }
}
