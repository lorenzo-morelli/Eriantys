package it.polimi.ingsw.utils.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;


public class SetupGame implements Initializable {
    private final GUI gui = new GUI();
    private final Gson gson = new Gson();
    private int connectedPlayers;
    private ParametersFromNetwork response;
    private ClientModel receivedClientModel;
    private boolean isToReset;

    @FXML
    private Label connectedOnIp = new Label();
    @FXML
    private Label connectedOnPort = new Label();


    @FXML
    private Label otherPlayersLabel = new Label();
    @FXML
    public Label numberOfPlayersLabel;
    @FXML
    public Label gameModeLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.connectedOnIp.setText("Connected on IP: " + this.gui.getClientModel().getIp());
        this.connectedOnPort.setText("Connected on Port: " + this.gui.getClientModel().getPort());
        this.otherPlayersLabel.setText("okkkkkk");
        this.connectedPlayers = 0;
        isToReset = false;
    }

    public void set2Players(MouseEvent mouseEvent) {
        this.gui.getClientModel().setNumofplayer(2);
        numberOfPlayersLabel.setText("Number of players: " + this.gui.getClientModel().getNumofplayer());
    }

    public void set3Players(MouseEvent mouseEvent) {
        this.gui.getClientModel().setNumofplayer(3);
        numberOfPlayersLabel.setText("Number of players: " + this.gui.getClientModel().getNumofplayer());
    }

    public void set4Players(MouseEvent mouseEvent) {
        this.gui.getClientModel().setNumofplayer(4);
        numberOfPlayersLabel.setText("Number of players: " + this.gui.getClientModel().getNumofplayer());
    }

    public void setPrincipiant(MouseEvent mouseEvent) {
        this.gui.getClientModel().setGameMode("PRINCIPIANT");
        this.gameModeLabel.setText("Game mode: principiant");
    }

    public void setExpert(MouseEvent mouseEvent) {
        this.gui.getClientModel().setGameMode("EXPERT");
        this.gameModeLabel.setText("Game mode: expert");
    }

    public void start(MouseEvent mouseEvent) throws InterruptedException, IOException {
        if (this.gui.getClientModel().getNumofplayer() != 2 && this.gui.getClientModel().getNumofplayer() != 3 && this.gui.getClientModel().getNumofplayer() != 4) {
            this.otherPlayersLabel.setText("ERROR: Please select a number of players!");
            System.out.println(this.gui.getClientModel().getNumofplayer());
        } else if (this.gui.getClientModel().getGameMode() == null) {
            this.otherPlayersLabel.setText("ERROR: Please select a game mode!");
        } else {
            this.otherPlayersLabel.setText("Waiting for other players to join the game...");
            Network.send(gson.toJson(this.gui.getClientModel()));
            System.out.println("invio al server...");

            ParametersFromNetwork message = new ParametersFromNetwork(1);
            message.enable();
            message.waitParametersReceived();

            Thread t = new Thread(){
                public synchronized void run() {
                    System.out.println("ricevo il model...");
                    ClientModel tryreceivedClientModel = gson.fromJson(message.getParameter(0), ClientModel.class);

                    if(Objects.equals(tryreceivedClientModel.getTypeOfRequest(), "CONNECTTOEXISTINGGAME")){
                        return;
                    }

                    receivedClientModel=tryreceivedClientModel;

                    if (Network.disconnectedClient()) {
                        Network.disconnect();
                        System.out.println("Il gioco è terminato a causa della disconnessione di un client");
                        isToReset = true;
                    }

                    if (receivedClientModel.isGameStarted().equals(true) && receivedClientModel.NotisKicked()) {
                        System.out.println("gioco iniziato");

                        // Il messaggio è o una richiesta o una risposta

                        // se il messaggio non è una risposta di un client al server vuol dire che
                        if (receivedClientModel.isResponse().equals(false) && receivedClientModel.getTypeOfRequest() != null) {
                            // il messaggio è una richiesta del server alla view di un client

                            // se il messaggio è rivolto a me devo essere io a compiere l'azione
                            if (receivedClientModel.getClientIdentity() == gui.getClientModel().getClientIdentity()) {
                                // il messaggio è rivolto a me
                                if (receivedClientModel.isPingMessage()) {
                                    gui.requestPing();
                                } else {
                                    try {
                                        System.out.println("request to me");
                                        gui.setClientModel(receivedClientModel);
                                        gui.requestToMe(otherPlayersLabel);
                                    } catch (InterruptedException | IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            } else {
                                // altrimenti devo limitarmi a segnalare che l'altro giocatore sta facendo qualcosa
                                if (receivedClientModel.getTypeOfRequest() != null &&
                                        !receivedClientModel.isPingMessage() && !Objects.equals(receivedClientModel.getTypeOfRequest(), "TRYTORECONNECT") && !Objects.equals(receivedClientModel.getTypeOfRequest(), "DISCONNECTION")) {
                                    try {
                                        System.out.println("request to others");
                                        gui.setClientModel(receivedClientModel);
                                        gui.requestToOthers();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }
                        // altrimenti il messaggio è una risposta di un altro client ad un server
                        else if (receivedClientModel.isResponse().equals(true) && receivedClientModel.getTypeOfRequest() != null) {
                            try {
                                System.out.println("response");
                                gui.setClientModel(receivedClientModel);
                                gui.response();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            };
            t.start();

            if (isToReset) {
                System.out.println("resetto");
            } else {
                System.out.println("ok...");
            }
        }
    }
}

