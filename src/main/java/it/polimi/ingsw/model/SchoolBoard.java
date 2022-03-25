package it.polimi.ingsw.model;

public class SchoolBoard {
    private StudentSet entranceSpace;
    private StudentSet dinnerTable;
    private ProfessorBoard professorSpace;
    private Color TowerColor;
    int towerQuantity;

    public SchoolBoard(){
        this.entranceSpace = new StudentSet();
        this.dinnerTable = new StudentSet();
        this.professorSpace = new ProfessorBoard();
        //inizializzazione delle torri con valori nulli
        this.TowerColor = Color.nullcolor;
        this.towerQuantity = 0;
    }

    public StudentSet getEntranceSpace() {
        return entranceSpace;
    }

    public void setEntranceSpace(StudentSet entranceSpace) {
        this.entranceSpace = entranceSpace;
    }

    public StudentSet getDinnerTable() {
        return dinnerTable;
    }

    public void setDinnerTable(StudentSet dinnerTable) {
        this.dinnerTable = dinnerTable;
    }

    public ProfessorBoard getProfessorSpace() {
        return professorSpace;
    }

    public void setProfessorSpace(ProfessorBoard professorSpace) {
        this.professorSpace = professorSpace;
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
