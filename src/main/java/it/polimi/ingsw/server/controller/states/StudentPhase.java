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
    private Event studentPhaseEnded;
    private Model model;

    private ConnectionModel connectionModel;
    private Controller controller;

    private Gson json;
    private ServerController serverController;

    private ParametersFromNetwork message;

    public Event studentPhaseEnded() {
        return studentPhaseEnded;
    }
    public StudentPhase(ServerController serverController) {
        super("[Move students]");
        this.serverController = serverController;
        this.controller = serverController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        studentPhaseEnded = new Event("game created");
        studentPhaseEnded.setStateEventListener(controller);
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

            // dati ricevuti da network
            currentPlayerData = json.fromJson(message.getParameter(0),ClientModel.class);
            /**
             * type:
             * SCHOOL : il client vuole muovere uno studente dalla entrance space alla SCHOOL
             * ISLAND : il client vuole muovere uno studente dalla entrance space alla ISLAND
             *
             * supposizioni: il client ha già scelto il colore tra quelli disponibili, ed il
             * server lo può trovare in currentPlayerData.getChoosedColor()
             */
            String type = currentPlayerData.getTypeOfRequest();

            if(type.equals("SCHOOL")){
                currentPlayer.getSchoolBoard().load_dinner(currentPlayerData.getChoosedColor());
                model.getTable().checkProfessor(currentPlayerData.getChoosedColor(),model.getPlayers());
            }
            else if(type.equals("ISLAND")){
                model.getTable().load_island(currentPlayer,currentPlayerData.getChoosedColor(),currentPlayerData.getChoosedIsland());
            }
        }
        studentPhaseEnded.fireStateEvent();
        return super.entryAction(cause);
    }
}
