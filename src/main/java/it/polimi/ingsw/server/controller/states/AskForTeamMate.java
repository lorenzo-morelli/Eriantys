package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.view.cli.CLIcontroller.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.Cloud;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Team;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

/**
 * This class implements the state of the server where the user has to choose his
 * teammate, to correctly create the 4 players game
 */

public class AskForTeamMate extends State {
    private final Event teamMateChosen;
    private Model model;
    private final ConnectionModel connectionModel;

    private final Gson json;
    private final ServerController serverController;

    private ParametersFromNetwork message;

    /**
     * Events callers
     * @return different events in order to change to different phase
     */
    public Event teamMateChosen() {
        return teamMateChosen;
    }

    /**
     * Constructor of the Ask teammate server state
     * @param serverController the main server controller
     */
    public AskForTeamMate(ServerController serverController) {
        super("[Ask for team mate]");
        this.serverController = serverController;
        Controller controller = ServerController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        teamMateChosen = new Event("game created");
        Event reset = new ClientDisconnection();
        reset.setStateEventListener(controller);
        teamMateChosen.setStateEventListener(controller);
        json = new Gson();
    }

    /**
     * Overrides the entry action of the state abstract class,
     * This method make a request to the client view to ask the client to
     * choose his teammate, after that creates the 4 players game mode
     * @param cause the cause that generated the transition in this state
     * @return null event
     * @throws Exception caused by threads/network/IO errors
     */
    @Override
    public IEvent entryAction(IEvent cause) throws Exception {

        ClientModel currentPlayerData = connectionModel.getClientsInfo().get(0);
        for(ClientModel c : connectionModel.getClientsInfo()){
            currentPlayerData.getNicknames().add(c.getNickname());
            c.setGameStarted(true);
        }
        currentPlayerData.getNicknames().remove(currentPlayerData.getNickname());
        currentPlayerData.setResponse(false);
        currentPlayerData.setTypeOfRequest("TEAMMATE");
        Network.send(json.toJson(currentPlayerData));
        System.out.println("sent team request");

        boolean responseReceived = false;

        while (!responseReceived) {
            message = new ParametersFromNetwork(1);
            message.enable();
            message.waitParametersReceived();
            if (json.fromJson(message.getParameter(0), ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity()) {
                responseReceived = true;
            }
        }

        ClientModel received = json.fromJson(message.getParameter(0), ClientModel.class);

        model = new Model(connectionModel.getNumOfPlayers(), connectionModel.getGameMode(),false);
        model.getPlayers().add(new Player(received.getNicknames().get(3), connectionModel.findPlayer(received.getNicknames().get(3)).getMyIp(),1, model,false));
        model.getPlayers().add(new Player(received.getNicknames().get(2), connectionModel.findPlayer(received.getNicknames().get(2)).getMyIp(),1, model,false));
        model.getPlayers().add(new Player(received.getNicknames().get(1), connectionModel.findPlayer(received.getNicknames().get(1)).getMyIp(),2, model,false));
        model.getPlayers().add(new Player(received.getNicknames().get(0), connectionModel.findPlayer(received.getNicknames().get(0)).getMyIp(),2, model,false));
        model.randomschedulePlayers();


        Thread t= new Thread(){

            /**
             * This method make the server capable to manage the connection of new player during the game:
             * If someone was disconnected previously puts the new player into the game (if it has a valid nickname)
             * If the game is full, ignore the player.
             */
            public synchronized void run() {
                while (true) {
                    if(connectionModel.isCloseThread()){
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

                    if(receivedClientModel.getAmIfirst()==null && !connectionModel.isCloseThread()) {
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
                                for (Team team : getModel().getTeams()) {
                                    if (team.getPlayer1().isDisconnected()) {
                                        ClientModel target = connectionModel.findPlayer(team.getPlayer1().getNickname());
                                        team.getPlayer1().setNickname(receivedClientModel.getNickname());
                                        team.getPlayer1().setDisconnected(false);
                                        receivedClientModel.setAmIfirst(false);
                                        receivedClientModel.setKicked(false);
                                        receivedClientModel.setGameStarted(true);
                                        receivedClientModel.setTypeOfRequest("CONNECTTOEXISTINGGAME");
                                        connectionModel.change(target, receivedClientModel);
                                        try {
                                            Network.send(json.toJson(receivedClientModel));
                                        } catch (InterruptedException e) {
                                            throw new RuntimeException(e);
                                        }
                                        model.setDisconnection(false);
                                        model.getTable().getClouds().add(new Cloud(model.getNumberOfPlayers()));
                                        model.getTable().getClouds().get(model.getTable().getClouds().size()-1).charge(model.getTable().getBag());
                                        break;
                                    }
                                    if (team.getPlayer2().isDisconnected()) {
                                        ClientModel target = connectionModel.findPlayer(team.getPlayer2().getNickname());
                                        team.getPlayer2().setNickname(receivedClientModel.getNickname());
                                        team.getPlayer2().setDisconnected(false);
                                        receivedClientModel.setAmIfirst(false);
                                        receivedClientModel.setKicked(false);
                                        receivedClientModel.setGameStarted(true);
                                        receivedClientModel.setTypeOfRequest("CONNECTTOEXISTINGGAME");
                                        connectionModel.change(target, receivedClientModel);
                                        try {
                                            Network.send(json.toJson(receivedClientModel));
                                        } catch (InterruptedException e) {
                                            throw new RuntimeException(e);
                                        }
                                        model.setDisconnection(false);
                                        model.getTable().getClouds().add(new Cloud(model.getNumberOfPlayers()));
                                        model.getTable().getClouds().get(model.getTable().getClouds().size()-1).charge(model.getTable().getBag());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                      throw new RuntimeException(e);
                    }
                }
            }
        };
        t.start();
        teamMateChosen.fireStateEvent();

        return super.entryAction(cause);
    }

    private Model getModel() {
        return model;
    }

    /**
     * Stores the updated model in the main controller model
     * @param cause the event that caused us to exit this state
     */
    @Override
    public void exitAction(IEvent cause) throws IOException {
        serverController.setModel(model);
        super.exitAction(cause);
    }


}
