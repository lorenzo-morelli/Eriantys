package it.polimi.ingsw.client.view.gui.common;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.other.DoubleObject;
import javafx.application.Platform;

import java.util.concurrent.TimeUnit;

public class SendModelAndGetResponse {
    public static final Object object = new Object();

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


            boolean check = ((DoubleObject) Platform.enterNestedEventLoop(object)).isResp();

            if (check) {
                return null;
            }

            if (gson.fromJson(response.getParameter(0), ClientModel.class).getClientIdentity() == clientModel.getClientIdentity()) {
                responseReceived = true;
            }
        }
        return gson.fromJson(response.getParameter(0), ClientModel.class);
    }
}