package it.polimi.ingsw.dataobjects;

public class Team {
    private int player1;
    private int player2;
    private String teamName;
    private TowerColor towerColor;
    private int numOfTowers;

    public void setPlayers(int player1, int player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    private int getPlayer1() {
        return player1;
    }

    private int getPlayer2(){
        return player2;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public void setNumOfTowers(int numOfTowers) {
        this.numOfTowers = numOfTowers;
    }

    public int getNumOfTowers() {
        return numOfTowers;
    }
}
