package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.controller.events.ClientDisconnection;
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

public class CreateGame extends State {
    private final Event gameCreated, fourPlayersGameCreated;
    private Model model;
    private final ConnectionModel connectionModel;
    private final ServerController serverController;
    private final Event reset = new ClientDisconnection();

    public CreateGame(ServerController serverController) {
        super("[Create game]");
        this.serverController = serverController;
        Controller controller = ServerController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        gameCreated = new Event("game created");
        reset.setStateEventListener(controller);
        fourPlayersGameCreated = new Event("Four players game created");
        gameCreated.setStateEventListener(controller);
    }

    public Event getReset() {
        return reset;
    }

    public Event gameCreated() {
        return gameCreated;
    }

    public Event fourPlayersGameCreated() {
        return fourPlayersGameCreated;
    }

    public Model getModel() {
        return model;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        model = new Model(connectionModel.getNumOfPlayers(), connectionModel.getGameMode());
        int i = 0;
        for (ClientModel c : connectionModel.getClientsInfo()) {
            model.getPlayers().add(new Player(connectionModel.getClientsInfo().get(i).getNickname(), connectionModel.getClientsInfo().get(i).getMyIp(), model));
            c.setGameStarted(true);
            c.setAmIfirst(false); // adesso i clients sono tutti "uguali", non c'è un primo
            i++;
        }
        model.randomschedulePlayers();
        serverController.setModel(model);

        Thread t= new Thread(){
            public synchronized void run() {
                while (true) {
                    if(connectionModel.isCloseThred()){
                        connectionModel.setCloseThred(false);
                        return;
                    }
                    //System.out.println("in connection controll loop");
                    ParametersFromNetwork message = new ParametersFromNetwork(1);
                    message.enable();
                    try {
                        message.waitParametersReceived();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    //System.out.println("check disconnessione");
                    Gson json = new Gson();
                    ClientModel receivedClientModel = json.fromJson(message.getParameter(0), ClientModel.class);

                        if(receivedClientModel.getAmIfirst()==null && !connectionModel.isCloseThred()) {
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
                                    for (Player p : getModel().getPlayers()) {
                                        if (p.isDisconnected()) {
                                            ClientModel target = connectionModel.findPlayer(p.getNickname());
                                            p.setNickname(receivedClientModel.getNickname());
                                            p.setDisconnected(false);
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
                                            //System.out.println("accepted");
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    //System.out.println("exit loop");
                        try {
                            sleep(5000);
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

}
