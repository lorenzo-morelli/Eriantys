package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
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

public class StudentPhase extends State {
    private Event cardsChoosen;
    private Model model;

    private ConnectionModel connectionModel;
    private Controller controller;

    private Gson json;
    private ServerController serverController;

    private ParametersFromNetwork message;

    public Event cardsChoosen() {
        return cardsChoosen;
    }
    public StudentPhase(ServerController serverController) {
        super("[Move students]");
        this.serverController = serverController;
        this.controller = serverController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        //cardsChoosen= new Event("game created");
        //cardsChoosen.setStateEventListener(controller);
        json = new Gson();
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        int moves;
        model = serverController.getModel();
        // retrive the current player
        Player currentPlayer = model.getcurrentPlayer();
        // retrive data of the current player
        ClientModel currentPlayerData = connectionModel.findPlayer(currentPlayer.getNickname());
        if(model.getNumberOfPlayers() == 3){
            moves = 4;
        }
        else{
            moves = 3;
        }
        for(int i=0; i< moves; i++){
            currentPlayerData.setServermodel(model);
            currentPlayerData.setTypeOfRequest("CHOOSEWHERETOMOVESTUDENTS");
            currentPlayerData.setResponse(false); //non è una risposta, è una richiesta del server al client

            Network.send(json.toJson(currentPlayerData));

            boolean responseReceived = false;

            while (!responseReceived) {
                message = new ParametersFromNetwork(1);
                message.enable();
                while (!message.parametersReceived()) {
                    // il client non ha ancora scelto la carta assistente
                }
                if(json.fromJson(message.getParameter(0),ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity()){
                    responseReceived = true;
                }
            }


            currentPlayerData = json.fromJson(message.getParameter(0),ClientModel.class);
            String type = currentPlayerData.getTypeOfRequest();

            if(type.equals("SCHOOL")){
                currentPlayer.getSchoolBoard().load_dinner(currentPlayerData.getChoosedColor());
                model.getTable().checkProfessor(currentPlayerData.getChoosedColor(),model.getPlayers());
            }
            else if(type.equals("ISLAND")){
                model.getTable().load_island(currentPlayer,currentPlayerData.getChoosedColor(),currentPlayerData.getChoosedIsland());
            }

        }

        return super.entryAction(cause);
    }
}
