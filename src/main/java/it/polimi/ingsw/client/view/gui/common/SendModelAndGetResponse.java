package it.polimi.ingsw.client.view.gui.common;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.network.events.ResultOfWaiting;
import javafx.application.Platform;

import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.utils.network.events.ParametersFromNetwork.PAUSE_KEY;

public class SendModelAndGetResponse {

    public static ClientModel sendAndGetModel(ClientModel clientModel) throws InterruptedException {
        ParametersFromNetwork response = new ParametersFromNetwork(1);
        Gson gson = new Gson();
        long start = System.currentTimeMillis();
        long end = start + 40 * 1000L;
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
            boolean check = ((ResultOfWaiting) Platform.enterNestedEventLoop(PAUSE_KEY)).isNotArrived();
            if (check) {
                return null;
            }
            if (response.getParameter(0)!=null && gson.fromJson(response.getParameter(0), ClientModel.class).getClientIdentity() == clientModel.getClientIdentity()) {
                responseReceived = true;
            }
        }
        return gson.fromJson(response.getParameter(0), ClientModel.class);
    }
}
