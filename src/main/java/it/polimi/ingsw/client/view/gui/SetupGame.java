package it.polimi.ingsw.client.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.view.gui.GuiView.currNode;

public class SetupGame implements Initializable {
    private final GuiView guiView = new GuiView();
    @FXML
    private Label connectedOnIp = new Label();
    @FXML
    private Label connectedOnPort = new Label();


    @FXML
    private Label otherPlayersLabel = new Label();
    @FXML
    private Label numberOfPlayersLabel;
    @FXML
    private Label gameModeLabel;
    @FXML
    private ImageView loading;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currNode = otherPlayersLabel;
        loading.setVisible(false);
        this.connectedOnIp.setText("Connected on IP: " + this.guiView.getClientModel().getIp());
        this.connectedOnPort.setText("Connected on Port: " + this.guiView.getClientModel().getPort());
        this.gameModeLabel.setText("");
        this.numberOfPlayersLabel.setText("");
        otherPlayersLabel.setText("");
    }

    public void set2Players() {
        this.guiView.getClientModel().setNumOfPlayers(2);
        numberOfPlayersLabel.setText("Number of players: " + this.guiView.getClientModel().getNumOfPlayers());
    }

    public void set3Players() {
        this.guiView.getClientModel().setNumOfPlayers(3);
        numberOfPlayersLabel.setText("Number of players: " + this.guiView.getClientModel().getNumOfPlayers());
    }

    public void set4Players() {
        this.guiView.getClientModel().setNumOfPlayers(4);
        numberOfPlayersLabel.setText("Number of players: " + this.guiView.getClientModel().getNumOfPlayers());
    }

    public void setBeginner() {
        this.guiView.getClientModel().setGameMode("BEGINNER");
        this.gameModeLabel.setText("Game mode: Beginner");
    }

    public void setExpert() {
        this.guiView.getClientModel().setGameMode("EXPERT");
        this.gameModeLabel.setText("Game mode: expert");
    }

    /**
     * This event creates a new game with the chosen parameters.
     */
    public void start() {
        if (this.guiView.getClientModel().getNumOfPlayers() != 2 && this.guiView.getClientModel().getNumOfPlayers() != 3 && this.guiView.getClientModel().getNumOfPlayers() != 4) {
            this.otherPlayersLabel.setText("ERROR: Please select a number of players!");
        } else if (this.guiView.getClientModel().getGameMode() == null) {
            this.otherPlayersLabel.setText("ERROR: Please select a game mode!");
        } else {
            loading.setVisible(true);
            this.otherPlayersLabel.setText("...Waiting for other players to join the game...");
            ConnectionToServer connection = new ConnectionToServer();
            connection.connect(true);
        }
    }
}
