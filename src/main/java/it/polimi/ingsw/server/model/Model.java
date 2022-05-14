package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.GameMode;

import java.util.ArrayList;
import java.util.Objects;

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
    boolean islastturn;
    public Model(int numofplayer, String gamemode) {

        this.islastturn = false;
        this.currentplayer = 0;
        this.players = new ArrayList<>();
        this.numberOfPlayers = numofplayer;
        switch (gamemode) {
            case "PRINCIPIANT":
                gameMode = GameMode.PRINCIPIANT;
                break;
            case "EXPERT":
                gameMode = GameMode.EXPERT;
                break;
        }
        this.turnNumber = 0;
        this.table = new CenterTable(numofplayer, this.gameMode);
        if (numofplayer == 4) {
            teams = new ArrayList<>();
            teams.add(new Team(1));
            teams.add(new Team(2));
        } else {
            teams = null;
        }
    }

    public static Model createModel(int numofplayer, String Gamemode) {
        if (numofplayer < 5 && numofplayer > 1 && (Objects.equals(Gamemode, "PRINCIPIANT") || Objects.equals(Gamemode, "EXPERT"))) {
            return new Model(numofplayer, Gamemode);
        } else {
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

    public void randomschedulePlayers() {
        shuffle(players);
    }

    public void schedulePlayers() {
        sort(players);
    }

    public Player getcurrentPlayer() {
        return players.get(currentplayer);
    }

    public void nextPlayer() {
        if (currentplayer == (players.size() - 1)) {
            currentplayer = 0;
        } else {
            currentplayer++;
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void nextTurn() {
        turnNumber++;
    }

    public void setlastturn() {
        this.islastturn = true;
    }
    public boolean islastturn(){return islastturn;}

    public String player_winner() {
        int min = 8;
        Player winner = null;
        for (Player player : players) {
            if (player.getSchoolBoard().getNumOfTowers() < min) {
                min = player.getSchoolBoard().getNumOfTowers();
                winner = player;
            } else if (player.getSchoolBoard().getNumOfTowers() == min) {
                winner = null;
            }
        }
        if (winner == null) {
            int max = 0;
            for (Player player : players) {
                int numProf = 0;
                for (int i = 0; i < getTable().getProfessors().size(); i++) {
                    if (getTable().getProfessors().get(i).getHeldBy().equals(player)) {
                        numProf++;
                    }
                }
                if (numProf > max) {
                    max = numProf;
                    winner = player;
                } else if (numProf == max) {
                    winner = null;
                }
            }
        }

        if (winner == null) return "NON C'E' NESSUN VINCITORE";
        else return winner.getNickname();
    }

    public String team_winner() {
        int min = 8;
        Team winner = null;
        for (Team team : teams) {
            if (team.getPlayer1().getSchoolBoard().getNumOfTowers() < min) {
                min = team.getPlayer1().getSchoolBoard().getNumOfTowers();
                winner = team;
            } else if (team.getPlayer1().getSchoolBoard().getNumOfTowers() == min) {
                winner = null;
            }
        }
        if (winner == null) {
            int max = 0;
            for (Team team : teams) {
                int numProf = 0;
                for (int i = 0; i < getTable().getProfessors().size(); i++) {
                    if (getTable().getProfessors().get(i).getHeldBy().equals(team.getPlayer1()) || getTable().getProfessors().get(i).getHeldBy().equals(team.getPlayer2())) {
                        numProf++;
                    }
                }
                if (numProf > max) {
                    max = numProf;
                    winner = team;
                } else if (numProf == max) {
                    winner = null;
                }
            }
        }

        if (winner == null) return "NON C'E' NESSUN VINCITORE";
        else return winner.getPlayer1().getNickname() + " " + winner.getPlayer2().getNickname();
    }

    public String toString() {
        return "\n\n\n-----------------------------------------GAME-----------------------------------------------------------------------------------------------------------------------------------------\n\n" +
                "    TURN = " + turnNumber + "    " + (islastturn ? " ! LAST TURN OF THE GAME ! ": "" ) +"\n\n" +
                table.toString() +
                ( numberOfPlayers==4 ? "-----------------TEAM-----------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + printteam() : "-----------------PLAYER---------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n"+ printplayers() )+
                "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n"  + "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n";


    }

    public String printteam(){
        StringBuilder result= new StringBuilder();
        for (Team team : teams) {
            result.append(team.toString());
        }
        return result.toString();
    }

    public String printplayers(){
        StringBuilder result= new StringBuilder();
        for(Player player: players){
            result.append(player.toString());
        }
        return result.toString();
    }
}
