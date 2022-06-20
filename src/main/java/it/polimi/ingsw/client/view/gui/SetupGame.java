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
import javafx.scene.input.MouseEvent;

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
    private int connectedPlayers;
    private ClientModel receivedClientModel;
    private boolean isToReset, waitForFirst = true, check, notdone=false;
    private int myID;

    ParametersFromNetwork message;

    private boolean notread = false;

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
        this.connectedPlayers = 0;
        this.gui.currNode = otherPlayersLabel;
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

    public void start(MouseEvent mouseEvent) throws InterruptedException, IOException { //todo : questo è lo start da primo client, c'è da fare anche quello da non primo client
        if (this.gui.getClientModel().getNumofplayer() != 2 && this.gui.getClientModel().getNumofplayer() != 3 && this.gui.getClientModel().getNumofplayer() != 4) {
            this.otherPlayersLabel.setText("ERROR: Please select a number of players!");
        } else if (this.gui.getClientModel().getGameMode() == null) {
            this.otherPlayersLabel.setText("ERROR: Please select a game mode!");
        } else {
            this.gui.currNode = otherPlayersLabel;
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
            do{
                if (!notread) {
                    message = new ParametersFromNetwork(1);
                    message.enable();

                    long finalEnd = end;
                    Thread t = new Thread(() -> {
                        try {
                            message.waitParametersReceivedGUI(finalEnd);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    t.start();

                    DoubleObject responce= ((DoubleObject) Platform.enterNestedEventLoop(PAUSE_KEY));
                    check= responce.isRespo();
                    message= responce.getParame();



                    if (check) {
                        System.out.println("\n\nServer non ha dato risposta");
                        Network.disconnect();
                        currNode = otherPlayersLabel;
                        this.otherPlayersLabel.setText("...Server si è disconnesso, mi disconnetto...");
                        TimeUnit.SECONDS.sleep(5);
                        System.exit(0);
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

                                    do {

                                        message = new ParametersFromNetwork(1);
                                        message.enable();

                                        long finalEnd1 = end;
                                        Thread t = new Thread(() -> {
                                            try {
                                                message.waitParametersReceivedGUI(finalEnd1);
                                            } catch (InterruptedException e) {
                                                throw new RuntimeException(e);
                                            }
                                        });
                                        t.start();

                                        DoubleObject responce= ((DoubleObject) Platform.enterNestedEventLoop(PAUSE_KEY));
                                        check= responce.isRespo();
                                        message= responce.getParame();

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
                                    }
                                    while (!Objects.equals(tryreceivedClientModel.getTypeOfRequest(), "CONNECTTOEXISTINGGAME") && receivedClientModel.isGameStarted() && receivedClientModel.NotisKicked() && receivedClientModel.isResponse().equals(false) && receivedClientModel.getTypeOfRequest() != null && receivedClientModel.getClientIdentity() == myID && receivedClientModel.isPingMessage());


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
                end=System.currentTimeMillis()+ 40000L;
            }while (!isToReset && !notdone);

        }
}
