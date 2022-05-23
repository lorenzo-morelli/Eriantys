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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class AssistantCardPhase extends State {
    private final Event cardsChoosen ,gameEnd;
    private final ConnectionModel connectionModel;

    private final Gson json;
    private final ServerController serverController;

    private ParametersFromNetwork message;
    private final Event reset = new ClientDisconnection();
    private boolean disconnected,fromPing;
    public Event gameEnd() {
        return gameEnd;
    }
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
        gameEnd = new Event("end phase");
        gameEnd.setStateEventListener(controller);
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
            if(!currentPlayer.isDisconnected()) {
                ClientModel currentPlayerData;
                boolean lowpriority = false;
                disconnected = false;
                fromPing = false;
                // retrive data of the current player
                currentPlayerData = connectionModel.findPlayer(currentPlayer.getNickname());
                List<AssistantCard> canbBeChooesed = new ArrayList<>(currentPlayer.getAvailableCards().getCardsList());
                if (alreadyChooseds.containsAll(canbBeChooesed)) {
                    lowpriority = true;
                } else {
                    canbBeChooesed.removeAll(alreadyChooseds);
                }
                // put the deck in the data and send it over the network
                //System.out.println(model.toString("fer","ciao"));
                currentPlayerData.setDeck(canbBeChooesed);
                currentPlayerData.setResponse(false); // è una richiesta non una risposta
                currentPlayerData.setTypeOfRequest("CHOOSEASSISTANTCARD");  // lato client avrà una nella CliView un metodo per gestire questa richiesta
                currentPlayerData.setPingMessage(false);
                currentPlayerData.setServermodel(model);
                boolean checkDisc = Network.send(json.toJson(currentPlayerData));
                if (checkDisc) {
                    Thread ping = new AssistantCardThread(this, currentPlayerData);
                    ping.start();

                    boolean responseReceived = false;
                    while (!responseReceived) {
                        synchronized (this) {
                            if (!fromPing) {
                                message = new ParametersFromNetwork(1);
                                message.enable();
                            }
                        }
                        while (!message.parametersReceived()) {
                            if (disconnected) {
                                break;
                            }
                        }
                        synchronized (this) {
                            if (disconnected || (json.fromJson(message.getParameter(0), ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity() && !json.fromJson(message.getParameter(0), ClientModel.class).isPingMessage())) {
                                responseReceived = true;
                                if (disconnected) {
                                    currentPlayer.setDisconnected(true);
                                    currentPlayer.setChoosedCard(currentPlayer.getAvailableCards().getCardsList().get(currentPlayer.getAvailableCards().getCardsList().size() - 1));
                                    model.getTable().getClouds().removeIf(cloud -> (cloud.getStudentsAccumulator().size() == 0));
                                    if (model.getTable().getClouds().size() == model.getNumberOfPlayers()) {
                                        model.getTable().getClouds().remove(0);
                                    }
                                } else {
                                    ping.interrupt();
                                }
                            }
                        }
                    }
                }
                if (!currentPlayer.isDisconnected()) {

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
                } else {
                    int check = 0;
                    if (model.getNumberOfPlayers() == 4) {
                        for (Team team : model.getTeams()) {
                            if (!team.getPlayer1().isDisconnected() || !team.getPlayer2().isDisconnected()) {
                                check++;
                            }
                        }
                    } else {
                        for (Player p : model.getPlayers()) {
                            if (!p.isDisconnected()) {
                                check++;
                            }
                        }
                    }
                    if (check <= 1) {
                        for (Player p : model.getPlayers()) {
                            ClientModel Data = connectionModel.findPlayer(p.getNickname());

                            Data.setTypeOfRequest("TRYTORECONNECT");
                            Data.setServermodel(model);
                            Data.setResponse(false);
                            Data.setPingMessage(false);

                            Network.send(json.toJson(Data));
                        }

                        TimeUnit.SECONDS.sleep(40);

                        check = 0;
                        if (model.getNumberOfPlayers() == 4) {
                            for (Team team : model.getTeams()) {
                                if (!team.getPlayer1().isDisconnected() || !team.getPlayer2().isDisconnected()) {
                                    check++;
                                }
                            }
                        } else {
                            for (Player p : model.getPlayers()) {
                                if (!p.isDisconnected()) {
                                    check++;
                                }
                            }
                        }
                        if (check <= 1) {
                            model.setDisconnection(true);
                            gameEnd().fireStateEvent();
                            return super.entryAction(cause);
                        }
                    }
                }
            }
            model.nextPlayer();
        }
        cardsChoosen.fireStateEvent();
        model.schedulePlayers();
        return super.entryAction(cause);
    }
    public void setDisconnected(boolean value){
        disconnected=value;
    }

    public void setMessage(ParametersFromNetwork message) {
        this.message = message;
    }

    public ParametersFromNetwork getMessage() {
        return message;
    }

    public void setFromPing(boolean fromPing) {
        this.fromPing = fromPing;
    }
}

class AssistantCardThread extends Thread {
    private final AssistantCardPhase phase;
    private final ClientModel CurrentPlayerData;
    private final Gson json;

    protected AssistantCardThread(AssistantCardPhase phase,ClientModel CurrentPlayerData) {
        this.phase = phase;
        this.CurrentPlayerData=CurrentPlayerData;
        json=new Gson();
    }

    public void run() {
        while (!phase.getMessage().parametersReceived() || json.fromJson(phase.getMessage().getParameter(0), ClientModel.class).isPingMessage()) {
            try {
                sleep(15000);
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

            long start = System.currentTimeMillis();
            long end = start + 10 * 1000;
            ParametersFromNetwork pingmessage = new ParametersFromNetwork(1);
            pingmessage.enable();

            while (!pingmessage.parametersReceived() && System.currentTimeMillis() < end) {
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
