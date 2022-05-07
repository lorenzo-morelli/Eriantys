package it.polimi.ingsw.server.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.server.ConnectionModel;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class AskHelloToPlayerOne extends State {

    private ParametersFromNetwork message;
    private ConnectionModel connectionModel;

    public AskHelloToPlayerOne(ConnectionModel connectionModel) {
        super("[Chiedo hello to the first client]");
        message = new ParametersFromNetwork(1);
        this.connectionModel = connectionModel;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {

        ClientModel datiPrimoClient;
        datiPrimoClient = connectionModel.getClientsInfo().get(0);

        // Compilo i campi per effettuare una richiesta
        datiPrimoClient.setResponse(false); // è una richiesta non una risposta
        datiPrimoClient.setGameStarded(true); // faccio partire il gioco
        datiPrimoClient.setTypeOfRequest("HELLO");  // lato client avrà una nella CliView un metodo per gestire questa richiesta
        datiPrimoClient.setRequestDescription("the_user_inserted_hello");



        // Invio la richiesta
        Gson json = new Gson();
        Network.send(json.toJson(datiPrimoClient));

        // Attendo la risposta
        message.enable();
        while (!message.parametersReceived()){
            // non ho ancora ricevuto la risposta
        }
        return super.entryAction(cause);
    }
}
