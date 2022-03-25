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


    public int getEntranceSpacePopulation(Color color){
        return get(color, this.entranceSpace);
    }

    public void setDinnerTablePopulation(int n, Color color){
        set(n,color,this.dinnerTable);
    }

    public int getDinnerTablePopulation(Color color){
        return get(color, this.dinnerTable);
    }

    public void setEntranceSpacePopulation(int n, Color color){
        set(n,color,this.entranceSpace);
    }

    private int get(Color color, StudentSet object) {
            return object.getCardinality(color);
    }


    private void set(int n, Color color, StudentSet object) {
            object.setCardinality(n, color);
    }
}
