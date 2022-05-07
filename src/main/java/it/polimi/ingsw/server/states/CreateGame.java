package it.polimi.ingsw.server.states;

import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.server.ConnectionModel;
import it.polimi.ingsw.server.ServerController;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

public class CreateGame extends State {
    private Event gameCreated;
    private Model model;

    private ConnectionModel connectionModel;
    private Controller controller;
    private ServerController serverController;
    public CreateGame(ServerController serverController) {
        super("[Create game]");
        this.serverController = serverController;
        this.controller = serverController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        gameCreated = new Event("game created");
        gameCreated.setStateEventListener(controller);
    }

    public Event gameCreated() {
        return gameCreated;
    }

    public Model getModel() {
        return model;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        model = new Model(connectionModel.getNumOfPlayers(), connectionModel.getGameMode());
        int i = 0;
        for (ClientModel c : connectionModel.getClientsInfo()){
            model.getPlayers().add(new Player(connectionModel.getClientsInfo().get(i).getNickname(),connectionModel.getClientsInfo().get(i).getMyIp(), model));
            i++;
        }
        model.randomschedulePlayers();
        gameCreated.fireStateEvent();
        return super.entryAction(cause);
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        serverController.setModel(model);
        super.exitAction(cause);
    }
}
