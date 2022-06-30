package it.polimi.ingsw.server.controller.pingThread;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.states.MotherPhase;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;

import java.util.ConcurrentModificationException;


/**
 * Thread for Mother Phase pings
 * @author Ignazio Neto Dell'Acqua
 */
public class MotherThread extends Thread {
    private final MotherPhase phase;
    private final ClientModel CurrentPlayerData;
    private final Gson json;

    public MotherThread(MotherPhase phase, ClientModel CurrentPlayerData) {
        this.phase = phase;
        this.CurrentPlayerData = CurrentPlayerData;
        json = new Gson();
    }

    /**
     * This method is used to send and receive ping during the Mother Phase in order to
     * manage the clients disconnection
     */
    public synchronized void run() {
        while (!phase.getMessage().parametersReceived() || json.fromJson(phase.getMessage().getParameter(0), ClientModel.class).isPingMessage()) {
            try {
                sleeping();
            } catch (InterruptedException e) {
                return;
            }
            System.out.println("ping sent");
            CurrentPlayerData.setResponse(false); // è una richiesta non una risposta// lato client avrà una nella CliView un metodo per gestire questa richiesta
            CurrentPlayerData.setPingMessage(true);
            try {
                Network.send(json.toJson(CurrentPlayerData));
            } catch (ConcurrentModificationException e) {
                Network.send(json.toJson(CurrentPlayerData));
            }


            ParametersFromNetwork ping_message = new ParametersFromNetwork(1);
            ping_message.enable();

            try {
                ping_message.waitParametersReceived(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (phase) {
                if (!ping_message.parametersReceived()) {
                    phase.setDisconnected(true);
                    return;
                }
                if (!json.fromJson(ping_message.getParameter(0), ClientModel.class).isPingMessage()) {
                    phase.setMessage(ping_message);
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
