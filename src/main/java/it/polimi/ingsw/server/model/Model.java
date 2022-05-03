package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.GameMode;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Vector;

public class Model {

    private GameMode gameMode;
    private int numberOfPlayers;
    private int turnNumber;
    private CenterTable table;

    private Optional<Vector<Team>> teams; //TODO
    private ArrayList<Player> Player;
    public Model(int numofplayer, String gamemode) {

        this.Player=new ArrayList<>();
        this.numberOfPlayers= numofplayer;
        switch(gamemode) {
            case "PRINCIPIANT":
                gameMode = GameMode.PRINCIPIANT;
                break;
            case "EXPERT":
                gameMode = GameMode.EXPERT;
                break;
        }
        this.turnNumber=0;
        this.table = new CenterTable(numofplayer);
    }

    public static Model createModel(int numofplayer, String Gamemode) {
                return new Model(numofplayer,Gamemode);
    }
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setTeams(Optional<Vector<Team>> teams) {
        this.teams = teams;
    }

    public Optional<Vector<Team>> getTeams() {
        return teams;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public ArrayList<it.polimi.ingsw.server.model.Player> getPlayer() {
        return Player;
    }

    public void setPlayer(ArrayList<it.polimi.ingsw.server.model.Player> player) {
        Player = player;
    }
//    public boolean isLastPlayerOfTheTurn(){
//        //todo
//    }
//    public boolean isFirsPlayerOfTheTurn(){
//        //todo
//    }
//    public void update(){
//        // todo, or just an idea??
//        /* aggiorna currentOrderNumber nel caso in cui
//           isLastPlayerOfTheTurn è falso
//         */
//    }
}
