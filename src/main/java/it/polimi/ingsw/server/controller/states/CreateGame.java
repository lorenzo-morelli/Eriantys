package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.controller.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.ServerController;
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
            public void run() {
                while (true) {
                    ParametersFromNetwork message = new ParametersFromNetwork(1);
                    message.enable();
                    while (!message.parametersReceived()) {
                        // non ho ricevuto ancora nessun messaggio
                    }
                    //System.out.println("check disconnessione");
                    Gson json = new Gson();
                    ClientModel receivedClientModel = json.fromJson(message.getParameter(0), ClientModel.class);
                    synchronized (this) {

                        //controllo se ci siano player disconnessi
                        boolean check = false;

                        for (Player p : getModel().getPlayers()) {
                            if (p.isDisconnected()) {
                                check = true;
                                break;
                            }
                        }

                        if (receivedClientModel.getAmIfirst() == null && !check) {
                            try {
                                receivedClientModel.setTypeOfRequest("KICK");
                                receivedClientModel.setKicked(true);
                                System.out.println("invio segnale di disconnessione");
                                Network.send(json.toJson(receivedClientModel));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        } else if(receivedClientModel.getAmIfirst() == null){
                            for (Player p : getModel().getPlayers()) {
                                if (p.isDisconnected()) {
                                    ClientModel target=connectionModel.findPlayer(p.getNickname());
                                    target.setNickname(receivedClientModel.getNickname());
                                    p.setNickname(receivedClientModel.getNickname());
                                    p.setDisconnected(false);
                                    receivedClientModel.setAmIfirst(false);
                                    receivedClientModel.setTypeOfRequest("CONNECTTOEXISTINGGAME");
                                    try {
                                        Network.send(json.toJson(receivedClientModel));
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    model.setDisconnection(false);
                                    return;
                                }
                            }
                        }

                    }
                }
            }
        };
        t.start();
        gameCreated.fireStateEvent();
        return super.entryAction(cause);
    }

}
