package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enums.GameMode;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AssistantCardPhase extends State {
    private Event cardsChoosen,gameEnd;
    private Model model;

    private ConnectionModel connectionModel;
    private Controller controller;

    private Gson json;
    private ServerController serverController;

    private ParametersFromNetwork message;

    public Event cardsChoosen() {
        return cardsChoosen;
    }
    public Event gameEnd() {
        return gameEnd;
    }
    public AssistantCardPhase(ServerController serverController) {
        super("[Choose Assistant Card]");
        this.serverController = serverController;
        this.controller = serverController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        cardsChoosen= new Event("game created");
        cardsChoosen.setStateEventListener(controller);
        gameEnd = new Event("end phase");
        gameEnd.setStateEventListener(controller);
        json = new Gson();
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        model = serverController.getModel();
        ArrayList<AssistantCard> alreadyChooseds=new ArrayList<>();
        model.nextTurn();
        // For each player
        for(int i=0; i<model.getNumberOfPlayers(); i++){
            // retrive the current player
            Player currentPlayer = model.getcurrentPlayer();
            // retrive data of the current player
            ClientModel currentPlayerData = connectionModel.findPlayer(currentPlayer.getNickname());
            List<AssistantCard> canbBeChooesed = new ArrayList<>(currentPlayer.getAvailableCards().getCardsList());
            boolean lowpriority=false;
            if(alreadyChooseds.containsAll(canbBeChooesed)){
                lowpriority=true;
            }
            else {
                canbBeChooesed.removeAll(alreadyChooseds);
            }
            // put the deck in the data and send it over the network
            //System.out.println(model.toString("fer","ciao"));
            currentPlayerData.setDeck(canbBeChooesed);
            currentPlayerData.setResponse(false); // è una richiesta non una risposta
            currentPlayerData.setTypeOfRequest("CHOOSEASSISTANTCARD");  // lato client avrà una nella CliView un metodo per gestire questa richiesta
            currentPlayerData.setServermodel(model); // questa cosa fa casini in EXPERT
            Network.send(json.toJson(currentPlayerData));

            boolean responseReceived = false;
            while (!responseReceived) {
                message = new ParametersFromNetwork(1);
                message.enable();
                while (!message.parametersReceived()) {
                    // il client non ha ancora scelto la carta assistente
                }
                if(json.fromJson(message.getParameter(0),ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity()){
                    responseReceived = true;
                }
            }

            AssistantCard choosen=null;
            currentPlayerData = json.fromJson(message.getParameter(0),ClientModel.class);
            // ricevo un campo json e lo converto in AssistantCard
            for(int j=0; j<currentPlayer.getAvailableCards().getCardsList().size();j++) {
                if (currentPlayerData.getCardChoosedValue() == currentPlayer.getAvailableCards().getCardsList().get(j).getValues()) {
                    choosen = currentPlayer.getAvailableCards().getCardsList().get(j);
                }
            }

            // il controllo sul fatto che l'utente scelga una carta appartenente a quelle presenti in availableCars
            // viene svolto direttamente dal client in CliView
            if(lowpriority) {
                    choosen.lowPriority();
            }

            alreadyChooseds.add(choosen);
            boolean checkEndCondition = currentPlayer.setChoosedCard(choosen);

            if (checkEndCondition){
                gameEnd().fireStateEvent();
                return super.entryAction(cause);
            }
            model.nextPlayer();

        }
        cardsChoosen.fireStateEvent();
        return super.entryAction(cause);
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        model.schedulePlayers();
        super.exitAction(cause);
    }
}
