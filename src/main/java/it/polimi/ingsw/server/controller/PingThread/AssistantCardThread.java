package it.polimi.ingsw.server.controller.PingThread;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.states.AssistantCardPhase;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;

import java.util.concurrent.TimeUnit;

public class AssistantCardThread extends Thread {
    private final AssistantCardPhase phase;
    private final ClientModel CurrentPlayerData;
    private final Gson json;

    public AssistantCardThread(AssistantCardPhase phase, ClientModel CurrentPlayerData) {
        this.phase = phase;
        this.CurrentPlayerData=CurrentPlayerData;
        json=new Gson();
    }

    public synchronized void run() {
        while (!phase.getMessage().parametersReceived() || json.fromJson(phase.getMessage().getParameter(0), ClientModel.class).isPingMessage()) {
            try {
                TimeUnit.MILLISECONDS.sleep(10000);
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

            ParametersFromNetwork pingmessage = new ParametersFromNetwork(1);
            pingmessage.enable();

            try {
                pingmessage.waitParametersReceived(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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
