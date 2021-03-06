package it.polimi.ingsw.server.controller.pingThread;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.states.AssistantCardPhase;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;

import java.util.ConcurrentModificationException;

/**
 * Thread for Assistant Card Phase pings
 * @author Ignazio Neto Dell'Acqua
 */
public class AssistantCardThread extends Thread {
    private final AssistantCardPhase phase;
    private final ClientModel CurrentPlayerData;
    private final Gson json;

    public AssistantCardThread(AssistantCardPhase phase, ClientModel CurrentPlayerData) {
        this.phase = phase;
        this.CurrentPlayerData = CurrentPlayerData;
        json = new Gson();
    }

    /**
     * This method is used to send and receive ping during the Assistant Card Phase in order to
     * manage the clients disconnection
     */
    public synchronized void run() {
        while (phase.getMessage() == null || !phase.getMessage().parametersReceived() || json.fromJson(phase.getMessage().getParameter(0), ClientModel.class).isPingMessage()) {
            try {
                sleeping();
            } catch (InterruptedException e) {
                return;
            }
            System.out.println("ping sent");
            CurrentPlayerData.setResponse(false);
            CurrentPlayerData.setPingMessage(true);
            try {
                Network.send(json.toJson(CurrentPlayerData));
            } catch (ConcurrentModificationException e) {
                Network.send(json.toJson(CurrentPlayerData));
            }

            ParametersFromNetwork pingMessage = new ParametersFromNetwork(1);
            pingMessage.enable();

            try {
                pingMessage.waitParametersReceived(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (phase) {
                if (!pingMessage.parametersReceived()) {
                    phase.setDisconnected(true);
                    return;
                }
                if (!json.fromJson(pingMessage.getParameter(0), ClientModel.class).isPingMessage()) {
                    phase.setMessage(pingMessage);
                    phase.setFromPing(true);
                    return;
                }
            }
        }
    }
    private void sleeping() throws InterruptedException {
        sleep(10000);
    }
}
