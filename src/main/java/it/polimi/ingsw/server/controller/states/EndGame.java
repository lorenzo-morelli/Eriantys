package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
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

public class EndGame extends State {
    private Model model;
private Event restart;
    private ConnectionModel connectionModel;
    private Controller controller;

    private Gson json;
    private ServerController serverController;

    public EndGame(ServerController serverController) {
        super("[End Game]");
        this.serverController = serverController;
        this.controller = serverController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        restart= new Event("Gioco Terminato, server riportato in WaitForPlayerConnection");
        restart.setStateEventListener(controller);

        json = new Gson();
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        model = serverController.getModel();
        String winner;
        if(model.getNumberOfPlayers()==4){
            winner=model.team_winner();
        }
        else {
            winner= model.player_winner();
        }

        for(int i=0;i<model.getPlayers().size();i++){

            ClientModel currentPlayerData = connectionModel.findPlayer(model.getPlayers().get(i).getNickname());

            currentPlayerData.setGamewinner(winner);
            currentPlayerData.setTypeOfRequest("GAMEEND");
            currentPlayerData.setResponse(false); //non è una risposta, è una richiesta del server al client

            Network.send(json.toJson(currentPlayerData));

        }
        getRestart().fireStateEvent();
        return super.entryAction(cause);
    }

    public Event getRestart() {
        return restart;
    }
}