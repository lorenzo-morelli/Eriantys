package it.polimi.ingsw.server.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.server.ConnectionModel;
import it.polimi.ingsw.server.ServerController;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AskForTeamMate extends State {
    private Event teamMateChoosen;
    private Model model;
    private ConnectionModel connectionModel;
    private Controller controller;

    private Gson json;
    private ServerController serverController;

    private ParametersFromNetwork message;

    public Event teamMateChoosen() {
        return teamMateChoosen;
    }

    public AskForTeamMate(ServerController serverController) {
        super("[Ask for team mate]");
        this.serverController = serverController;
        this.controller = serverController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        teamMateChoosen = new Event("game created");
        teamMateChoosen.setStateEventListener(controller);
        json = new Gson();
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        model = serverController.getModel();

        // retrive the current player
        Player currentPlayer = model.getcurrentPlayer();
        // retrive data of the current player
        ClientModel currentPlayerData = connectionModel.findPlayer(currentPlayer.getNickname());
        // put the deck in the data and send it over the network
        currentPlayerData.setServermodel(model);
        currentPlayerData.setResponse(false); // è una richiesta non una risposta
        currentPlayerData.setTypeOfRequest("TEAMMATE");  // lato client avrà una nella CliView un metodo per gestire questa richiesta
        Network.send(json.toJson(currentPlayerData));

        boolean responseReceived = false;

        while (!responseReceived) {
            message = new ParametersFromNetwork(1);
            message.enable();
            while (!message.parametersReceived()) {
                // il client non ha ancora scelto la carta assistente
            }
            if (json.fromJson(message.getParameter(0), ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity()) {
                responseReceived = true;
            }
        }
        // Ho ricevuto la risposta, ora devo aggiornare il model
        serverController.setModel(json.fromJson(message.getParameter(0), ClientModel.class).getServermodel());
        teamMateChoosen.fireStateEvent();

        return super.entryAction(cause);
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        super.exitAction(cause);
    }


}
