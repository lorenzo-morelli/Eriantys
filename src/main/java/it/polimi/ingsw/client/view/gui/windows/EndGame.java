package it.polimi.ingsw.client.view.gui.windows;

import it.polimi.ingsw.client.view.gui.GuiView;
import it.polimi.ingsw.client.view.gui.Menu;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utils.network.Network;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static it.polimi.ingsw.client.view.gui.GuiView.currNode;

public class EndGame implements Initializable {
    private final GuiView guiView = new GuiView();
    @FXML
    private Label winLabel;
    @FXML
    private Label winnerName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currNode = winLabel;
        Menu menu = new Menu();
        String me = menu.getNickname();
        String result = this.guiView.getClientModel().getGameWinner();
        int numOfPlayers = this.guiView.getClientModel().getServerModel().getNumberOfPlayers();
        if (result.equals("PAREGGIO")) {
            winLabel.setText("TIE!");
            winnerName.setText("It's a tie and everybody won! ;)");
        } else if (numOfPlayers < 4) {
            if (result.equals(me)) {
                winLabel.setText("YOU WON!");
                winnerName.setText("Congratulations! You won the game");
            } else {
                winLabel.setText("You lost...");
                winnerName.setText("You lost the game :( better luck next time!");
            }
        } else {
            if (result.contains(me)) {
                winLabel.setText("YOUR TEAM WON!");
                winnerName.setText("Congratulations! Your team won the game");
            } else {
                winLabel.setText("Your team lost...");
                winnerName.setText("Your team lost the game :( better luck next time!");
            }
        }
    }

    /**
     * This event redirects to the main menu of the game.
     */
    @FXML
    private void toMainMenu() throws IOException {
        this.guiView.changeScene("MainMenu");
    }
}

