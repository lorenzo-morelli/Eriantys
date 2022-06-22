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

public class Lobby implements Initializable {
    public static final Object PAUSE_KEY = new Object();
    private final GUI gui = new GUI();
    private final Gson gson = new Gson();
    private ParametersFromNetwork message;
    private boolean isToReset;
    private int myID;
    private boolean notRead = false;

    @FXML
    private Label otherPlayersLabel = new Label();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currNode = otherPlayersLabel;
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

    public void waitings(long end) throws InterruptedException {
        boolean notdone = false;
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
                boolean check = responce.isResp();
                message = responce.getParam();


                if (check) {
                    System.out.println("\n\nServer non ha dato risposta");
                    Network.disconnect();
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

                        if (!notdone && clientModel.getClientIdentity() != myID && clientModel.getTypeOfRequest() != null &&
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
        while (!isToReset && !notdone);
    }


}
