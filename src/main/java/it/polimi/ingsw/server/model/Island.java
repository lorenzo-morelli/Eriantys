package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.server.model.enums.TowerColor;

import java.util.ArrayList;

public class Island {
    private StudentSet inhabitants;
    private int numberOfTowers;
    private TowerColor towerColor;
    private boolean isBlocked;

    public Island(int initialstudent, StudentSet bag, ArrayList<PeopleColor> avaiablecolor) {
        this.inhabitants = new StudentSet();
        this.inhabitants.setStudentsRandomly(initialstudent,bag,avaiablecolor);
        this.numberOfTowers = 0;
        this.towerColor = null;
        this.isBlocked = false;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public void setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
    }

    public StudentSet getInhabitants() {
        return inhabitants;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
    public Player player_influence(ArrayList<Player> Players, ArrayList<Professor> Professors) { //ritorna nullo se nessuno ha influenza
        int max = 0, partial_sum = 0;   //2 or 3 player
        Player max_influence = null;
        for (Player player : Players) {
            if (player.getTowerColor().equals(towerColor)) partial_sum++;
            for (Professor professor : Professors) {
                if (player.equals(professor.getHeldBy())) {
                    partial_sum = partial_sum + inhabitants.numStudentsbycolor(professor.getColor());
                }
            }
            if (partial_sum > max) {
                max = partial_sum;
                max_influence = player;
            } else if (partial_sum == max) {
                max_influence = null;
            }
            partial_sum = 0;
        }
        return max_influence; //ritorna player con piu influenza
    }

    public Team team_influence(ArrayList<Team> Teams, ArrayList<Professor> Professors){ //ritorna nullo se nessuno ha influenza
        int max=0, partial_sum=0;   //4 player
        Team max_influence = null;

        for (Team team : Teams) {
            if (team.getPlayer1().getTowerColor().equals(towerColor)) partial_sum++;
            for (Professor professor : Professors) {
                if (team.getPlayer1().equals(professor.getHeldBy()) || team.getPlayer2().equals(professor.getHeldBy())) {
                    partial_sum = partial_sum + inhabitants.numStudentsbycolor(professor.getColor());
                }
            }
            if (partial_sum > max) {
                max = partial_sum;
                max_influence = team;
            } else if (partial_sum == max) {
                max_influence = null;
            }
            partial_sum = 0;
        }
        return max_influence; //ritorna team con piu influenza
    }
    public void placeTower(){ this.numberOfTowers ++; }
    public void controllIsland(Player influence_player){
        setTowerColor(influence_player.getSchoolBoard().getTowerColor());
        influence_player.getSchoolBoard().placeTower();
    }
}
