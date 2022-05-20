package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.server.model.enums.TowerColor;
import java.util.ArrayList;

public class Island {
    private final StudentSet inhabitants;
    private int numberOfTowers;
    private TowerColor towerColor;
    private boolean isBlocked;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String WHITE_BOLD_BRIGHT="\033[1;97m";
    public static final String ANSI_GRAY="\033[1;90m";

    public Island(int initialstudent, StudentSet bag) {
        this.inhabitants = new StudentSet();
        this.inhabitants.setStudentsRandomly(initialstudent,bag);
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

    public Player player_influence(ArrayList<Player> Players, ArrayList<Professor> Professors, boolean centaur, PeopleColor mushroom, Player knight) { //ritorna nullo se nessuno ha influenza
        int max = 0, partial_sum = 0;   //2 or 3 player
        Player max_influence = null;
        for (Player player : Players) {
            if (player.getSchoolBoard().getTowerColor().equals(towerColor) && !centaur) partial_sum+=numberOfTowers;
            if( player.equals(knight)) partial_sum+=2;
            for (Professor professor : Professors) {
                if (player.equals(professor.getHeldBy()) && professor.getColor()!= mushroom) {
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
        int partial_sumofNobody=0;
            for (Professor professor : Professors) {
                if (professor.getHeldBy() == null && professor.getColor() != mushroom) {
                    partial_sumofNobody = inhabitants.numStudentsbycolor(professor.getColor());
                }
                if (partial_sumofNobody >= max) {
                    max_influence = null;
                    break;
                }
                partial_sumofNobody=0;
            }
        return max_influence; //ritorna player con piu influenza
    }

    public Team team_influence(ArrayList<Team> Teams, ArrayList<Professor> Professors, boolean centaur, PeopleColor mushroom, Player knight){ //ritorna nullo se nessuno ha influenza
        int max=0, partial_sum=0;   //4 player
        Team max_influence = null;

        for (Team team : Teams) {
            if (team.getPlayer1().getSchoolBoard().getTowerColor().equals(towerColor)&& !centaur) partial_sum+=numberOfTowers;
            if( team.getPlayer1().equals(knight)) partial_sum+=2;
            if( team.getPlayer2().equals(knight)) partial_sum+=2;
            for (Professor professor : Professors) {
                if (team.getPlayer1().equals(professor.getHeldBy()) || team.getPlayer2().equals(professor.getHeldBy())&& professor.getColor()!= mushroom) {
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
        int partial_sumofNobody=0;
        for (Professor professor : Professors) {
            if (professor.getHeldBy() == null && professor.getColor() != mushroom) {
                partial_sumofNobody = inhabitants.numStudentsbycolor(professor.getColor());
            }
            if (partial_sumofNobody >= max) {
                max_influence = null;
                break;
            }
            partial_sumofNobody=0;
        }
        return max_influence; //ritorna team con piu influenza
    }
    public void placeTower(){ this.numberOfTowers ++; }

    public void controllIsland(Player influence_player){
        setTowerColor(influence_player.getSchoolBoard().getTowerColor());
        if(numberOfTowers>0){
            for(int i=0;i<numberOfTowers;i++) {
                influence_player.getSchoolBoard().placeTower();
            }
        }
        else{
            influence_player.getSchoolBoard().placeTower();
        }
    }

    public void controllIsland(Team influence_team){
        setTowerColor(influence_team.getPlayer1().getSchoolBoard().getTowerColor());
        if(numberOfTowers>0){
            for(int i=0;i<numberOfTowers;i++) {
                influence_team.getPlayer1().getSchoolBoard().placeTower();
                influence_team.getPlayer2().getSchoolBoard().placeTower();
            }
        }
        else{
            influence_team.getPlayer1().getSchoolBoard().placeTower();
            influence_team.getPlayer2().getSchoolBoard().placeTower();
        }
    }


    public int getNumberOfTowers() {
        return numberOfTowers;
    }

    public void setNumberOfTowers(int numberOfTowers) {
        this.numberOfTowers = numberOfTowers;
    }

    @Override
    public String toString() {
        return "    INHABITANS : " + inhabitants.toString() +
                "    NUMBER OF TOWER : " + numberOfTowers + "\n"+
                "    TOWER COLOR :" +  (towerColor==null ? " Null" : (towerColor==TowerColor.GREY ? ANSI_GRAY+towerColor+ANSI_RESET :(towerColor==TowerColor.WHITE ? WHITE_BOLD_BRIGHT+towerColor+ANSI_RESET :  ANSI_BLACK+towerColor+ANSI_RESET))) +"\n"+
                (isBlocked ? "    ! QUESTA ISOLA E' BLOCCATA !\n\n" : "\n");
    }
}
