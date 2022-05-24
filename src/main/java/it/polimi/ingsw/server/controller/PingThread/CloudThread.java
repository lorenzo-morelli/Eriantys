package it.polimi.ingsw.server.controller.PingThread;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.states.CloudPhase;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;

public class CloudThread extends Thread {
    private final CloudPhase phase;
    private final ClientModel CurrentPlayerData;
    private final Gson json;

    public CloudThread(CloudPhase phase, ClientModel CurrentPlayerData) {
        this.phase = phase;
        this.CurrentPlayerData=CurrentPlayerData;
        json=new Gson();
    }

    public void run() {
        while (!phase.getMessage().parametersReceived() || json.fromJson(phase.getMessage().getParameter(0), ClientModel.class).isPingMessage()) {
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                return;
            }
            System.out.println("ping sended");
            CurrentPlayerData.setResponse(false); // è una richiesta non una risposta// lato client avrà una nella CliView un metodo per gestire questa richiesta
            CurrentPlayerData.setPingMessage(true);
            try {
                Network.send(json.toJson(CurrentPlayerData));
            } catch (InterruptedException e) {
                return;
            }

            long start = System.currentTimeMillis();
            long end = start + 5 * 1000;
            ParametersFromNetwork pingmessage = new ParametersFromNetwork(1);
            pingmessage.enable();

            while (!pingmessage.parametersReceived() && System.currentTimeMillis() < end) {
            }
            synchronized (phase) {
                if (!pingmessage.parametersReceived()) {
                    phase.setDisconnected(true);
                    return;
                }
                if (!json.fromJson(pingmessage.getParameter(0), ClientModel.class).isPingMessage()) {
                    phase.setMessage(pingmessage);
                    phase.setFromPing(true);
                    return;
                }
            }
        }
    }
}
