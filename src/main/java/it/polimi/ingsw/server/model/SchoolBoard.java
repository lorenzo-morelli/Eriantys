package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.server.model.enums.TowerColor;

public class SchoolBoard {
    private StudentSet entranceSpace;
    private StudentSet dinnerTable;
    private TowerColor towerColor;
    private int numOfTowers;

    public SchoolBoard() {
        this.entranceSpace = new StudentSet();
        this.dinnerTable = new StudentSet();
        // inizializzazione vuota della school board
        this.entranceSpace.setAllStudentsToZero();
        this.dinnerTable.setAllStudentsToZero();
        this.towerColor = TowerColor.UNKNOWN;
        this.numOfTowers = 0;
    }

    public void setDinnerTable(StudentSet dinnerTable) {
        this.dinnerTable = dinnerTable;
    }

    public void setDinnerTable(int n, PeopleColor color) {
        this.dinnerTable.setStudents(n, color);
    }

    public void setEntranceSpace(StudentSet entranceSpace) {
        this.entranceSpace = entranceSpace;
    }

    public void setEntranceSpace(int n, PeopleColor color) {
        this.entranceSpace.setStudents(n, color);
    }

    public StudentSet getEntranceSpace() {
        return entranceSpace;
    }

    public int getEntranceSpace(PeopleColor color) {
        return this.entranceSpace.getStudents(color);
    }

    public StudentSet getDinnerTable() {
        return dinnerTable;
    }

    public int getDinnerTable(PeopleColor color) {
        return this.dinnerTable.getStudents(color);
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public void setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
    }

    public int getNumOfTowers() {
        return numOfTowers;
    }

    public void setNumOfTowers(int numOfTowers) {
        this.numOfTowers = numOfTowers;
    }
}
