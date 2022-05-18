package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Team;
import it.polimi.ingsw.server.model.characters.Character;
import it.polimi.ingsw.server.model.characters.Granny;
import it.polimi.ingsw.server.model.characters.MushroomHunter;
import it.polimi.ingsw.server.model.characters.Thief;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

public class MotherPhase extends State {
    private Event gameEnd, goToStudentPhase, goToCloudPhase;
    private Model model;

    private ConnectionModel connectionModel;
    private Controller controller;

    private Gson json;
    private ServerController serverController;

    private ParametersFromNetwork message;
    private boolean istorepeat;

    public Event gameEnd() {
        return gameEnd;
    }

    public Event goToStudentPhase() {
        return goToStudentPhase;
    }

    public Event GoToCloudPhase() {
        return goToCloudPhase;
    }

    public MotherPhase(ServerController serverController) {
        super("[Move mother]");
        this.serverController = serverController;
        this.controller = serverController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        gameEnd = new Event("end phase");
        gameEnd.setStateEventListener(controller);
        goToCloudPhase = new Event("go to cloud phase");
        goToCloudPhase.setStateEventListener(controller);
        goToStudentPhase = new Event("go to student phase");
        goToStudentPhase.setStateEventListener(controller);
        json = new Gson();
        istorepeat=true;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {
        while (istorepeat) {
            istorepeat=false;
            model = serverController.getModel();
            // retrive the current player
            Player currentPlayer = model.getcurrentPlayer();
            // retrive data of the current player
            ClientModel currentPlayerData = connectionModel.findPlayer(currentPlayer.getNickname());
            currentPlayerData.setServermodel(model);
            currentPlayerData.setTypeOfRequest("CHOOSEWHERETOMOVEMOTHER");
            currentPlayerData.setResponse(false); //non è una risposta, è una richiesta del server al client

            Network.send(json.toJson(currentPlayerData));

            boolean responseReceived = false;

            while (!responseReceived) {
                message = new ParametersFromNetwork(1);
                message.enable();
                while (!message.parametersReceived()) {
                    // il client non ha ancora scelto dove muovere madre natura
                }
                if (json.fromJson(message.getParameter(0), ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity()) {
                    responseReceived = true;
                }
            }

            currentPlayerData = json.fromJson(message.getParameter(0), ClientModel.class);
            String type = currentPlayerData.getTypeOfRequest();
            System.out.println("HO RICEVUTO " + type);
            if (type.equals("MOTHER")) {
                // Si suppone che il client abblia scelto il numero di mosse (passi da far fare a madre natura)
                int moves = currentPlayerData.getChoosedMoves();
                model.getTable().movemother(moves);
                Island target = model.getTable().getIslands().get(model.getTable().getMotherNaturePosition());
                if (!target.isBlocked()) {
                    if (model.getNumberOfPlayers() == 4) {
                        Team influence_team = target.team_influence(model.getTeams(), model.getTable().getProfessors(), model.getTable().isCentaurEffect(), model.getTable().getMushroomColor(), model.getTable().getKnightEffect());
                        if (influence_team != null) {
                            if (target.getNumberOfTowers() == 0) {
                                target.controllIsland(influence_team);
                                target.placeTower();
                            } else if (!(target.getTowerColor().equals(influence_team.getPlayer1().getSchoolBoard().getTowerColor()))) {
                                model.getTable().ConquestIsland(model.getTable().getMotherNaturePosition(), model.getTeams(), influence_team);
                            }
                            if (influence_team.getPlayer1().getSchoolBoard().getNumOfTowers() == 0) {
                                gameEnd().fireStateEvent();
                                return super.entryAction(cause);
                            }
                        }
                    } else {
                        Player influence_player = target.player_influence(model.getPlayers(), model.getTable().getProfessors(), model.getTable().isCentaurEffect(), model.getTable().getMushroomColor(), model.getTable().getKnightEffect());
                        if (influence_player != null) {
                            if (target.getNumberOfTowers() == 0) {
                                target.controllIsland(influence_player);
                                target.placeTower();
                            } else if (!(target.getTowerColor().equals(influence_player.getSchoolBoard().getTowerColor()))) {
                                model.getTable().ConquestIsland(model.getTable().getMotherNaturePosition(), model.getPlayers(), influence_player);
                            }
                            if (influence_player.getSchoolBoard().getNumOfTowers() == 0) {
                                gameEnd().fireStateEvent();
                                return super.entryAction(cause);
                            }
                        }
                    }
                    if (model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()) != null) {
                        if (model.getTable().getIslands().get((model.getTable().getMotherNaturePosition() + 1) % model.getTable().getIslands().size()).getTowerColor() != null && model.getTable().getIslands().get((model.getTable().getMotherNaturePosition() + 1) % model.getTable().getIslands().size()).getTowerColor().equals(model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()).getTowerColor())) {
                            model.getTable().MergeIsland(model.getTable().getMotherNaturePosition(), ((model.getTable().getMotherNaturePosition() + 1) % model.getTable().getIslands().size()));
                        }
                        if (model.getTable().getIslands().get((model.getTable().getMotherNaturePosition() - 1) % model.getTable().getIslands().size()).getTowerColor() != null && model.getTable().getIslands().get((model.getTable().getMotherNaturePosition() - 1) % model.getTable().getIslands().size()).getTowerColor().equals(model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()).getTowerColor())) {
                            model.getTable().MergeIsland(model.getTable().getMotherNaturePosition(), ((model.getTable().getMotherNaturePosition() - 1) % model.getTable().getIslands().size()));
                        }
                    }
                } else {
                    target.setBlocked(false);
                    for (int i = 0; i < model.getTable().getCards().size(); i++) {
                        if (model.getTable().getCards().get(i) instanceof Granny) {
                            ((Granny) model.getTable().getCards().get(i)).improveDivieti();
                            break;
                        }
                    }
                }

                model.getTable().setCentaurEffect(false);
                model.getTable().setMushroomColor(null);
                model.getTable().setKnightEffect(null);

                if (model.getTable().getIslands().size() == 3) {
                    gameEnd().fireStateEvent();
                    return super.entryAction(cause);
                } else if (model.islastturn()) {
                    if (model.getcurrentPlayer().equals(model.getPlayers().get(model.getPlayers().size() - 1))) {
                        gameEnd().fireStateEvent();
                    } else {
                        model.nextPlayer();
                        goToStudentPhase().fireStateEvent();
                    }
                    return super.entryAction(cause);
                } else {
                    GoToCloudPhase().fireStateEvent();
                    return super.entryAction(cause);
                }
            } else {
                istorepeat = true;
                for(int j=0;j<model.getTable().getCards().size();j++){
                    if(model.getTable().getCards().get(j).getName().equals(type)){
                        switch (type){
                            case "MUSHROOMHUNTER": ((MushroomHunter) model.getTable().getCards().get(j)).useEffect(currentPlayer,currentPlayerData.getChoosedColor(),model.getTable());
                                break;
                            case "THIEF": ((Thief) model.getTable().getCards().get(j)).useEffect(currentPlayer,model.getPlayers(),currentPlayerData.getChoosedColor(),model.getTable());
                                break;
                            //fare gli altri...
                        }
                    }
                }

            }
        }
    return super.entryAction(cause);
    }
}