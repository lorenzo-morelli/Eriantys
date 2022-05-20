package it.polimi.ingsw.server.controller.states;

import it.polimi.ingsw.client.controller.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;

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
