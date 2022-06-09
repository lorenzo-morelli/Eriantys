package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.controller.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.PingThread.MotherThread;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Model;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Team;
import it.polimi.ingsw.server.model.characters.*;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MotherPhase extends State {
    private final Event gameEnd, goToStudentPhase, goToCloudPhase, goToEndTurn;
    private Model model;

    private final ConnectionModel connectionModel;

    private final Gson json;
    private final ServerController serverController;
    private final Event reset = new ClientDisconnection();

    private ParametersFromNetwork message;
    private boolean disconnected,fromPing;
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
    public Event GoToEndTurn() {
        return goToEndTurn;
    }

    public MotherPhase(ServerController serverController) {
        super("[Move mother]");
        this.serverController = serverController;
        Controller controller = ServerController.getFsm();
        this.connectionModel = serverController.getConnectionModel();
        reset.setStateEventListener(controller);
        gameEnd = new Event("end phase");
        gameEnd.setStateEventListener(controller);
        goToCloudPhase = new Event("go to cloud phase");
        goToCloudPhase.setStateEventListener(controller);
        goToStudentPhase = new Event("go to student phase");
        goToStudentPhase.setStateEventListener(controller);goToEndTurn= new Event("go to end turn");
        goToEndTurn.setStateEventListener(controller);
        json = new Gson();
        istorepeat=true;
    }
    public Event getReset() {
        return reset;
    }

    @Override
    public IEvent entryAction(IEvent cause) throws Exception {

        while (istorepeat) {
            istorepeat = false;
            model = serverController.getModel();

            // retrive the current player

            Player currentPlayer = model.getcurrentPlayer();
            disconnected=false; //flag che indica se si disconnette durante questo turno
            fromPing=false; //risposta non proviene da ping

            //salto fase se player se si è disconnesso precedentemente

            if(currentPlayer.isDisconnected()){

                if (model.getTable().getIslands().size() <= 3) {
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
                } else if (model.getcurrentPlayer().equals(model.getPlayers().get(model.getPlayers().size() - 1))) {
                    GoToEndTurn().fireStateEvent();
                } else {
                    model.nextPlayer();
                    goToStudentPhase().fireStateEvent();
                }
                return super.entryAction(cause);
            }

            // retrive data of the current player
            System.out.println(currentPlayer.getNickname());

            ClientModel currentPlayerData = connectionModel.findPlayer(currentPlayer.getNickname());
            currentPlayerData.setServermodel(model);
            currentPlayerData.setTypeOfRequest("CHOOSEWHERETOMOVEMOTHER");
            currentPlayerData.setPingMessage(false);
            currentPlayerData.setResponse(false); //non è una risposta, è una richiesta del server al client

            //invio e controllo che invio network sia fatto correttamente

            boolean checkError= Network.send(json.toJson(currentPlayerData));

            // se invio non va a buon fine salta il giocatore

            if(!checkError){
                if (model.getTable().getIslands().size() <= 3) {
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
            }

            //controllo ricezione risposta invio ping e settaggio del giocatore in disconnessione in caso di ricezione ping fallita

            Thread ping = new MotherThread(this, currentPlayerData);
            ping.start();

            boolean responseReceived = false;
            while (!responseReceived) {
                //System.out.println("another one");
                    if (!fromPing) {
                        message = new ParametersFromNetwork(1);
                        message.enable();
                }
                while (!message.parametersReceived()) {
                    //System.out.println("loop");
                    message.waitParametersReceived(5);
                    if (disconnected) {
                        break;
                    }
                }
                    if (disconnected || (json.fromJson(message.getParameter(0), ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity() && !json.fromJson(message.getParameter(0), ClientModel.class).isPingMessage())) {
                        responseReceived = true;
                        if (disconnected) {
                            currentPlayer.setDisconnected(true);
                            boolean check=true;
                            for(int j=0;j<model.getTable().getClouds().size();j++) {
                                if(model.getTable().getClouds().get(j).getStudentsAccumulator().size()==0)
                                {
                                    model.getTable().getClouds().remove(j);
                                    check=false;
                                    break;
                                }
                            }
                            if (check) {
                                model.getTable().getClouds().remove(0);
                            }
                        } else {
                            ping.interrupt();
                        }
                    }
                    fromPing=false;
            }

            //codice effettivo della fase se non si è disconnesso

            if (!currentPlayer.isDisconnected()) {

                currentPlayerData = json.fromJson(message.getParameter(0), ClientModel.class);
                String type = currentPlayerData.getTypeOfRequest();
                //System.out.println("HO RICEVUTO " + type);
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
                                    model.getTable().conquestIsland(model.getTable().getMotherNaturePosition(), model.getTeams(), influence_team);
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
                                    model.getTable().conquestIsland(model.getTable().getMotherNaturePosition(), model.getPlayers(), influence_player);
                                }
                                if (influence_player.getSchoolBoard().getNumOfTowers() == 0) {
                                    gameEnd().fireStateEvent();
                                    return super.entryAction(cause);
                                }
                            }
                        }
                        if (model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()) != null) {
                            if (model.getTable().getIslands().get((model.getTable().getMotherNaturePosition() + 1) % model.getTable().getIslands().size()).getTowerColor() != null && model.getTable().getIslands().get((model.getTable().getMotherNaturePosition() + 1) % model.getTable().getIslands().size()).getTowerColor().equals(model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()).getTowerColor())) {
                                model.getTable().mergeIsland(model.getTable().getMotherNaturePosition(), ((model.getTable().getMotherNaturePosition() + 1) % model.getTable().getIslands().size()));
                            }
                            Island merging;
                            if (model.getTable().getMotherNaturePosition() == 0) {
                                merging = model.getTable().getIslands().get(model.getTable().getIslands().size() - 1);
                            } else {
                                merging = model.getTable().getIslands().get((model.getTable().getMotherNaturePosition() - 1) % model.getTable().getIslands().size());
                            }
                            if (merging.getTowerColor() != null && merging.getTowerColor().equals(model.getTable().getIslands().get(model.getTable().getMotherNaturePosition()).getTowerColor())) {
                                int mergingindex;
                                if (model.getTable().getMotherNaturePosition() == 0) {
                                    mergingindex = model.getTable().getIslands().size() - 1;
                                } else {
                                    mergingindex = (model.getTable().getMotherNaturePosition() - 1) % model.getTable().getIslands().size();
                                }
                                model.getTable().mergeIsland(model.getTable().getMotherNaturePosition(), mergingindex);
                            }
                        }
                    } else {
                        target.setBlocked(false);
                        for (int i = 0; i < model.getTable().getCharacters().size(); i++) {
                            if (model.getTable().getCharacters().get(i) instanceof Granny) {
                                ((Granny) model.getTable().getCharacters().get(i)).improveDivieti();
                                break;
                            }
                        }
                    }

                    model.getTable().setCentaurEffect(false);
                    model.getTable().setMushroomColor(null);
                    model.getTable().setKnightEffect(null);

                    if (model.getTable().getIslands().size() <= 3) {
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
                    for (int j = 0; j < model.getTable().getCharacters().size(); j++) {
                        if (model.getTable().getCharacters().get(j).getName().equals(type)) {
                            switch (type) {
                                case "MUSHROOMHUNTER":
                                    ((MushroomHunter) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, currentPlayerData.getChoosedColor(), model.getTable());
                                    break;
                                case "THIEF":
                                    ((Thief) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, model.getPlayers(), currentPlayerData.getChoosedColor(), model.getTable());
                                    break;
                                case "CENTAUR":
                                    ((Centaur) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, model.getTable());
                                    break;
                                case "FARMER":
                                    ((Farmer) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, model.getTable(), model.getPlayers());
                                    break;
                                case "KNIGHT":
                                    ((Knight) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, model.getTable());
                                    break;
                                case "MINSTRELL":
                                    ((Minstrell) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, currentPlayerData.getColors2(), currentPlayerData.getColors1(),model.getTable(),model.getPlayers());
                                    break;
                                case "JESTER":
                                    ((Jester) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, currentPlayerData.getColors2(), currentPlayerData.getColors1());
                                    break;
                                case "POSTMAN":
                                    ((Postman) model.getTable().getCharacters().get(j)).useEffect(currentPlayer);
                                    break;
                                case "PRINCESS":
                                    ((Princess) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, currentPlayerData.getChoosedColor(), model.getTable(), model.getPlayers());
                                    break;
                                case "GRANNY":
                                    ((Granny) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, currentPlayerData.getChoosedIsland(), model.getTable());
                                    break;
                                case "MONK":
                                    ((Monk) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, currentPlayerData.getChoosedColor(), currentPlayerData.getChoosedIsland(), model.getTable());
                                    break;
                                case "HERALD":
                                    boolean check = ((Herald) model.getTable().getCharacters().get(j)).useEffect(currentPlayer, currentPlayerData.getChoosedIsland(), model);
                                    if (check) {
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

            //codice per disconnessione durante questo turno

            else{
                int check=0;
                if(model.getNumberOfPlayers()==4){
                    for(Team team: model.getTeams()){
                        if(!team.getPlayer1().isDisconnected() || !team.getPlayer2().isDisconnected()){
                            check++;
                        }
                    }
                }
                else{
                    for(Player p: model.getPlayers()){
                        if(!p.isDisconnected()){
                            check++;
                        }
                    }
                }
                if(check<=1){
                    System.out.println("attendo 40 secondi in attesa di una riconnessione");
                    check=0;
                    if(model.getNumberOfPlayers()==4){
                        for(Team team: model.getTeams()){
                            if(!team.getPlayer1().isDisconnected() || !team.getPlayer2().isDisconnected()){
                                check++;
                            }
                        }
                    }
                    else{
                        for(Player p: model.getPlayers()){
                            if(!p.isDisconnected()){
                                check++;
                            }
                        }
                    }
                    if(check<=1){
                        System.out.println("numero minimo di giocatori non disponibile, attendo 40 secondi in attesa che un altro giocatore si riconnette");
                        for (Player p : model.getPlayers()) {
                            if(!p.isDisconnected()) {
                                ClientModel Data = connectionModel.findPlayer(p.getNickname());

                                Data.setTypeOfRequest("TRYTORECONNECT");
                                Data.setServermodel(model);
                                Data.setResponse(false);
                                Data.setPingMessage(false);

                                Network.send(json.toJson(Data));
                            }
                        }

                        model.setDisconnection(true);
                        TimeUnit.MILLISECONDS.sleep(40000); //aspetto 40 secondi nella speranza che qualcuno si riconnetta

                        if (model.isDisconnection()) {
                            gameEnd().fireStateEvent();
                            return super.entryAction(cause);
                        }
                        System.out.println("un giocatore si è riconnesso, la partita può ricominciare");
                    }
                }
                if (model.getTable().getIslands().size() <= 3) {
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
                    model.nextPlayer();
                    GoToCloudPhase().fireStateEvent();
                    return super.entryAction(cause);
                }
            }
        }
    return super.entryAction(cause);
    }

    @Override
    public void exitAction(IEvent cause) throws IOException {
        model.getTable().setFarmerEffect(null);
        istorepeat = true;
        super.exitAction(cause);
    }

    public ParametersFromNetwork getMessage() {
        return message;
    }

    public void setMessage(ParametersFromNetwork message) {
        this.message = message;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    public void setFromPing(boolean fromPing) {
        this.fromPing = fromPing;
    }
}
