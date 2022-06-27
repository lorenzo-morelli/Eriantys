package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.server.model.enums.TowerColor;
import java.util.ArrayList;

/**
 * This class represents the data model of islands,
 * seen as student accumulators and towers with particular properties
 *
 * @author Ignazio Neto Dell'Acqua
 */
public class Island {
    private final StudentSet inhabitants;
    private int numberOfTowers;
    private TowerColor towerColor;
    private boolean isBlocked;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String WHITE_BOLD_BRIGHT="\033[1;97m";
    public static final String ANSI_GRAY="\033[1;90m";

    public Island(int initialStudent, StudentSet bag) {
        this.inhabitants = new StudentSet();
        this.inhabitants.setStudentsRandomly(initialStudent,bag);
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

    /**
     * Method to establish if the island is blocked or not
     * @return boolean value
     */
    public boolean isBlocked() {
        return isBlocked;
    }

    /**
     * Method to set  the blocked status
     * @param blocked boolean value
     */
    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    /**
     * Calculates who has the influence on the island
     * @param Players the players to compare
     * @param Professors the professor
     * @param centaur required for effect card
     * @param mushroom required for effect card
     * @param knight required for effect card
     * @return the player that currently has the influence on the island
     */
    public Player playerInfluenceCalculator(ArrayList<Player> Players, ArrayList<Professor> Professors, boolean centaur, PeopleColor mushroom, Player knight) { //ritorna nullo se nessuno ha influenza
        int max = 0, partialSum = 0;   //2 or 3 player
        Player maxInfluence = null;
        for (Player player : Players) {
            if (player.getSchoolBoard().getTowerColor().equals(towerColor) && !centaur) partialSum +=numberOfTowers;
            if( player.equals(knight)) partialSum +=2;
            for (Professor professor : Professors) {
                if (player.equals(professor.getHeldBy()) && professor.getColor()!= mushroom) {
                    partialSum = partialSum + inhabitants.numStudentsByColor(professor.getColor());
                }
            }
            if (partialSum > max) {
                max = partialSum;
                maxInfluence = player;
            } else if (partialSum == max) {
                maxInfluence = null;
            }
            partialSum = 0;
        }
        int partialSumOfNobody =0;
            for (Professor professor : Professors) {
                if (professor.getHeldBy() == null && professor.getColor() != mushroom) {
                    partialSumOfNobody = inhabitants.numStudentsByColor(professor.getColor());
                }
                if (partialSumOfNobody >= max) {
                    maxInfluence = null;
                    break;
                }
                partialSumOfNobody =0;
            }
        return maxInfluence; //return the player with the maximum influence
    }

    /**
     * Similar to the precedent method, but for the 4 players game mode
     * @param Teams teams to compare
     * @param Professors professors boards
     * @param centaur required for effect card
     * @param mushroom required for effect card
     * @param knight required for effect card
     * @return the team with maximised influence on the island
     */
    public Team teamInfluenceCalculator(ArrayList<Team> Teams, ArrayList<Professor> Professors, boolean centaur, PeopleColor mushroom, Player knight){ //ritorna nullo se nessuno ha influenza
        int max=0, partialSum =0;   //4 player
        Team maxInfluence = null;

        for (Team team : Teams) {
            if (team.getPlayer1().getSchoolBoard().getTowerColor().equals(towerColor)&& !centaur) partialSum +=numberOfTowers;
            if( team.getPlayer1().equals(knight)) partialSum +=2;
            if( team.getPlayer2().equals(knight)) partialSum +=2;
            for (Professor professor : Professors) {
                if (team.getPlayer1().equals(professor.getHeldBy()) || team.getPlayer2().equals(professor.getHeldBy())&& professor.getColor()!= mushroom) {
                    partialSum = partialSum + inhabitants.numStudentsByColor(professor.getColor());
                }
            }
            if (partialSum > max) {
                max = partialSum;
                maxInfluence = team;
            } else if (partialSum == max) {
                maxInfluence = null;
            }
            partialSum = 0;
        }
        int partialSumOfNobody =0;
        for (Professor professor : Professors) {
            if (professor.getHeldBy() == null && professor.getColor() != mushroom) {
                partialSumOfNobody = inhabitants.numStudentsByColor(professor.getColor());
            }
            if (partialSumOfNobody >= max) {
                maxInfluence = null;
                break;
            }
            partialSumOfNobody =0;
        }
        return maxInfluence; //returns the team with the max influence
    }
    public void placeTower(){ this.numberOfTowers ++; }

    /**
     * Method to control the island, once we have determined that the player has the influence on the island
     * @param influencePlayer the player with the influence
     */
    public void controlIsland(Player influencePlayer){
        setTowerColor(influencePlayer.getSchoolBoard().getTowerColor());
        if(numberOfTowers>0){
            for(int i=0;i<numberOfTowers;i++) {
                influencePlayer.getSchoolBoard().placeTower();
            }
        }
        else{
            influencePlayer.getSchoolBoard().placeTower();
        }
    }

    /**
     * As the precedent method, but for the 4 players mode
     * @param influenceTeam the team with the influence on the island
     */
    public void controlIsland(Team influenceTeam){
        setTowerColor(influenceTeam.getPlayer1().getSchoolBoard().getTowerColor());
        if(numberOfTowers>0){
            for(int i=0;i<numberOfTowers;i++) {
                influenceTeam.getPlayer1().getSchoolBoard().placeTower();
                influenceTeam.getPlayer2().getSchoolBoard().placeTower();
            }
        }
        else{
            influenceTeam.getPlayer1().getSchoolBoard().placeTower();
            influenceTeam.getPlayer2().getSchoolBoard().placeTower();
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
