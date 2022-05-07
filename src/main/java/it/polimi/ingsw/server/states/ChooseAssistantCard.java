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

import java.util.ArrayList;

public class ChooseAssistantCard extends State {
    private Event cardsChoosen;
    private Model model;

    private ConnectionModel connectionModel;
    private Controller controller;

    private Gson json;
    private ServerController serverController;

    private ParametersFromNetwork message;

    public Event cardsChoosen() {
        return cardsChoosen;
    }
    public ChooseAssistantCard(ServerController serverController) {
        super("[Choose Assistant Card]");
        this.serverController = serverController;
        this.controller = serverController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        cardsChoosen= new Event("game created");
        cardsChoosen.setStateEventListener(controller);
        json = new Gson();
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        model = serverController.getModel();
        ArrayList alreadyChoosen = new ArrayList<AssistantCard>();
        // For each player
        for(int i=0; i<model.getNumberOfPlayers(); i++){
            // retrive the current player
            Player currentPlayer = model.getcurrentPlayer();
            // retrive data of the current player
            ClientModel currentPlayerData = connectionModel.findPlayer(currentPlayer.getNickname());
            // put the deck in the data and send it over the network
            currentPlayerData.setDeck(currentPlayer.getAvailableCards().getCardsList());
            currentPlayerData.setResponse(false); // è una richiesta non una risposta
            currentPlayerData.setGameStarded(true); // faccio partire il gioco
            currentPlayerData.setTypeOfRequest("CHOOSEASSISTANTCARD");  // lato client avrà una nella CliView un metodo per gestire questa richiesta
            Network.send(json.toJson(currentPlayerData));

            message = new ParametersFromNetwork(1);
            message.enable();
            while(!message.parametersReceived()){
                // il client non ha ancora scelto la carta assistente
            }


        }
        return super.entryAction(cause);
    }
}
