package it.polimi.ingsw.utils.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.LinkOption;
import java.util.Objects;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;


public class SetupGame implements Initializable {
    private final GUI gui = new GUI();
    private final Gson gson = new Gson();
    private int connectedPlayers;
    private ClientModel receivedClientModel;
    private boolean isToReset;
    private int myID;

    ParametersFromNetwork message;

    private boolean notdone=false,notread=false;

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
            boolean responseReceived = false;
            while (!responseReceived) {
                Network.send(gson.toJson(this.gui.getClientModel()));
                System.out.println("invio al server in attesa di ack...");
                ParametersFromNetwork ack = new ParametersFromNetwork(1);
                ack.enable();
                long start = System.currentTimeMillis();
                long end = start + 15 * 1000L;
                boolean check = ack.waitParametersReceived(5);
                if (check || System.currentTimeMillis() >= end) {
                    System.out.println("\n\nServer non ha dato risposta");
                    Network.disconnect();
                    System.exit(0);
                }

                if (gson.fromJson(ack.getParameter(0), ClientModel.class).getClientIdentity() == this.gui.getClientModel().getClientIdentity()) {
                    responseReceived = true;
                }
            }
            System.out.println("[Conferma ricevuta]");
            System.out.println("In attesa che gli altri giocatori si colleghino...");
            otherPlayersLabel.setText("...Waiting for other players to join the game...");

            myID=gui.getClientModel().getClientIdentity();
            waitings();
            }
        }
        public void waitings() throws InterruptedException {
             do {
                 if(!notread) {
                     message = new ParametersFromNetwork(1);
                     message.enable();
                     message.waitParametersReceived();
                 }
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
                                gui.requestToMe(otherPlayersLabel);

                                Thread t= new Thread(() -> {
                                    try {
                                        wait_pings(tryreceivedClientModel);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                t.start();
                                notdone=true;

                            } catch (InterruptedException | IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        if (!notdone && receivedClientModel.getClientIdentity() != myID && receivedClientModel.getTypeOfRequest() != null &&
                                !receivedClientModel.isPingMessage() &&
                                !receivedClientModel.getTypeOfRequest().equals("TRYTORECONNECT") &&
                                !receivedClientModel.getTypeOfRequest().equals("DISCONNECTION"))
                        {
                            try {
                                System.out.println("request to other");
                                gui.setClientModel(receivedClientModel);
                                gui.requestToOthers(otherPlayersLabel);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    // altrimenti il messaggio è una risposta di un altro client ad un server
    //                    else if (receivedClientModel.isResponse().equals(true) && receivedClientModel.getTypeOfRequest() != null) {
    //                        try {
    //                            System.out.println("response");
    //                            Thread t = new Thread() {
    //                                public synchronized void run() {
    //                                    while (true) {
    //                                        ParametersFromNetwork message = new ParametersFromNetwork(1);
    //                                        message.enable();
    //                                        try {
    //                                            message.waitParametersReceived();
    //                                        } catch (InterruptedException e) {
    //                                            throw new RuntimeException(e);
    //                                        }
    //
    //                                        System.out.println("ricevuto un ping...");
    //                                        ClientModel tryreceivedClientModel = gson.fromJson(message.getParameter(0), ClientModel.class);
    //
    //                                        if (!Objects.equals(tryreceivedClientModel.getTypeOfRequest(), "CONNECTTOEXISTINGGAME")) { //todo received.gettype.equals("connect")
    //
    //                                            receivedClientModel = tryreceivedClientModel;
    //
    //                                            if (receivedClientModel.isGameStarted() && receivedClientModel.NotisKicked()) {
    //                                                // Il messaggio è o una richiesta o una risposta
    //
    //                                                // se il messaggio non è una risposta di un client al server vuol dire che
    //                                                if (receivedClientModel.isResponse().equals(false) && receivedClientModel.getTypeOfRequest() != null) {
    //                                                    // il messaggio è una richiesta del server alla view di un client
    //
    //                                                    // se il messaggio è rivolto a me devo essere io a compiere l'azione
    //
    //
    //                                                }
    //                                            }
    //                                        }
    //                                    }
    //                                }
    //                            };
    //                            t.start();
    //                            gui.setClientModel(receivedClientModel);
    //                            gui.response();
    //                            wait();
    //                            t.interrupt();
    //                        } catch (InterruptedException | IOException e) {
    //                            throw new RuntimeException(e);
    //                        }
    //                    }
                }

            }
        }while (!isToReset && !notdone) ;
    }
        public synchronized void wait_pings(ClientModel tryreceivedClientModel) throws InterruptedException {
            while (!Objects.equals(tryreceivedClientModel.getTypeOfRequest(), "CONNECTTOEXISTINGGAME") && receivedClientModel.isGameStarted() && receivedClientModel.NotisKicked() && receivedClientModel.isResponse().equals(false) && receivedClientModel.getTypeOfRequest() != null && receivedClientModel.getClientIdentity() == myID) {
                message = new ParametersFromNetwork(1);
                message.enable();
                try {
                    message.waitParametersReceived();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("ricevuto un ping...");
                tryreceivedClientModel = gson.fromJson(message.getParameter(0), ClientModel.class);

                if (!Objects.equals(tryreceivedClientModel.getTypeOfRequest(), "CONNECTTOEXISTINGGAME")) { //todo received.gettype.equals("connect")

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
            notdone=false;
            notread=true;
            waitings();
        }
    }

