package it.polimi.ingsw.server.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.server.ConnectionModel;
import it.polimi.ingsw.server.ServerController;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Deck;
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
import java.util.concurrent.TimeUnit;

public class AssistantCardPhase extends State {
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
    public AssistantCardPhase(ServerController serverController) {
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
        ArrayList<AssistantCard> alreadyChooseds=new ArrayList<>();
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
            currentPlayerData.setDeck(canbBeChooesed);
            currentPlayerData.setResponse(false); // è una richiesta non una risposta
            currentPlayerData.setGameStarded(true); // faccio partire il gioco
            currentPlayerData.setTypeOfRequest("CHOOSEASSISTANTCARD");  // lato client avrà una nella CliView un metodo per gestire questa richiesta
            Network.send(json.toJson(currentPlayerData));

            message = new ParametersFromNetwork(1);
            message.enable();
            while(!message.parametersReceived()){
                TimeUnit.SECONDS.sleep(1);
                // il client non ha ancora scelto la carta assistente
            }
            System.out.println(message.getParameter(0));
            // ricevo un campo json e lo converto in AssistantCard
            AssistantCard choosen = currentPlayerData.getDeck().get(Integer.parseInt(json.fromJson(message.getParameter(0),ClientModel.class).getFromTerminal().get(0)));

            // il controllo sul fatto che l'utente scelga una carta appartenente a quelle presenti in availableCars
            // viene svolto direttamente dal client in CliView
            if(lowpriority) {
                    choosen.lowPriority();
            }

            alreadyChooseds.add(choosen);
            boolean checkEndCondition = currentPlayer.setChoosedCard(choosen);

            if (checkEndCondition){
                // TODO: vai alla ENDPHASE
            }
            model.nextPlayer();

            // Siccome il network ha latenza non nulla diamo il tempo a tutti i client di elaborare
            TimeUnit.SECONDS.sleep(1);

        }
        return super.entryAction(cause);
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        model.schedulePlayers();
        super.exitAction(cause);
    }
}
