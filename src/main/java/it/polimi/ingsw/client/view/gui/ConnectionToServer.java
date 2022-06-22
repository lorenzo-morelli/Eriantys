package it.polimi.ingsw.client.view.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.other.DoubleObject;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
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
        boolean isfirst= true;
        String previousRequest=null;

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

            if(clientModel.Reply()) {
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
                                    if (!isfirst && Objects.equals(previousRequest, clientModel.getTypeOfRequest())) {
                                        if(Objects.equals("CHOOSEWHERETOMOVESTUDENTS", clientModel.getTypeOfRequest()) || Objects.equals("CHOOSEWHERETOMOVEMOTHER", clientModel.getTypeOfRequest())){
                                            gui.setClientModel(clientModel);
                                            System.out.println("request to me done with pings");
                                            gui.requestPing();
                                            gui.requestToMe();
                                        }
                                        else {
                                            System.out.println("ping");
                                            gui.requestPing();
                                        }
                                    } else if(clientModel.isPingMessage()) {
                                        gui.setClientModel(clientModel);
                                        System.out.println("request to me done with pings");
                                        gui.requestPing();
                                        gui.requestToMe();
                                        isfirst=false;
                                        previousRequest=gui.getClientModel().getTypeOfRequest();
                                    }
                                    else {
                                        gui.setClientModel(clientModel);
                                        System.out.println("request to me");
                                        gui.requestToMe();
                                        isfirst=false;
                                        previousRequest=gui.getClientModel().getTypeOfRequest();
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
                                    isfirst=true;
                                    gui.setClientModel(clientModel);
                                    gui.requestToOthers();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
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
