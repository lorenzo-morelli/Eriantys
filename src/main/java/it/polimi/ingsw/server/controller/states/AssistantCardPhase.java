package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.view.cli.CLIcontroller.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.PingThread.AssistantCardThread;
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

/**
 * This server state implements the logic necessary to correctly handle
 * the Assistant card phase of the game.
 */
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

    /**
     * The main constructor of the Assistant Card Phase
     * @param serverController the main server controller
     */
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

    /**
     * For each player among those correctly connected to the game, in turn,
     * send a request to their view asking him to choose the assistant card
     * and wait for the answer.
     * Some sanity checks are made (in case of malicious client), as well as
     * special code to handle the advanced disconnection feature.
     * @param cause what caused the server controller to enter in this method
     * @return null event
     * @throws Exception IO errors and network related problems.
     */
    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        Model model = serverController.getModel();
        ArrayList<AssistantCard> alreadyChooseds=new ArrayList<>();
        model.nextTurn();

        // For each player

        for(int i = 0; i< model.getNumberOfPlayers(); i++){

            // retrive the current player

            Player currentPlayer = model.getcurrentPlayer();
            disconnected=false; //flag che indica se si disconnette durante questo turno
            fromPing=false; //risposta non proviene da ping

            //salto player se si è disconnesso precedentemente

            if(!currentPlayer.isDisconnected()) {

                //se non è disconnesso allora:

                ClientModel currentPlayerData;
                boolean lowpriority = false;
                fromPing = false;

                // retrive data of the current player

                currentPlayerData = connectionModel.findPlayer(currentPlayer.getNickname());

                //gestione delle carte che puo scegliere

                List<AssistantCard> canbBeChooesed = new ArrayList<>(currentPlayer.getAvailableCards().getCardsList());
                if (alreadyChooseds.containsAll(canbBeChooesed)) {
                    lowpriority = true;
                } else {
                    canbBeChooesed.removeAll(alreadyChooseds);
                }

                // put the deck in the data and send it over the network

                currentPlayerData.setDeck(canbBeChooesed);
                currentPlayerData.setResponse(false); // è una richiesta non una risposta
                currentPlayerData.setTypeOfRequest("CHOOSEASSISTANTCARD");  // lato client avrà una nella CliView un metodo per gestire questa richiesta
                currentPlayerData.setPingMessage(false);
                currentPlayerData.setServermodel(model);

                //invio e controllo che invio network sia fatto correttamente

                boolean checkDisc = Network.send(json.toJson(currentPlayerData));

                // se invio va a buon fine continua sennò salta il player

                if (checkDisc) {

                    //controllo ricezione risposta invio ping e settaggio del giocatore in disconnessione in caso di ricezione ping fallita

                    Thread ping = new AssistantCardThread(this, currentPlayerData);
                    ping.start();

                    boolean responseReceived = false;
                    while (!responseReceived) {
                        //System.out.println("another one");
                            if (!fromPing) {
                                message = new ParametersFromNetwork(1);
                                message.enable();
                            }
                        while (!message.parametersReceived()) {
                            //System.out.println("loop");
                            message.waitParametersReceived(5);
                            if (disconnected) {
                                break;
                            }
                        }
                            if (disconnected || (json.fromJson(message.getParameter(0), ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity() && !json.fromJson(message.getParameter(0), ClientModel.class).isPingMessage())) {
                                responseReceived = true;
                                if (disconnected) {
                                    currentPlayer.setDisconnected(true);
                                    alreadyChooseds.add(currentPlayer.getAvailableCards().getCardsList().get(currentPlayer.getAvailableCards().getCardsList().size() - 1));
                                    currentPlayer.setChoosedCard(canbBeChooesed.get(canbBeChooesed.size()-1));
                                    boolean check=true;
                                    for(int j=0;j<model.getTable().getClouds().size();j++) {
                                        if(model.getTable().getClouds().get(j).getStudentsAccumulator().size()==0)
                                        {
                                            model.getTable().getClouds().remove(j);
                                            check=false;
                                            break;
                                        }
                                    }
                                    if (check) {
                                        model.getTable().getClouds().remove(0);
                                    }
                                } else {
                                    ping.interrupt();
                                }
                            }
                            fromPing=false;
                    }

                }

                //codice effettivo della fase se non si è disconnesso

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

                }

                //codice per disconnessione di questo player durante questo turno

                else {
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
                        System.out.println("numero minimo di giocatori non disponibile, attendo 40 secondi in attesa che un altro giocatore si riconnette");
                        for (Player p : model.getPlayers()) {
                            if(!p.isDisconnected()) {
                                ClientModel Data = connectionModel.findPlayer(p.getNickname());

                                Data.setTypeOfRequest("TRYTORECONNECT");
                                Data.setServermodel(model);
                                Data.setResponse(false);
                                Data.setPingMessage(false);

                                Network.send(json.toJson(Data));
                                System.out.println("send to"+p.getNickname());
                            }
                        }

                        model.setDisconnection(true);
                        TimeUnit.MILLISECONDS.sleep(40000); //aspetto 40 secondi nella speranza che qualcuno si riconnetta


                        if (model.isDisconnection()) {
                            gameEnd().fireStateEvent();
                            return super.entryAction(cause);
                        }
                        System.out.println("un giocatore si è riconnesso, la partita può ricominciare");
                    }
                }
            }

            //prossimo player

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
