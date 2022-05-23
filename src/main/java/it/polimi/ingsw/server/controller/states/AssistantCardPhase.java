package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.controller.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.ServerController;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AssistantCardPhase extends State {
    private final Event cardsChoosen;
    private final ConnectionModel connectionModel;

    private final Gson json;
    private final ServerController serverController;

    private ParametersFromNetwork message;
    private final Event reset = new ClientDisconnection();

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
    }

    public Event getReset() {
        return reset;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        Model model = serverController.getModel();
        ArrayList<AssistantCard> alreadyChooseds=new ArrayList<>();
        model.nextTurn();
        // For each player
        for(int i = 0; i< model.getNumberOfPlayers(); i++){
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
            Thread t= new Thread(){
                public void run() {
                    boolean pingmessagereceived = true;
                    while (pingmessagereceived) {
                        ParametersFromNetwork pingmessage = new ParametersFromNetwork(1);
                        pingmessage.enable();
                        try {
                            TimeUnit.SECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        if( pingmessage.parametersReceived()){
                            Gson json = new Gson();
                            if(     json.fromJson(pingmessage.getParameter(0),ClientModel.class).getTypeOfRequest() != null &&
                                    json.fromJson(pingmessage.getParameter(0),ClientModel.class).getTypeOfRequest().equals("PING")){
                                pingmessagereceived = true;
                            }
                            else pingmessagereceived= false;
                        }
                        else pingmessagereceived = false;
                        if (!pingmessagereceived){
                            Network.setDisconnectedClient(true);
                        }
                    }
                }
            };
            t.start();

            boolean responseReceived = false;
            while (!responseReceived && !Network.disconnectedClient()) {
                message = new ParametersFromNetwork(1);
                message.enable();
                while (!message.parametersReceived() && !Network.disconnectedClient()) {}
                if(     !Network.disconnectedClient() &&
                        json.fromJson(message.getParameter(0),ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity() &&
                        !json.fromJson(message.getParameter(0),ClientModel.class).getTypeOfRequest().equals("PING") ){
                    responseReceived = true;
                }
            }
            t.stop();


            if(!Network.disconnectedClient()) {

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
            model.nextPlayer();

        }
        cardsChoosen.fireStateEvent();
        model.schedulePlayers();
        return super.entryAction(cause);
    }

}
