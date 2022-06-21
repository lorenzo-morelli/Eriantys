package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.util.concurrent.TimeUnit;

/**
 * This state handles the things to do in the event of a natural end of the game
 * caused by termination events (in compliance with the rules of the game).
 */
public class EndGame extends State {
    private final Event restart;
    private final ConnectionModel connectionModel;

    private final Gson json;
    private final ServerController serverController;

    public EndGame(ServerController serverController) {
        super("[End Game]");
        this.serverController = serverController;
        Controller controller = ServerController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        restart= new Event("Game ended, server goes to WaitForPlayerConnection");
        restart.setStateEventListener(controller);

        json = new Gson();
    }

    /**
     * Note to clients that the game is ended :
     * Two possible cases : end for disconnection or caused by the win of one of the player
     * @param cause the event that caused the controller transition in this state
     * @return null event
     * @throws Exception input output or network related exceptions
     */

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        Model model = serverController.getModel();

        if (model.isDisconnection()){

            System.out.println("Send disconnection: minimum number of player not available");

            for(int h = 0; h< model.getPlayers().size(); h++){

                ClientModel currentPlayerData = connectionModel.findPlayer(model.getPlayers().get(h).getNickname());
                currentPlayerData.setTypeOfRequest("DISCONNECTION");
                currentPlayerData.setServermodel(model);
                currentPlayerData.setResponse(false);

                Network.send(json.toJson(currentPlayerData));
                Network.setDisconnectedClient(true);
            }
            TimeUnit.SECONDS.sleep(5);
            getRestart().fireStateEvent();
            connectionModel.close();
            return super.entryAction(cause);

        }


        String winner;
        if(model.getNumberOfPlayers()==4){
            winner= model.team_winner();
        }
        else {
            winner= model.player_winner();
        }

        for(int i = 0; i< model.getPlayers().size(); i++){

            ClientModel currentPlayerData = connectionModel.findPlayer(model.getPlayers().get(i).getNickname());

            currentPlayerData.setGameWinner(winner);
            currentPlayerData.setTypeOfRequest("GAMEEND");
            currentPlayerData.setServermodel(model);
            currentPlayerData.setResponse(false);

            Network.send(json.toJson(currentPlayerData));
            Network.setDisconnectedClient(true);

        }
        TimeUnit.SECONDS.sleep(5);
        getRestart().fireStateEvent();
        connectionModel.close();
        return super.entryAction(cause);
    }

    /**
     * Events callers
     * @return different events in order to change to different phase
     */
    public Event getRestart() {
        return restart;
    }
}