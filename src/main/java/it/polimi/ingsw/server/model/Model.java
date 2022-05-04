package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.GameMode;
import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.ArrayList;

import static java.util.Collections.shuffle;
import static java.util.Collections.sort;

public class Model {

    private GameMode gameMode;
    private final int numberOfPlayers;
    private final int turnNumber;
    private final CenterTable table;

    private int currentplayer;
    private final ArrayList<Team> teams;
    private final ArrayList<Player> players;

    public Model(int numofplayer, String gamemode) {

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

    public static Model createModel(int numofplayer, String Gamemode) {
                return new Model(numofplayer,Gamemode);
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
}
