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

import java.io.IOException;

public class AskForTeamMate extends State {
    private final Event teamMateChoosen;
    private Model model;
    private final ConnectionModel connectionModel;

    private final Gson json;
    private final ServerController serverController;

    private ParametersFromNetwork message;
    private final Event reset = new ClientDisconnection();

    public Event teamMateChoosen() {
        return teamMateChoosen;
    }

    public AskForTeamMate(ServerController serverController) {
        super("[Ask for team mate]");
        this.serverController = serverController;
        Controller controller = ServerController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        teamMateChoosen = new Event("game created");
        reset.setStateEventListener(controller);
        teamMateChoosen.setStateEventListener(controller);
        json = new Gson();
    }
    public Event getReset() {
        return reset;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {

        ClientModel currentPlayerData = connectionModel.getClientsInfo().get(0);
        for(ClientModel c : connectionModel.getClientsInfo()){
            currentPlayerData.getNicknames().add(c.getNickname());
            c.setGameStarted(true);
        }
        currentPlayerData.getNicknames().remove(currentPlayerData.getNickname());
        currentPlayerData.setResponse(false); // è una richiesta non una risposta
        currentPlayerData.setTypeOfRequest("TEAMMATE");  // lato client avrà una nella CliView un metodo per gestire questa richiesta
        Network.send(json.toJson(currentPlayerData));

        boolean responseReceived = false;

        while (!responseReceived) {
            message = new ParametersFromNetwork(1);
            message.enable();
            while (!message.parametersReceived()) {}
            if (json.fromJson(message.getParameter(0), ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity()) {
                responseReceived = true;
            }
        }
        // Ho ricevuto la risposta, ora devo creare la partita
        ClientModel received = json.fromJson(message.getParameter(0), ClientModel.class);

        //model = new Model(4, "PRINCIPIANT"); // per il momento hardcodato PRINCIPIANT
        model = new Model(connectionModel.getNumOfPlayers(), connectionModel.getGameMode());
        model.getPlayers().add(new Player(received.getNicknames().get(3), connectionModel.findPlayer(received.getNicknames().get(3)).getMyIp(),1, model));
        model.getPlayers().add(new Player(received.getNicknames().get(2), connectionModel.findPlayer(received.getNicknames().get(2)).getMyIp(),1, model));
        model.getPlayers().add(new Player(received.getNicknames().get(1), connectionModel.findPlayer(received.getNicknames().get(1)).getMyIp(),2, model));
        model.getPlayers().add(new Player(received.getNicknames().get(0), connectionModel.findPlayer(received.getNicknames().get(0)).getMyIp(),2, model));
        model.randomschedulePlayers();
        teamMateChoosen.fireStateEvent();

        return super.entryAction(cause);
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        serverController.setModel(model);
        super.exitAction(cause);
    }


}
