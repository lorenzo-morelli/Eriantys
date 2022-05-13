package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.TowerColor;

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

    public int setPlayer(Player player){
        if(player1==null){
            player1=player;
            return 1;
        }
        else {
            player2=player;
            return 2;
        }
    }

    public int getTeamNumber() {
        return teamNumber;
    }
    public boolean isFull(){
        return player1 != null && player2 != null;
    }

    @Override
    public String toString() {
        return "  TEAM " + teamNumber + " \n\n" +
                player1.toString() +
                player2.toString() +
                "    TOWERS : " + player1.getSchoolBoard().getNumOfTowers() + "\n" +
                        "    TOWER COLOR : " + (player1.getSchoolBoard().getTowerColor()==null ? " Null" : (player1.getSchoolBoard().getTowerColor()== TowerColor.GREY ? ANSI_GRAY+player1.getSchoolBoard().getTowerColor()+ANSI_RESET :(player1.getSchoolBoard().getTowerColor()==TowerColor.WHITE ? ANSI_WHITE+player1.getSchoolBoard().getTowerColor()+ANSI_RESET :  ANSI_BLACK+player1.getSchoolBoard().getTowerColor()+ANSI_RESET)))  +
                "\n\n\n";
    }
}
