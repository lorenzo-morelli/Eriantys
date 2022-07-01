package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.view.cli.cliController.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.Cloud;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import static java.lang.Thread.sleep;

/**
 * Handles the creation of a new game (server side setup of the model)
 * @author Ignazio Neto Dell'Acqua
 * @author Fernando Morea
 */
public class Create2Or3PlayerGame extends State {
    private final Event gameCreated, fourPlayersGameCreated;
    private Model model;
    private final ConnectionModel connectionModel;
    private final ServerController serverController;

    public Create2Or3PlayerGame(ServerController serverController) {
        super("[Create game]");
        this.serverController = serverController;
        Controller controller = ServerController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        gameCreated = new Event("game created");
        Event reset = new ClientDisconnection();
        reset.setStateEventListener(controller);
        fourPlayersGameCreated = new Event("Four players game created");
        gameCreated.setStateEventListener(controller);
    }

    /**
     * Events caller
     * @return gameCreated event in order to trigger the fsm machine
     */
    public Event gameCreated() {
        return gameCreated;
    }

    /**
     * Events caller
     * @return fourPlayersGameCreated event in order to trigger the fsm machine
     */
    public Event fourPlayersGameCreated() {
        return fourPlayersGameCreated;
    }

    public Model getModel() {
        return model;
    }

    /**
     * Create the model (3/2 players only, for the 4 players one the controller use the @Create4PlayerGame phase) passing the numberOfPlayer and gameMode chosen , add all the player on it
     * @param cause the event that caused the controller transition in this state
     * @return null event
     * @throws Exception input output or network related exceptions
     */
    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        model = new Model(connectionModel.getNumOfPlayers(), connectionModel.getGameMode(), false);
        int i = 0;
        for (ClientModel c : connectionModel.getClientsInfo()) {
            model.getPlayers().add(new Player(connectionModel.getClientsInfo().get(i).getNickname(), connectionModel.getClientsInfo().get(i).getMyIp(), model, false));
            c.setGameStarted(true);
            c.setAmFirst(false);
            i++;
        }
        model.randomSchedulePlayers();
        serverController.setModel(model);

        Thread t = new Thread() {
            /**
             * This method make the server capable to manage the connection of new player during the game:
             * If someone was disconnected previously puts the new player into the game (if it has a valid nickname)
             * If the game is full, ignore the player.
             */
            public synchronized void run() {
                while (true) {
                    if (connectionModel.isCloseThread()) {
                        connectionModel.setCloseThread(false);
                        return;
                    }

                    ParametersFromNetwork message = new ParametersFromNetwork(1);
                    message.enable();
                    try {
                        message.waitParametersReceived();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Gson json = new Gson();
                    ClientModel receivedClientModel = json.fromJson(message.getParameter(0), ClientModel.class);

                    if (receivedClientModel.getAmFirst() == null && !connectionModel.isCloseThread()) {
                        boolean check = false;

                        for (Player p : getModel().getPlayers()) {
                            if (p.isDisconnected()) {
                                check = true;
                                break;
                            }
                        }

                        if (check) {
                            boolean check2 = true;

                            for (Player p : getModel().getPlayers()) {
                                if (!p.isDisconnected() && p.getNickname().equals(receivedClientModel.getNickname())) {
                                    check2 = false;
                                    break;
                                }
                            }

                            if (check2) {
                                try {
                                    sleeping(6000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                for (Player p : getModel().getPlayers()) {
                                    if (p.isDisconnected()) {
                                        ClientModel target = connectionModel.findPlayer(p.getNickname());
                                        p.setNickname(receivedClientModel.getNickname());
                                        p.setDisconnected(false);
                                        receivedClientModel.setAmFirst(false);
                                        receivedClientModel.setKicked(false);
                                        receivedClientModel.setGameStarted(true);
                                        receivedClientModel.setTypeOfRequest("CONNECTTOEXISTINGGAME");
                                        connectionModel.change(target, receivedClientModel);
                                        Network.send(json.toJson(receivedClientModel));
                                        model.setDisconnection(false);
                                        model.getTable().getClouds().add(new Cloud(model.getNumberOfPlayers()));
                                        if(!p.isSkipClouds()) {
                                            model.getTable().getClouds().get(model.getTable().getClouds().size() - 1).charge(model.getTable().getBag());
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    try {
                        sleeping(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        t.start();
        gameCreated.fireStateEvent();
        return super.entryAction(cause);
    }
    private void sleeping(int i) throws InterruptedException {
        sleep(i);
    }

}
