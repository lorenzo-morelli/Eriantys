package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.GameMode;

import java.util.ArrayList;

import static java.util.Collections.shuffle;
import static java.util.Collections.sort;

/**
 * This class represents the data model of the server related to the game, with all the components necessary
 * for its correct functioning (players, center table, and so on)
 *
 * @author Ignazio Neto Dell'Acqua
 */
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

    /**
     * The constructor permit to Set up the Model in function of numOfPlayers and game mode.
     * In particular this method create the CentreTable of the game and the array containing the players, and the array containing the teams if the numOfPlayer is 4
     * @param debug is used for testing to unlock all the 12 cards (and not only 3) and set all the of the player coins to 100
     */
    public Model(int numOfPlayer, String gameMode, boolean debug) {

//        debug=true; //TODO: DELETE AFTER TESTING
        this.isLastTurn = false;
        this.currentPlayer = 0;
        this.players = new ArrayList<>();
        this.numberOfPlayers = numOfPlayer;
        switch (gameMode) {
            case "BEGINNER":
                this.gameMode = GameMode.BEGINNER;
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

    /**
     * This method allow to shuffle the array list of the player to randomize the first one who start the game
     */
    public void randomSchedulePlayers() {
        shuffle(players);
    }

    /**
     * This method allow to sort the array list of the player (player are comparable) in ascend order of the chosenCardValue of those
     */
    public void schedulePlayers() {
        sort(players);
    }

    /**
     * Return the turn player from the array
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    /**
     * This method permit to change the currentPlayer of the array (the next one in cycle order)
     */
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

    /**
     * This method improve the number of turn (utils for CLI graphics)
     */
    public void nextTurn() {
        turnNumber++;
    }

    /**
     * This method will set the turn of the game as "LAST TURN", in order to deal to particular cases and graphics who happened on it
     */
    public void setLastTurn() {
        this.isLastTurn = true;
    }
    public boolean isLastTurn(){return isLastTurn;}

    /**
     * Return the winning player in order to the rules.
     * @return winner player or DRAW if no one win
     */

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

        if (winner == null) return "DRAW";
        else return winner.getNickname();
    }

    /**
     * Return the winning team (the name of each player) in order to the rules.
     * @return winner team or DRAW if no one win
     */
    public String teamWinner() {
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

        if (winner == null) return "DRAW";
        else return winner.getPlayer1().getNickname() + " AND " + winner.getPlayer2().getNickname();
    }

    /**
     * This method permit to show all the CLI Model View
     * @param nickname the nickname of the player of the CLI that call this method
     * @param message log message to print in video
     * @return CLI Model View
     */
    public String toString(String nickname,String message) {
        return "\n\n\n-----------------------------------------GAME-----------------------------------------------------------------------------------------------------------------------------------------\n\n" +
                "    TURN = " + turnNumber + "    " + (isLastTurn ? " ! LAST TURN OF THE GAME ! ": "" ) +"\n\n" +
                table.toString(isLastTurn) +
                ( numberOfPlayers==4 ? "-----------------TEAM-----------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + printTeam(nickname) : "-----------------PLAYER---------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n"+ printPlayers(nickname) )+
                "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" +"--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n"+ message+ "\n" ;


    }

    /**
     * Util method for the CLI View graphics to represent the teams and the players in those (4 Player only)
     * @param nickname the nickname of the player of the CLI that call this method
     * @return Team View
     */
    public String printTeam(String nickname){
        StringBuilder result= new StringBuilder();
        for (Team team : teams) {
            result.append(team.toString(nickname, getCurrentPlayer().getNickname()));
        }
        return result.toString();
    }

    /**
     * Util method for the CLI View graphics to represent the players (3 or 4 players)
     * @param nickname the nickname of the player of the CLI that call this method
     * @return Players View
     */

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

    /**
     * @param disconnection is true if the game is to close cause disconnection (default false)
     */
    public void setDisconnection(boolean disconnection) {
        this.disconnection = disconnection;
    }

    /**
     * @return true if the game is to close cause disconnection (default false)
     */
    public boolean isDisconnection() {
        return disconnection;
    }
}
