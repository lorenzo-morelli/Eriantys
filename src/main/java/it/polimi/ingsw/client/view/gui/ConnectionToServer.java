package it.polimi.ingsw.client.view.gui;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.network.events.ResultOfWaiting;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.utils.network.events.ParametersFromNetwork.PAUSE_KEY;

/**
 * @author Ignazio Neto Dell'Acqua
 * @author Lorenzo Morelli
 */
public class ConnectionToServer {

    private final GuiView guiView = new GuiView();
    private final Gson gson = new Gson();
    private boolean isToReset = false;
    private boolean waitForFirst = true;
    private int myID;
    public static String myNickname;
    private ParametersFromNetwork message;
    private boolean notRead = false;

    public void connect(boolean first) {
        if (first) {
            Network.send(gson.toJson(this.guiView.getClientModel()));
        }
        myID = guiView.getClientModel().getClientIdentity();
        myNickname = guiView.getClientModel().getNickname();
        long start = System.currentTimeMillis();
        long end = start + 70 * 1000L;
        try {
            waiting(end);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * The wait state should be interpreted as a "command waiting" state from the server.
     * The client is therefore at rest and only 2 types of messages will be delivered from server:
     * requestToMe, a request for direct client interaction (for example: insert the assistant card),
     * requestToOthers, i.e. the server is requesting interaction with another client ,
     * and pings (sometimes pings are used as requestToMe message in order to recovery from lost messages).
     * @param end = timing for timeout -> when the timeout expires the game will be closed (timeout expires when the server doesn't give any response)
     */
    public void waiting(long end) throws InterruptedException {
        boolean notDone = false;
        boolean isFirst = true;
        String previousRequest = null;

        do {
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
                ResultOfWaiting response = ((ResultOfWaiting) Platform.enterNestedEventLoop(PAUSE_KEY));
                boolean check = response.isNotArrived();
                message = response.getMessage();
                if (check && waitForFirst) {
                    waiting(System.currentTimeMillis() + 40000L);
                    return;
                }
                if (check) {
                    Network.disconnect();
                    TimeUnit.SECONDS.sleep(5);
                    System.exit(0);
                }
            }
            notRead = false;
            ClientModel clientModel = gson.fromJson(message.getParameter(0), ClientModel.class);

            if (clientModel.getReply()) {
                if (!Objects.equals(clientModel.getTypeOfRequest(), "CONNECTTOEXISTINGGAME")) {
                    if (Network.disconnectedClient()) {
                        Network.disconnect();
                        isToReset = true;
                    }
                    if (clientModel.isGameStarted() && clientModel.isNotKicked()) {
                        waitForFirst = false;
                        if (!clientModel.isResponse() && clientModel.getTypeOfRequest() != null) {
                            if (clientModel.getClientIdentity() == myID) {
                                try {
                                    if (!isFirst && Objects.equals(previousRequest, clientModel.getTypeOfRequest())) {
                                        if (Objects.equals("CHOOSEWHERETOMOVESTUDENTS", clientModel.getTypeOfRequest()) || Objects.equals("CHOOSEWHERETOMOVEMOTHER", clientModel.getTypeOfRequest())) {
                                            guiView.setClientModel(clientModel);
                                            guiView.requestPing();
                                            guiView.requestToMe();
                                        } else {
                                            guiView.requestPing();
                                        }
                                    } else if (clientModel.isPingMessage()) {
                                        guiView.setClientModel(clientModel);
                                        guiView.requestPing();
                                        guiView.requestToMe();
                                        isFirst = false;
                                        previousRequest = guiView.getClientModel().getTypeOfRequest();
                                    } else {
                                        guiView.setClientModel(clientModel);
                                        guiView.requestToMe();
                                        isFirst = false;
                                        previousRequest = guiView.getClientModel().getTypeOfRequest();
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
                                    isFirst = true;
                                    guiView.setClientModel(clientModel);
                                    guiView.requestToOthers();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        if (!notDone && clientModel.getClientIdentity() != myID && clientModel.isResponse().equals(true) && clientModel.getTypeOfRequest() != null && !clientModel.isPingMessage()) {
                            guiView.setClientModel(clientModel);
                            guiView.response();
                        }
                    }
                }
            }
            end = System.currentTimeMillis() + 40000L;
        }
        while (!isToReset && !notDone);
    }
}
