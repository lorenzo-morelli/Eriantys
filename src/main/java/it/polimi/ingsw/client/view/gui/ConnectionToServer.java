package it.polimi.ingsw.client.view.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.other.DoubleObject;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.utils.network.events.ParametersFromNetwork.PAUSE_KEY;

public class ConnectionToServer {
    private final GUI gui = new GUI();
    private final Gson gson = new Gson();
    private boolean isToReset = false;
    private boolean waitForFirst = true;
    private int myID;
    private ParametersFromNetwork message;
    private boolean notRead = false;

    public void connect(boolean first) throws InterruptedException {
        if (first) {
            Network.send(gson.toJson(this.gui.getClientModel()));
        }
        myID = gui.getClientModel().getClientIdentity();
        long start = System.currentTimeMillis();
        long end = start + 70 * 1000L;
        try {
            waiting(end);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waiting(long end) throws InterruptedException {
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
                DoubleObject response = ((DoubleObject) Platform.enterNestedEventLoop(PAUSE_KEY));
                boolean check = response.isResp();
                message = response.getParam();
                if (check && waitForFirst) {
                    waiting(System.currentTimeMillis() + 40000L);
                    return;
                }
                if (check) {
                    System.out.println("\n\nServer non ha dato risposta");
                    Network.disconnect();
                    TimeUnit.SECONDS.sleep(5);
                    System.exit(0);
                }
            }
            notRead = false;
            ClientModel clientModel = gson.fromJson(message.getParameter(0), ClientModel.class);
            if (!Objects.equals(clientModel.getTypeOfRequest(), "CONNECTTOEXISTINGGAME") && !clientModel.isDoNotResponce()) {
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
                                    //gui.requestPing();
                                } else {
                                    gui.setClientModel(clientModel);
                                    gui.requestToMe();
                                    Thread t = new Thread(gui::requestPing);
                                    t.start();
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
                                gui.stopPing();
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
