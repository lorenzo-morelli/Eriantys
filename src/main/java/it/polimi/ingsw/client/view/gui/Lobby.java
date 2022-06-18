package it.polimi.ingsw.client.view.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.client.GUI.currNode;

public class Lobby implements Initializable {
    private final GUI gui = new GUI();
    private final Gson gson = new Gson();
    ParametersFromNetwork message;
    private ClientModel receivedClientModel;
    private boolean isToReset, waitForFirst = true;
    private int myID;
    private boolean notdone = false, notread = false;

    @FXML
    private Label otherPlayersLabel = new Label();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("In attesa che gli altri giocatori si colleghino...");
        this.otherPlayersLabel.setText("...Waiting for other players to join the game...");
        this.gui.currNode = otherPlayersLabel;

        myID = gui.getClientModel().getClientIdentity();
        try {
            waitings();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitings() throws InterruptedException {
        do {
            if (!notread) {
                message = new ParametersFromNetwork(1);
                message.enable();
                long start = System.currentTimeMillis();
                long end = start + 40 * 1000L;
                if (waitForFirst) {
                    message.waitParametersReceived();
                } else {
                    boolean check = message.waitParametersReceivedMax(end);
                    if (check) {
                        System.out.println("\n\nServer non ha dato risposta");
                        Network.disconnect();
                        currNode = otherPlayersLabel;
                        this.otherPlayersLabel.setText("...Server si è disconnesso, mi disconnetto...");
                        TimeUnit.SECONDS.sleep(5);
                        System.exit(0);
                    }
                }
            }
            notread = false;
            //System.out.println(message.getParameter(0));
            ClientModel tryreceivedClientModel = gson.fromJson(message.getParameter(0), ClientModel.class);
            if (!Objects.equals(tryreceivedClientModel.getTypeOfRequest(), "CONNECTTOEXISTINGGAME")) {
                receivedClientModel = tryreceivedClientModel;
                if (Network.disconnectedClient()) {
                    Network.disconnect();
                    System.out.println("Il gioco è terminato a causa della disconnessione di un client");
                    isToReset = true;
                }

                if (receivedClientModel.isGameStarted() && receivedClientModel.NotisKicked()) {
                    waitForFirst = false;

                    // Il messaggio è o una richiesta o una risposta

                    // se il messaggio non è una risposta di un client al server vuol dire che
                    if (!receivedClientModel.isResponse() && receivedClientModel.getTypeOfRequest() != null) {
                        // il messaggio è una richiesta del server alla view di un client

                        // se il messaggio è rivolto a me devo essere io a compiere l'azione
                        if (receivedClientModel.getClientIdentity() == myID) {
                            // il messaggio è rivolto a me
                            try {
                                System.out.println("request to me");
                                gui.setClientModel(receivedClientModel);
                                gui.requestToMe();

                                Thread t = new Thread(() -> {
                                    try {
                                        wait_pings();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                t.start();
                                notdone = true;

                            } catch (InterruptedException | IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        if (!notdone && receivedClientModel.getClientIdentity() != myID && receivedClientModel.getTypeOfRequest() != null &&
                                !receivedClientModel.isPingMessage() &&
                                !receivedClientModel.getTypeOfRequest().equals("TRYTORECONNECT") &&
                                !receivedClientModel.getTypeOfRequest().equals("DISCONNECTION")) {
                            try {
                                System.out.println("request to other");
                                gui.setClientModel(receivedClientModel);
                                gui.requestToOthers();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }

            }
        } while (!isToReset && !notdone);
    }

    public synchronized void wait_pings() throws InterruptedException {
        ClientModel tryreceivedClientModel;
        do {
            message = new ParametersFromNetwork(1);
            message.enable();
            long start = System.currentTimeMillis();
            long end = start + 40 * 1000L;
            boolean check = message.waitParametersReceivedMax(end);
            if (check) {
                System.out.println("\n\nServer non ha dato risposta");
                Network.disconnect();
                currNode = otherPlayersLabel;
                this.otherPlayersLabel.setText("...Server si è disconnesso, mi disconnetto...");
                TimeUnit.SECONDS.sleep(5);
                System.exit(0);
            }

            tryreceivedClientModel = gson.fromJson(message.getParameter(0), ClientModel.class);

            if (!Objects.equals(tryreceivedClientModel.getTypeOfRequest(), "CONNECTTOEXISTINGGAME")) {

                receivedClientModel = tryreceivedClientModel;

                if (receivedClientModel.isGameStarted() && receivedClientModel.NotisKicked()) {
                    // Il messaggio è o una richiesta o una risposta

                    // se il messaggio non è una risposta di un client al server vuol dire che
                    if (receivedClientModel.isResponse().equals(false) && receivedClientModel.getTypeOfRequest() != null) {
                        // il messaggio è una richiesta del server alla view di un client

                        // se il messaggio è rivolto a me devo essere io a compiere l'azione
                        if (receivedClientModel.getClientIdentity() == myID) {
                            // il messaggio è rivolto a me
                            if (receivedClientModel.isPingMessage()) {
                                gui.requestPing();
                            }
                        }
                    }
                }
            }
        } while (!Objects.equals(tryreceivedClientModel.getTypeOfRequest(), "CONNECTTOEXISTINGGAME") && receivedClientModel.isGameStarted() && receivedClientModel.NotisKicked() && receivedClientModel.isResponse().equals(false) && receivedClientModel.getTypeOfRequest() != null && receivedClientModel.getClientIdentity() == myID && receivedClientModel.isPingMessage());
        notdone = false;
        notread = true;
        Platform.runLater(() -> {
            try {
                waitings();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
