package it.polimi.ingsw.utils.common;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUI;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;

public class SendModelAndGetResponse {

    public static ClientModel sendAndGetModel(ClientModel clientModel) throws InterruptedException {
        ParametersFromNetwork response = new ParametersFromNetwork(1);
        Gson gson = new Gson();

        boolean responseReceived = false;
        while (!responseReceived) {
            Network.send(gson.toJson(clientModel));
            response = new ParametersFromNetwork(1);
            response.enable();
            while (!response.parametersReceived()) {
                System.out.println("still waiting...");
            }
            if (gson.fromJson(response.getParameter(0), ClientModel.class).getClientIdentity() == clientModel.getClientIdentity()) {
                responseReceived = true;
            }
        }
        return gson.fromJson(response.getParameter(0), ClientModel.class);
    }
}
