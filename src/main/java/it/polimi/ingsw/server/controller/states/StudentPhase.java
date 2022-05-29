package it.polimi.ingsw.server.controller.states;

import com.google.gson.Gson;
import it.polimi.ingsw.client.controller.events.ClientDisconnection;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.server.controller.ConnectionModel;
import it.polimi.ingsw.server.controller.PingThread.StudentThread;
import it.polimi.ingsw.server.controller.ServerController;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.characters.*;
import it.polimi.ingsw.server.model.enums.GameMode;
import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.utils.network.Network;
import it.polimi.ingsw.utils.network.events.ParametersFromNetwork;
import it.polimi.ingsw.utils.stateMachine.Controller;
import it.polimi.ingsw.utils.stateMachine.Event;
import it.polimi.ingsw.utils.stateMachine.IEvent;
import it.polimi.ingsw.utils.stateMachine.State;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.shuffle;

public class StudentPhase extends State {
    private final Event studentPhaseEnded, gameEnd;

    private final ConnectionModel connectionModel;

    private final Gson json;
    private final ServerController serverController;

    private ParametersFromNetwork message;

    private boolean disconnected,fromPing;
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
        disconnected=false; //flag che indica se si disconnette durante questo turno
        fromPing=false; //risposta non proviene da ping

        //salto fase se player se si è disconnesso precedentemente

        if(currentPlayer.isDisconnected()){
            studentPhaseEnded.fireStateEvent();
            return super.entryAction(cause);
        }

        // retrive data of the current player

        ClientModel currentPlayerData = connectionModel.findPlayer(currentPlayer.getNickname());

        //gestione numero mosse che il player puo fare

        if(model.getNumberOfPlayers() == 3){
            moves = 4;
        }
        else{
            moves = 3;
        }

        //codice looppa per ogni mossa che player puo fare

        for(int i=0; i< moves; i++) {

            currentPlayerData.setServermodel(model);
            currentPlayerData.setTypeOfRequest("CHOOSEWHERETOMOVESTUDENTS");
            currentPlayerData.setPingMessage(false);
            currentPlayerData.setResponse(false); //non è una risposta, è una richiesta del server al client

            //invio e controllo che invio network sia fatto correttamente

            boolean checkError= Network.send(json.toJson(currentPlayerData));

            // se invio non va a buon fine salta il giocatore

            if(!checkError){
                studentPhaseEnded.fireStateEvent();
                return super.entryAction(cause);
            }

            //controllo ricezione risposta invio ping e settaggio del giocatore in disconnessione in caso di ricezione ping fallita

            Thread ping = new StudentThread(this, currentPlayerData);
            ping.start();

            boolean responseReceived = false;
            while (!responseReceived) {
                System.out.println("ancora qua");
                    if (!fromPing) {
                        message = new ParametersFromNetwork(1);
                        message.enable();
                    }
                while (!message.parametersReceived()) {
                    System.out.println("non arrivato nulla");
                    message.waitParametersReceived(5);
                    if (disconnected) {
                        break;
                    }
                }
                    if (disconnected || (json.fromJson(message.getParameter(0), ClientModel.class).getClientIdentity() == currentPlayerData.getClientIdentity() && !json.fromJson(message.getParameter(0), ClientModel.class).isPingMessage())) {
                        responseReceived = true;
                        if (disconnected) {
                            currentPlayer.setDisconnected(true);
                            List<PeopleColor> colors= Arrays.asList(PeopleColor.values());
                            shuffle(colors);
                            int j=0;
                            for(PeopleColor color: colors) {
                                if(currentPlayer.getSchoolBoard().getEntranceSpace().numStudentsbycolor(color)!=0) {
                                    currentPlayer.getSchoolBoard().load_dinner(color);
                                    model.getTable().checkProfessor(color,model.getPlayers());
                                    j++;
                                    if (j == moves - i) {
                                        break;
                                    }
                                }
                            }
                            for(int k=0;k<model.getTable().getClouds().size();k++) {
                                if(model.getTable().getClouds().get(k).getStudentsAccumulator().size()!=0)
                                {
                                    currentPlayer.getSchoolBoard().load_entrance(model.getTable().getClouds().get(k),model.getTable().getClouds());
                                    model.getTable().getClouds().remove(k);
                                    break;
                                }
                            }
                            break;
                        } else {
                            ping.interrupt();
                        }
                    }
                    fromPing=false;
            }

            //codice effettivo della fase se non si è disconnesso

            if (!currentPlayer.isDisconnected()) {
                // dati ricevuti da network
                currentPlayerData = json.fromJson(message.getParameter(0), ClientModel.class);
            /*
              type:
              SCHOOL : il client vuole muovere uno studente dalla entrance space alla SCHOOL
              ISLAND : il client vuole muovere uno studente dalla entrance space alla ISLAND

              supposizioni: il client ha già scelto il colore tra quelli disponibili, ed il
              server lo può trovare in currentPlayerData.getChoosedColor()
             */
                String type = currentPlayerData.getTypeOfRequest();
                System.out.println("HO RICEVUTO " + type + " " + currentPlayerData.getChoosedColor());
                if (type.equals("SCHOOL")) {
                    currentPlayer.getSchoolBoard().load_dinner(currentPlayerData.getChoosedColor());
                    if (model.getGameMode().equals(GameMode.EXPERT) && currentPlayer.getSchoolBoard().getDinnerTable().numStudentsbycolor(currentPlayerData.getChoosedColor()) % 3 == 0) {
                        currentPlayer.improveCoin();
                    }
                    model.getTable().checkProfessor(currentPlayerData.getChoosedColor(), model.getPlayers());
                } else if (type.equals("ISLAND")) {
                    model.getTable().load_island(currentPlayer, currentPlayerData.getChoosedColor(), currentPlayerData.getChoosedIsland());
                } else {
                    i--;
                    for (int j = 0; j < model.getTable().getCharachter().size(); j++) {
                        if (model.getTable().getCharachter().get(j).getName().equals(type)) {
                            switch (type) {
                                case "MUSHROOMHUNTER":
                                    ((MushroomHunter) model.getTable().getCharachter().get(j)).useEffect(currentPlayer, currentPlayerData.getChoosedColor(), model.getTable());
                                    break;
                                case "THIEF":
                                    ((Thief) model.getTable().getCharachter().get(j)).useEffect(currentPlayer, model.getPlayers(), currentPlayerData.getChoosedColor(), model.getTable());
                                    break;
                                case "CENTAUR":
                                    ((Centaur) model.getTable().getCharachter().get(j)).useEffect(currentPlayer, model.getTable());
                                    break;
                                case "FARMER":
                                    ((Farmer) model.getTable().getCharachter().get(j)).useEffect(currentPlayer, model.getTable(), model.getPlayers());
                                    break;
                                case "KNIGHT":
                                    ((Knight) model.getTable().getCharachter().get(j)).useEffect(currentPlayer, model.getTable());
                                    break;
                                case "MINSTRELL":
                                    ((Minstrell) model.getTable().getCharachter().get(j)).useEffect(currentPlayer, currentPlayerData.getColors2(), currentPlayerData.getColors1(),model.getTable(),model.getPlayers());
                                    break;
                                case "JESTER":
                                    ((Jester) model.getTable().getCharachter().get(j)).useEffect(currentPlayer, currentPlayerData.getColors2(), currentPlayerData.getColors1());
                                    break;
                                case "POSTMAN":
                                    ((Postman) model.getTable().getCharachter().get(j)).useEffect(currentPlayer);
                                    break;
                                case "PRINCESS":
                                    ((Princess) model.getTable().getCharachter().get(j)).useEffect(currentPlayer, currentPlayerData.getChoosedColor(), model.getTable(), model.getPlayers());
                                    break;
                                case "GRANNY":
                                    ((Granny) model.getTable().getCharachter().get(j)).useEffect(currentPlayer, currentPlayerData.getChoosedIsland(), model.getTable());
                                    break;
                                case "MONK":
                                    ((Monk) model.getTable().getCharachter().get(j)).useEffect(currentPlayer, currentPlayerData.getChoosedColor(), currentPlayerData.getChoosedIsland(), model.getTable());
                                    break;
                                case "HERALD":
                                    boolean check = ((Herald) model.getTable().getCharachter().get(j)).useEffect(currentPlayer, currentPlayerData.getChoosedIsland(), model);
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

            else {
                int check = 0;
                if (model.getNumberOfPlayers() == 4) {
                    for (Team team : model.getTeams()) {
                        if (!team.getPlayer1().isDisconnected() || !team.getPlayer2().isDisconnected()) {
                            check++;
                        }
                    }
                } else {
                    for (Player p : model.getPlayers()) {
                        if (!p.isDisconnected()) {
                            check++;
                        }
                    }
                }
                if (check <= 1) {
                    check = 0;
                    if (model.getNumberOfPlayers() == 4) {
                        for (Team team : model.getTeams()) {
                            if (!team.getPlayer1().isDisconnected() || !team.getPlayer2().isDisconnected()) {
                                check++;
                            }
                        }
                    } else {
                        for (Player p : model.getPlayers()) {
                            if (!p.isDisconnected()) {
                                check++;
                            }
                        }
                    }
                    if (check <= 1) {
                        System.out.println("numero minimo di giocatori non disponibile, attendo 40 secondi in attesa che un altro giocatore si riconnette");
                        for (Player p : model.getPlayers()) {
                            ClientModel Data = connectionModel.findPlayer(p.getNickname());

                            Data.setTypeOfRequest("TRYTORECONNECT");
                            Data.setServermodel(model);
                            Data.setResponse(false);
                            Data.setPingMessage(false);

                            Network.send(json.toJson(Data));
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
                break;
            }
        }

        //scoppia evento fine fase

        studentPhaseEnded.fireStateEvent();
        return super.entryAction(cause);
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