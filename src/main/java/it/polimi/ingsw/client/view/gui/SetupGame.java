package it.polimi.ingsw.client.view.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.other.DoubleObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.client.GUI.currNode;
import static it.polimi.ingsw.client.view.gui.Lobby.PAUSE_KEY;

public class SetupGame implements Initializable {
    private final GUI gui = new GUI();
    private final Gson gson = new Gson();
    private boolean isToReset;
    private boolean waitForFirst = true;
    private int myID;
    private ParametersFromNetwork message;
    private boolean notRead = false;

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
        this.connectedOnIp.setText("Connected on IP: " + this.gui.getClientModel().getIp());
        this.connectedOnPort.setText("Connected on Port: " + this.gui.getClientModel().getPort());
        currNode = otherPlayersLabel;
        isToReset = false;
    }

    public void set2Players() {
        this.gui.getClientModel().setNumofplayer(2);
        numberOfPlayersLabel.setText("Number of players: " + this.gui.getClientModel().getNumofplayer());
    }

    public void set3Players() {
        this.gui.getClientModel().setNumofplayer(3);
        numberOfPlayersLabel.setText("Number of players: " + this.gui.getClientModel().getNumofplayer());
    }

    public void set4Players() {
        this.gui.getClientModel().setNumofplayer(4);
        numberOfPlayersLabel.setText("Number of players: " + this.gui.getClientModel().getNumofplayer());
    }

    public void setPrincipiant() {
        this.gui.getClientModel().setGameMode("PRINCIPIANT");
        this.gameModeLabel.setText("Game mode: principiant");
    }

    public void setExpert() {
        this.gui.getClientModel().setGameMode("EXPERT");
        this.gameModeLabel.setText("Game mode: expert");
    }

    public void start() throws InterruptedException, IOException {
        if (this.gui.getClientModel().getNumofplayer() != 2 && this.gui.getClientModel().getNumofplayer() != 3 && this.gui.getClientModel().getNumofplayer() != 4) {
            this.otherPlayersLabel.setText("ERROR: Please select a number of players!");
        } else if (this.gui.getClientModel().getGameMode() == null) {
            this.otherPlayersLabel.setText("ERROR: Please select a game mode!");
        } else {
            Network.send(gson.toJson(this.gui.getClientModel()));
            System.out.println("In attesa che gli altri giocatori si colleghino...");
            this.otherPlayersLabel.setText("...Waiting for other players to join the game...");

            myID = gui.getClientModel().getClientIdentity();
            long start = System.currentTimeMillis();
            long end = start + 40 * 1000L;
            try {
                waitings(end);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void waitings(long end) throws InterruptedException {
        boolean notDone = false;
        do {
            System.out.println("primo loop");
            if (!notRead) {
                message = new ParametersFromNetwork(1);
                message.enable();
                long finalEnd = end;
                Thread thread = new Thread(() -> {
                    try {
                        message.waitParametersReceivedGUI(finalEnd);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                thread.start();
                DoubleObject responce = ((DoubleObject) Platform.enterNestedEventLoop(PAUSE_KEY));
                boolean check = responce.isRespo();
                message = responce.getParame();
                if (check && waitForFirst) {
                    waitings(System.currentTimeMillis() + 40000L);
                    return;
                }
                if (check) {
                    System.out.println("\n\nServer non ha dato risposta");
                    Network.disconnect();
                    currNode = otherPlayersLabel;
                    this.otherPlayersLabel.setText("...Server si è disconnesso, mi disconnetto...");
                    TimeUnit.SECONDS.sleep(5);
                    System.exit(0);
                }
            }
            notRead = false;
            ClientModel clientModel = gson.fromJson(message.getParameter(0), ClientModel.class);
            if (!Objects.equals(clientModel.getTypeOfRequest(), "CONNECTTOEXISTINGGAME")) {
                if (Network.disconnectedClient()) {
                    Network.disconnect();
                    System.out.println("Il gioco è terminato a causa della disconnessione di un client");
                    isToReset = true;
                }
                if (clientModel.isGameStarted() && clientModel.NotisKicked()) {
                    waitForFirst = false;
                    if (!clientModel.isResponse() && clientModel.getTypeOfRequest() != null) {
                        if (clientModel.getClientIdentity() == myID) {
                            try {
                                System.out.println("request to me");
                                if (clientModel.isPingMessage()) {
                                    gui.requestPing();
                                } else {
                                    gui.setClientModel(clientModel);
                                    gui.requestToMe();
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        if (!notDone && clientModel.getClientIdentity() != myID && clientModel.getTypeOfRequest() != null &&
                                !clientModel.isPingMessage() &&
                                !clientModel.getTypeOfRequest().equals("TRYTORECONNECT") &&
                                !clientModel.getTypeOfRequest().equals("DISCONNECTION")) {
                            try {

                                System.out.println("request to other");
                                gui.setClientModel(clientModel);
                                gui.requestToOthers();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
            end = System.currentTimeMillis() + 40000L;
        }
        while (!isToReset && !notDone);
    }
}
