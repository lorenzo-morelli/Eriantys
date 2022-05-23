package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.controller.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Team;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import javax.management.timer.Timer;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AssistantCardPhase extends State {
    private final Event cardsChoosen;
    private final ConnectionModel connectionModel;

    private final Gson json;
    private final ServerController serverController;

    private ParametersFromNetwork message, pingmessage;
    private final Event reset = new ClientDisconnection();
    private Player currentPlayer;
    private static boolean disconnected;

    public Event cardsChoosen() {
        return cardsChoosen;
    }
    public AssistantCardPhase(ServerController serverController) {
        super("[Choose Assistant Card]");
        this.serverController = serverController;
        Controller controller = ServerController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        cardsChoosen= new Event("game created");
        cardsChoosen.setStateEventListener(controller);
        reset.setStateEventListener(controller);
        json = new Gson();
        message=null;
    }

    public Event getReset() {
        return reset;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        Model model = serverController.getModel();
        ArrayList<AssistantCard> alreadyChooseds=new ArrayList<>();
        model.nextTurn();
        disconnected=false;
        // For each player
        for(int i = 0; i< model.getNumberOfPlayers(); i++){
            // retrive the current player
            currentPlayer = model.getcurrentPlayer();
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
            currentPlayerData.setPingMessage(false);
            currentPlayerData.setServermodel(model);
            Network.send(json.toJson(currentPlayerData));

            ClientModel finalCurrentPlayerData = currentPlayerData;

            Thread ping = new Thread(() -> {
                while(!message.parametersReceived() || json.fromJson(message.getParameter(0), ClientModel.class).isPingMessage()) {
                    System.out.println("ping sended");
                    finalCurrentPlayerData.setResponse(false); // è una richiesta non una risposta// lato client avrà una nella CliView un metodo per gestire questa richiesta
                    finalCurrentPlayerData.setPingMessage(true);
                    try {
                        Network.send(json.toJson(finalCurrentPlayerData));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    long start = System.currentTimeMillis();
                    long end = start + 10 * 1000;
                    pingmessage = new ParametersFromNetwork(1);
                    pingmessage.enable();

                    while (!pingmessage.parametersReceived() && System.currentTimeMillis() < end) {
                    }
                    synchronized (this) {
                        if (!pingmessage.parametersReceived()) {
                            disconnected=true;
                            return;
                        }
                        if (!json.fromJson(pingmessage.getParameter(0), ClientModel.class).isPingMessage()) {
                            try {
                                message =(ParametersFromNetwork) pingmessage.clone();
                            } catch (CloneNotSupportedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            });
            ping.start();

            boolean responseReceived = false;
            while (!responseReceived) {
                synchronized (this) {
                    if (message == null || json.fromJson(message.getParameter(0), ClientModel.class).isPingMessage()) {
                        if(message!=null && message.parametersReceived() && json.fromJson(message.getParameter(0), ClientModel.class).isPingMessage()){
                            pingmessage= (ParametersFromNetwork)message.clone();
                        }
                        message = new ParametersFromNetwork(1);
                        message.enable();
                    }
                }
                while (!message.parametersReceived()) {
                    if(disconnected){
                        currentPlayer.setDisconnected(true);
                        currentPlayer.setChoosedCardforDisconnection(new AssistantCard(11, 1));
                        break;
                    }
                }
                synchronized (this) {
                    if (disconnected || (json.fromJson(message.getParameter(0), ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity() && !json.fromJson(message.getParameter(0), ClientModel.class).isPingMessage())) {
                        responseReceived = true;
                        if(disconnected){
                            currentPlayer.setDisconnected(true);
                            currentPlayer.setChoosedCardforDisconnection(new AssistantCard(11, 1));
                        }
                        else {
                            ping.interrupt();
                        }
                    }
                }
            }

            if(!currentPlayer.isDisconnected()) {

                AssistantCard choosen = null;
                currentPlayerData = json.fromJson(message.getParameter(0), ClientModel.class);
                // ricevo un campo json e lo converto in AssistantCard
                for (int j = 0; j < currentPlayer.getAvailableCards().getCardsList().size(); j++) {
                    if (currentPlayerData.getCardChoosedValue() == currentPlayer.getAvailableCards().getCardsList().get(j).getValues()) {
                        choosen = currentPlayer.getAvailableCards().getCardsList().get(j);
                    }
                }

                // il controllo sul fatto che l'utente scelga una carta appartenente a quelle presenti in availableCars
                // viene svolto direttamente dal client in CliView
                if (lowpriority) {
                    assert choosen != null;
                    choosen.lowPriority();
                }

                alreadyChooseds.add(choosen);
                boolean checkEndCondition = currentPlayer.setChoosedCard(choosen);

                if (checkEndCondition) {
                    model.setlastturn();
                }
            }
            else{
                int check=0;
                if(model.getNumberOfPlayers()==4){
                    for(Team team: model.getTeams()){
                        if(!team.getPlayer1().isDisconnected() || !team.getPlayer2().isDisconnected()){
                            check++;
                        }
                    }
                }
                else{
                    for(Player p: model.getPlayers()){
                        if(!p.isDisconnected()){
                            check++;
                        }
                    }
                }
                if(check<=1){
                    System.out.println("attendo 60 secondi in attesa di una riconnessione");
                    check=0;
                    if(model.getNumberOfPlayers()==4){
                        for(Team team: model.getTeams()){
                            if(!team.getPlayer1().isDisconnected() || !team.getPlayer2().isDisconnected()){
                                check++;
                            }
                        }
                    }
                    else{
                        for(Player p: model.getPlayers()){
                            if(!p.isDisconnected()){
                                check++;
                            }
                        }
                    }
                    if(check<=1){
                        System.out.println("END");
                    }
                }
            }
            model.nextPlayer();

        }
        cardsChoosen.fireStateEvent();
        model.schedulePlayers();
        return super.entryAction(cause);
    }
}

