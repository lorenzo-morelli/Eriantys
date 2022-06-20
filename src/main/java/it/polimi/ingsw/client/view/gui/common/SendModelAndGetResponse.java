package it.polimi.ingsw.client.view.gui.common;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.other.DoubleObject;
import javafx.application.Platform;

import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.polimi.ingsw.client.view.gui.Lobby.PAUSE_KEY;

public class SendModelAndGetResponse {

    public static ClientModel sendAndGetModel(ClientModel clientModel) throws InterruptedException {
        ParametersFromNetwork response = new ParametersFromNetwork(1);
        Gson gson = new Gson();

        long start = System.currentTimeMillis();
        long end = start + 15 * 1000L;

        TimeUnit.MILLISECONDS.sleep(500);

        Network.send(gson.toJson(clientModel));

        boolean responseReceived = false;

        while (!responseReceived) {

            response = new ParametersFromNetwork(1);
            response.enable();


            ParametersFromNetwork finalResponse = response;

Thread t = new Thread(() -> {
                try {
                    finalResponse.waitParametersReceivedGUI(end);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            t.start();


            boolean check = ((DoubleObject)Platform.enterNestedEventLoop(PAUSE_KEY)).isRespo();

            if(check){
                return null;
            }

            if (gson.fromJson(response.getParameter(0), ClientModel.class).getClientIdentity() == clientModel.getClientIdentity()) {
                responseReceived = true;
            }
        }
        return gson.fromJson(response.getParameter(0), ClientModel.class);
    }
}
