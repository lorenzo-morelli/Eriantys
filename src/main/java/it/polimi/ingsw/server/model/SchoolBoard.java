package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.server.model.enums.TowerColor;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents the data model of the "school board",
 * which is nothing more than a special accumulator of students
 *
 * @author Ignazio Neto Dell'Acqua
 */
public class SchoolBoard {
    private final StudentSet entranceSpace;
    private final StudentSet dinnerTable;
    private final TowerColor towerColor;
    private int numOfTowers;
    private final boolean showTower;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_WHITE = "\033[1;97m";
    public static final String ANSI_GRAY = "\033[1;90m";

    /**
     * The constructor permit to Set up the School Board in function of numOfPlayers and available towers for 2/3 player.
     * The students are taking from the bag of the centre table
     */
    public SchoolBoard(int numOfPlayer, StudentSet bag, ArrayList<TowerColor> availableTower) {
        this.entranceSpace = new StudentSet();
        if (numOfPlayer == 3) {
            this.entranceSpace.setStudentsRandomly(9, bag);
            this.numOfTowers = 6;
        } else {
            this.entranceSpace.setStudentsRandomly(7, bag);
            this.numOfTowers = 8;
        }
        this.dinnerTable = new StudentSet();
        int rnd = new Random().nextInt(availableTower.size());
        this.towerColor = availableTower.get(rnd);
        availableTower.remove(rnd);
        showTower = true;
    }

    /**
     * The constructor permit to Set up the School Board in function of numOfPlayers and available towers for 4 player.
     * The students are taking from the bag of the centre table
     */
    public SchoolBoard(Team team, StudentSet bag, ArrayList<TowerColor> availableTower) {
        this.entranceSpace = new StudentSet();
        this.entranceSpace.setStudentsRandomly(7, bag);
        this.numOfTowers = 8;
        this.dinnerTable = new StudentSet();
        if (team.getPlayer2() == null) {
            int rnd = new Random().nextInt(availableTower.size());
            this.towerColor = availableTower.get(rnd);
            availableTower.remove(rnd);
        } else {
            this.towerColor = team.getPlayer1().getSchoolBoard().getTowerColor();
        }
        showTower = false;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public void placeTower() {
        this.numOfTowers = numOfTowers - 1;
    }

    public void removeTower() {
        this.numOfTowers = numOfTowers + 1;
    }

    /**
     * This method permit to remove one student of @param color from Entrance Table and put it on Dinner Table
     */
    public void loadDinnerTable(PeopleColor color) {
        entranceSpace.removeStudent(1, color);
        dinnerTable.addStudents(1, color);
    }

    /**
     * This method permit to load the students from the students' accumulator of the chosen @param cloud
     */
    public void loadEntrance(Cloud cloud, ArrayList<Cloud> clouds) {
        entranceSpace.addStudents(cloud.getStudentsAccumulator().numStudentsByColor(PeopleColor.RED), PeopleColor.RED);
        entranceSpace.addStudents(cloud.getStudentsAccumulator().numStudentsByColor(PeopleColor.PINK), PeopleColor.PINK);
        entranceSpace.addStudents(cloud.getStudentsAccumulator().numStudentsByColor(PeopleColor.GREEN), PeopleColor.GREEN);
        entranceSpace.addStudents(cloud.getStudentsAccumulator().numStudentsByColor(PeopleColor.BLUE), PeopleColor.BLUE);
        entranceSpace.addStudents(cloud.getStudentsAccumulator().numStudentsByColor(PeopleColor.YELLOW), PeopleColor.YELLOW);
        for (Cloud c : clouds) {
            if (cloud.getStudentsAccumulator().equals(c.getStudentsAccumulator())) {
                c.getStudentsAccumulator().setAllStudentToZero();
                break;
            }
        }
    }

    public StudentSet getDinnerTable() {
        return dinnerTable;
    }

    public StudentSet getEntranceSpace() {
        return entranceSpace;
    }

    public int getNumOfTowers() {
        return numOfTowers;
    }

    @Override
    public String toString() {
        return "    ENTRANCE : " + entranceSpace.toString() +
                "    DINNER   : " + dinnerTable.toString() +
                (showTower ? "    TOWERS : " + numOfTowers + "\n" +
                        "    TOWER COLOR : " + (towerColor == null ? " Null" : (towerColor == TowerColor.GREY ? ANSI_GRAY + towerColor + ANSI_RESET : (towerColor == TowerColor.WHITE ? ANSI_WHITE + towerColor + ANSI_RESET : ANSI_BLACK + towerColor + ANSI_RESET))) : "") +
                '\n';
    }
}
