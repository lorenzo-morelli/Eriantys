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
    private int currentPlayer;
    private final ArrayList<Team> teams;
    private final ArrayList<Player> players;
    boolean isLastTurn,disconnection=false;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_WHITE="\033[1;97m";
    public static final String ANSI_GRAY="\033[1;90m";
    public Model(int numOfPlayer, String gameMode, boolean debug) {

    //    debug=true; //TODO: DELETE AFTER TESTING
        this.isLastTurn = false;
        this.currentPlayer = 0;
        this.players = new ArrayList<>();
        this.numberOfPlayers = numOfPlayer;
        switch (gameMode) {
            case "PRINCIPIANT":
                this.gameMode = GameMode.PRINCIPIANT;
                break;
            case "EXPERT":
                this.gameMode = GameMode.EXPERT;
                break;
        }
        this.turnNumber = 0;
        this.table = new CenterTable(numOfPlayer, this.gameMode, debug);
        if (numOfPlayer == 4) {
            teams = new ArrayList<>();
            teams.add(new Team(1));
            teams.add(new Team(2));
        } else {
            teams = null;
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

    public void randomSchedulePlayers() {
        shuffle(players);
    }

    public void schedulePlayers() {
        sort(players);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    public void nextPlayer() {
        if (currentPlayer == (players.size() - 1)) {
            currentPlayer = 0;
        } else {
            currentPlayer++;
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    public void nextTurn() {
        turnNumber++;
    }

    public void setLastTurn() {
        this.isLastTurn = true;
    }
    public boolean isLastTurn(){return isLastTurn;}

    public String playerWinner() {
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
                    if (getTable().getProfessors().get(i).getHeldBy()!=null && getTable().getProfessors().get(i).getHeldBy().equals(player)) {
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

        if (winner == null) return "PAREGGIO";
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
                    if (getTable().getProfessors().get(i).getHeldBy()!=null && (getTable().getProfessors().get(i).getHeldBy().equals(team.getPlayer1()) || getTable().getProfessors().get(i).getHeldBy().equals(team.getPlayer2()))) {
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

        if (winner == null) return "PAREGGIO";
        else return winner.getPlayer1().getNickname() + "+" + winner.getPlayer2().getNickname();
    }

    public String toString(String nickname,String message) {
        return "\n\n\n-----------------------------------------GAME-----------------------------------------------------------------------------------------------------------------------------------------\n\n" +
                "    TURN = " + turnNumber + "    " + (isLastTurn ? " ! LAST TURN OF THE GAME ! ": "" ) +"\n\n" +
                table.toString(isLastTurn) +
                ( numberOfPlayers==4 ? "-----------------TEAM-----------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + printTeam(nickname) : "-----------------PLAYER---------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n"+ printPlayers(nickname) )+
                "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" +"--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n"+ message+ "\n" ;


    }

    public String printTeam(String nickname){
        StringBuilder result= new StringBuilder();
        for (Team team : teams) {
            result.append(team.toString(nickname, getCurrentPlayer().getNickname()));
        }
        return result.toString();
    }

    public String printPlayers(String nickname){
        StringBuilder result= new StringBuilder();
        for(Player player: players) {
            if (player.equals(getCurrentPlayer())) {
                switch (player.getSchoolBoard().getTowerColor()) {
                    case BLACK:
                        result.append(ANSI_BLACK + "    (CURRENT PLAYER) " + ANSI_RESET);
                        break;
                    case GREY:
                        result.append(ANSI_GRAY + "    (CURRENT PLAYER) " + ANSI_RESET);
                        break;
                    case WHITE:
                        result.append(ANSI_WHITE + "    (CURRENT PLAYER) " + ANSI_RESET);
                        break;
                }
            }
            if (player.getNickname().equals(nickname)) {
                switch (player.getSchoolBoard().getTowerColor()) {
                    case BLACK:
                        result.append(ANSI_BLACK + "    (YOU) " + ANSI_RESET);
                        break;
                    case GREY:
                        result.append(ANSI_GRAY + "    (YOU) " + ANSI_RESET);
                        break;
                    case WHITE:
                        result.append(ANSI_WHITE + "    (YOU) " + ANSI_RESET);
                        break;
                }
            }
            if(player.getNickname().equals(nickname) || player.equals(getCurrentPlayer())){
                result.append("\n");
            }
            result.append(player.toString(nickname)).append("\n");
        }
        return result.toString();
    }

    public void setDisconnection(boolean disconnection) {
        this.disconnection = disconnection;
    }

    public boolean isDisconnection() {
        return disconnection;
    }
}
