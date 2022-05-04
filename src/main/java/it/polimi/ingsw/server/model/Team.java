package it.polimi.ingsw.server.model;

public class Team {
    private Player player1;
    private Player player2;
    private final int teamNumber;

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
}
