package it.polimi.ingsw.client.view.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.utils.network.Network;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.GUI.currNode;

public class TeamMate implements Initializable {
    private final GUI gui = new GUI();
    @FXML
    private Button player1;
    @FXML
    private Button player2;
    @FXML
    private Button player3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currNode = player1;
        ArrayList<String> players = this.gui.getClientModel().getNicknames();
        players.remove(this.gui.getClientModel().getNickname());
        ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(player1, player2, player3));
        buttons.forEach(button -> {
            button.setText(players.get(buttons.indexOf(button)));
            button.setOnMouseClicked(event -> {
                String teamMate = players.get(buttons.indexOf(button));
                this.gui.getClientModel().getNicknames().remove(teamMate);
                this.gui.getClientModel().getNicknames().add(teamMate);
                this.gui.getClientModel().getNicknames().add(this.gui.getClientModel().getNickname());
                this.gui.getClientModel().setResponse(true);
                this.gui.getClientModel().setPingMessage(false);
                Gson gson = new Gson();
                try {
                    Network.send(gson.toJson(this.gui.getClientModel()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
