package it.polimi.ingsw.dataobjects;

public class Island {
    private StudentSet inhabitants;
    private int numberOfTowers;
    private TowerColor towerColor;
    private boolean isBlocked;
    public Island(){
        //inizializzazione
        this.inhabitants=new StudentSet();
        this.inhabitants.setAllStudentsToZero();
        this.numberOfTowers = 0;
        this.towerColor = TowerColor.unknown;
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

    public void setInhabitants(StudentSet inhabitants) {
        this.inhabitants = inhabitants;
    }

    public int getNumberOfTowers() {
        return numberOfTowers;
    }

    public void setNumberOfTowers(int numberOfTowers) {
        this.numberOfTowers = numberOfTowers;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
