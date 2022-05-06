package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.GameMode;

import java.util.ArrayList;

import static java.util.Collections.shuffle;
import static java.util.Collections.sort;

public class Model {

    private GameMode gameMode;
    private final int numberOfPlayers;
    private int turnNumber;
    private final CenterTable table;
    private int currentplayer;
    private final ArrayList<Team> teams;
    private final ArrayList<Player> players;

    public Model(int numofplayer, String gamemode) throws Exception {

        this.currentplayer=0;
        this.players =new ArrayList<>();
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
        this.table = new CenterTable(numofplayer,this.gameMode);
        if(numofplayer==4) {
            teams = new ArrayList<>();
            teams.add(new Team(1));
            teams.add(new Team(2));
        }else{
            teams=null;
        }
    }

    public static Model createModel(int numofplayer, String Gamemode) throws Exception {
        if(numofplayer<5 && numofplayer>1 && (Gamemode=="PRINCIPIANT" || Gamemode=="EXPERT")) {
            return new Model(numofplayer, Gamemode);
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public CenterTable getTable() {
        return table;
    }
    public void randomschedulePlayers(){
        shuffle(players);
    }

    public void schedulePlayers(){
        sort(players);
    }

    public Player getcurrentPlayer(){
        return players.get(currentplayer);
    }

    public void nextPlayer(){
        if(currentplayer==(players.size()-1)){
            currentplayer=0;
        }
        else{
            currentplayer++;
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getTurnNumber() {
        return turnNumber;
    }
    public void nextTurn(){
        turnNumber++;
    }
}
