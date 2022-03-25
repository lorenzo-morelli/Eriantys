package it.polimi.ingsw.model;

public class SchoolBoard {
    StudentSet entranceSpace;
    StudentSet dinnerTable;
    ProfessorBoard professorSpace;
    Color TowerColor;
    int towerQuantity;

    public SchoolBoard(){
        this.entranceSpace = new StudentSet();
        this.dinnerTable = new StudentSet();
        this.professorSpace = new ProfessorBoard();
        //inizializzazione delle torri con valori nulli
        this.TowerColor = Color.nullcolor;
        this.towerQuantity = 0;
    }

    public Color getTowerColor() {
        return TowerColor;
    }

    public void setTowerColor(Color towerColor) {
        TowerColor = towerColor;
    }

    public int getTowerQuantity() {
        return towerQuantity;
    }

    public void setTowerQuantity(int towerQuantity) {
        this.towerQuantity = towerQuantity;
    }

}
