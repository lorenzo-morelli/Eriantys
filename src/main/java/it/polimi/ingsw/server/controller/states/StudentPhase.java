package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.controller.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.characters.*;
import it.polimi.ingsw.server.model.enums.GameMode;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

public class StudentPhase extends State {
    private final Event studentPhaseEnded, gameEnd;

    private final ConnectionModel connectionModel;

    private final Gson json;
    private final ServerController serverController;

    private ParametersFromNetwork message;
    private final Event reset = new ClientDisconnection();

    public Event studentPhaseEnded() {
        return studentPhaseEnded;
    }
    public Event gameEnd() {
        return gameEnd;
    }
    public StudentPhase(ServerController serverController) {
        super("[Move students]");
        this.serverController = serverController;
        Controller controller = ServerController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        studentPhaseEnded = new Event("game created");
        studentPhaseEnded.setStateEventListener(controller);
        reset.setStateEventListener(controller);
        gameEnd = new Event("end phase");
        gameEnd.setStateEventListener(controller);
        json = new Gson();
    }
    public Event getReset() {
        return reset;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        int moves;
        Model model = serverController.getModel();
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
                while (!message.parametersReceived()) {}
                if(json.fromJson(message.getParameter(0),ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity()){
                    responseReceived = true;
                }
            }

            // dati ricevuti da network
            currentPlayerData = json.fromJson(message.getParameter(0),ClientModel.class);
            /*
              type:
              SCHOOL : il client vuole muovere uno studente dalla entrance space alla SCHOOL
              ISLAND : il client vuole muovere uno studente dalla entrance space alla ISLAND

              supposizioni: il client ha già scelto il colore tra quelli disponibili, ed il
              server lo può trovare in currentPlayerData.getChoosedColor()
             */
            String type = currentPlayerData.getTypeOfRequest();
            System.out.println("HO RICEVUTO " + type + " " + currentPlayerData.getChoosedColor());
            if(type.equals("SCHOOL")){
                currentPlayer.getSchoolBoard().load_dinner(currentPlayerData.getChoosedColor());
                if(model.getGameMode().equals(GameMode.EXPERT) && currentPlayer.getSchoolBoard().getDinnerTable().numStudentsbycolor(currentPlayerData.getChoosedColor())%3==0){
                    currentPlayer.improveCoin();
                }
                model.getTable().checkProfessor(currentPlayerData.getChoosedColor(), model.getPlayers());
            }
            else if(type.equals("ISLAND")){
                model.getTable().load_island(currentPlayer,currentPlayerData.getChoosedColor(),currentPlayerData.getChoosedIsland());
            }
            else{
                i--;
                for(int j = 0; j< model.getTable().getCharachter().size(); j++){
                    if(model.getTable().getCharachter().get(j).getName().equals(type)){
                        switch (type){
                            case "MUSHROOMHUNTER": ((MushroomHunter) model.getTable().getCharachter().get(j)).useEffect(currentPlayer,currentPlayerData.getChoosedColor(), model.getTable());
                            break;
                            case "THIEF": ((Thief) model.getTable().getCharachter().get(j)).useEffect(currentPlayer, model.getPlayers(),currentPlayerData.getChoosedColor(), model.getTable());
                            break;
                            case "CENTAUR": ((Centaur) model.getTable().getCharachter().get(j)).useEffect(currentPlayer, model.getTable());
                                break;
                            case "FARMER": ((Farmer) model.getTable().getCharachter().get(j)).useEffect(currentPlayer, model.getTable(), model.getPlayers());
                                break;
                            case "KNIGHT": ((Knight) model.getTable().getCharachter().get(j)).useEffect(currentPlayer, model.getTable());
                                break;
                            case "MINSTRELL": ((Minstrell) model.getTable().getCharachter().get(j)).useEffect(currentPlayer,currentPlayerData.getColors2(),currentPlayerData.getColors1());
                                break;
                            case "JESTER": ((Jester) model.getTable().getCharachter().get(j)).useEffect(currentPlayer,currentPlayerData.getColors2(),currentPlayerData.getColors1());
                                break;
                            case "POSTMAN": ((Postman)  model.getTable().getCharachter().get(j)).useEffect(currentPlayer);
                                break;
                            case "PRINCESS": ((Princess)  model.getTable().getCharachter().get(j)).useEffect(currentPlayer,currentPlayerData.getChoosedColor(), model.getTable(), model.getPlayers());
                                break;
                            case "GRANNY": ((Granny)  model.getTable().getCharachter().get(j)).useEffect(currentPlayer,currentPlayerData.getChoosedIsland(), model.getTable());
                                break;
                            case "MONK": ((Monk)  model.getTable().getCharachter().get(j)).useEffect(currentPlayer,currentPlayerData.getChoosedColor(),currentPlayerData.getChoosedIsland(), model.getTable());
                                break;
                            case "HERALD": boolean check= ((Herald)  model.getTable().getCharachter().get(j)).useEffect(currentPlayer,currentPlayerData.getChoosedIsland(), model);
                                if(check) {
                                    gameEnd().fireStateEvent();
                                    return super.entryAction(cause);
                                }
                                break;
                        }
                        break;
                    }
                }
            }
        }
        studentPhaseEnded.fireStateEvent();
        return super.entryAction(cause);
    }
}
