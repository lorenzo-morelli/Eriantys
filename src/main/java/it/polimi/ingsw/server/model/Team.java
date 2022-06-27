package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.TowerColor;

/**
 * This class represent the data model of a team of players, necessary for the 4 game mode.
 * Each team has 2 team members (player objects) and a defined team number
 * (for this game there are only two teams so two different team numbers)
 *
 * @author Ignazio Neto Dell'Acqua
 */
public class Team {
    private Player player1;
    private Player player2;
    private final int teamNumber;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_WHITE="\033[1;97m";
    public static final String ANSI_GRAY="\033[1;90m";

    public Team(int teamNumber){
        player1=null;
        player2=null;
        this.teamNumber=teamNumber;
    }
    public Player getPlayer1() {
        return player1;
    }
    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer(Player player){
        if(player1==null){
            player1=player;
        }
        else {
            player2=player;
        }
    }

    public int getTeamNumber() {
        return teamNumber;
    }
    public boolean isFull(){
        return player1 != null && player2 != null;
    }

    public String toString(String player, String actualPlayer) {

        StringBuilder result = new StringBuilder();
        result.append("  TEAM ").append(teamNumber).append(" \n\n");
        if (getPlayer1().getNickname().equals(actualPlayer)) {
            switch (getPlayer1().getSchoolBoard().getTowerColor()) {
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
        if (getPlayer1().getNickname().equals(player)) {
            switch (getPlayer1().getSchoolBoard().getTowerColor()) {
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
        if(getPlayer1().getNickname().equals(player) || getPlayer1().getNickname().equals(actualPlayer)){
            result.append("\n");
        }
        result.append(getPlayer1().toString(actualPlayer));

        if (getPlayer2().getNickname().equals(actualPlayer)) {
            switch (getPlayer2().getSchoolBoard().getTowerColor()) {
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
        if (getPlayer2().getNickname().equals(player)) {
            switch (getPlayer2().getSchoolBoard().getTowerColor()) {
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
        if(getPlayer2().getNickname().equals(player) || getPlayer2().getNickname().equals(actualPlayer)){
            result.append("\n");
        }
        assert player1 != null;
        result.append(getPlayer2().toString(actualPlayer)).append("    TOWERS : ").append(player1.getSchoolBoard().getNumOfTowers()).append("\n").append("    TOWER COLOR : ").append(player1.getSchoolBoard().getTowerColor() == null ? " Null" : (player1.getSchoolBoard().getTowerColor() == TowerColor.GREY ? ANSI_GRAY + player1.getSchoolBoard().getTowerColor() + ANSI_RESET : (player1.getSchoolBoard().getTowerColor() == TowerColor.WHITE ? ANSI_WHITE + player1.getSchoolBoard().getTowerColor() + ANSI_RESET : ANSI_BLACK + player1.getSchoolBoard().getTowerColor() + ANSI_RESET))).append("\n\n\n");
        return result.toString();
    }

}
