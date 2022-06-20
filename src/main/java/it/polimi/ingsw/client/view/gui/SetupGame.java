package it.polimi.ingsw.client.view.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.client.GUI.currNode;

public class SetupGame implements Initializable {
    private final GUI gui = new GUI();
    private final Gson gson = new Gson();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currNode = otherPlayersLabel;
        this.connectedOnIp.setText("Connected on IP: " + this.gui.getClientModel().getIp());
        this.connectedOnPort.setText("Connected on Port: " + this.gui.getClientModel().getPort());
    }

    @FXML
    private void set2Players() {
        this.gui.getClientModel().setNumofplayer(2);
        numberOfPlayersLabel.setText("Number of players: " + this.gui.getClientModel().getNumofplayer());
    }

    @FXML
    private void set3Players() {
        this.gui.getClientModel().setNumofplayer(3);
        numberOfPlayersLabel.setText("Number of players: " + this.gui.getClientModel().getNumofplayer());
    }

    @FXML
    private void set4Players() {
        this.gui.getClientModel().setNumofplayer(4);
        numberOfPlayersLabel.setText("Number of players: " + this.gui.getClientModel().getNumofplayer());
    }

    @FXML
    private void setPrincipiant() {
        this.gui.getClientModel().setGameMode("PRINCIPIANT");
        this.gameModeLabel.setText("Game mode: principiant");
    }

    @FXML
    private void setExpert() {
        this.gui.getClientModel().setGameMode("EXPERT");
        this.gameModeLabel.setText("Game mode: expert");
    }

    /**
     * This event is used by the client host to create a new game with the chosen parameters
     * and to send the model to the server.
     */
    @FXML
    private void start() throws InterruptedException, IOException {
        if (this.gui.getClientModel().getNumofplayer() != 2 && this.gui.getClientModel().getNumofplayer() != 3 && this.gui.getClientModel().getNumofplayer() != 4) {
            this.otherPlayersLabel.setText("ERROR: Please select a number of players!");
        } else if (this.gui.getClientModel().getGameMode() == null) {
            this.otherPlayersLabel.setText("ERROR: Please select a game mode!");
        } else {
            boolean responseReceived = false;
            Network.send(gson.toJson(this.gui.getClientModel()));
            while (!responseReceived) {
                System.out.println("invio al server in attesa di ack...");
                ParametersFromNetwork ack = new ParametersFromNetwork(1);
                ack.enable();
                long start = System.currentTimeMillis();
                long end = start + 15 * 1000L;
                boolean check = ack.waitParametersReceived(5);
                if (check || System.currentTimeMillis() >= end) {
                    System.out.println("\n\nServer non ha dato risposta");
                    Network.disconnect();
                    this.otherPlayersLabel.setText("...Server non ha dato alcuna risposta, mi disconnetto...");
                    TimeUnit.SECONDS.sleep(5);
                    System.exit(0);
                }

                if (gson.fromJson(ack.getParameter(0), ClientModel.class).getClientIdentity() == this.gui.getClientModel().getClientIdentity()) {
                    responseReceived = true;
                }
            }
            System.out.println("[Conferma ricevuta]");
            this.gui.changeScene("Lobby");
        }
    }
}