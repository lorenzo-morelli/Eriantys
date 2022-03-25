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
        if (color == Color.green) {
            return object.getGreenColorSet().getCardinality();
        }
        if (color == Color.yellow) {
            return object.getYellowColorSet().getCardinality();
        }
        if (color == Color.blue) {
            return object.getBlueColorSet().getCardinality();
        }
        if (color == Color.pink) {
            return object.getPinkColorSet().getCardinality();
        }
        if (color == Color.red) {
            return object.getRedColorSet().getCardinality();
        }
        //ToDo: da gestire con una eccezione
        return -1;
    }


    private void set(int n, Color color, StudentSet object) {
        if (color == Color.green) {
            object.getGreenColorSet().setCardinality(n);
        }
        if (color == Color.yellow) {
            object.getYellowColorSet().setCardinality(n);
        }
        if (color == Color.blue) {
            object.getBlueColorSet().setCardinality(n);
        }
        if (color == Color.pink) {
            object.getPinkColorSet().setCardinality(n);
        }
        if (color == Color.red) {
            object.getRedColorSet().setCardinality(n);
        }
    }
}
