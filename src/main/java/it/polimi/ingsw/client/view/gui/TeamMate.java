package it.polimi.ingsw.client.view.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.utils.network.Network;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.view.gui.GuiView.currNode;

public class TeamMate implements Initializable {
    private final GuiView guiView = new GuiView();
    @FXML
    private Button player1;
    @FXML
    private Button player2;
    @FXML
    private Button player3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currNode = player1;
        ArrayList<String> players = this.guiView.getClientModel().getNicknames();
        players.remove(this.guiView.getClientModel().getNickname());
        ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(player1, player2, player3));
        buttons.forEach(button -> {
            button.setText(players.get(buttons.indexOf(button)));
            button.setOnMouseClicked(event -> {
                String teamMate = players.get(buttons.indexOf(button));
                this.guiView.getClientModel().getNicknames().remove(teamMate);
                this.guiView.getClientModel().getNicknames().add(teamMate);
                this.guiView.getClientModel().getNicknames().add(this.guiView.getClientModel().getNickname());
                this.guiView.getClientModel().setResponse(true);
                this.guiView.getClientModel().setPingMessage(false);
                Gson gson = new Gson();
                Network.send(gson.toJson(this.guiView.getClientModel()));
            });
        });
    }
}
