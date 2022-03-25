package it.polimi.ingsw.model;

public class SchoolBoard {
    private StudentSet entranceSpace;
    private StudentSet dinnerTable;
    private ProfessorBoard professorSpace;
    private Color towerColor;
    private int towerQuantity;

    public SchoolBoard(){
        this.entranceSpace = new StudentSet();
        this.dinnerTable = new StudentSet();
        this.professorSpace = new ProfessorBoard();
        //inizializzazione delle torri con valori nulli
        this.towerColor = Color.nullcolor;
        this.towerQuantity = 0;
    }

    public Color getTowerColor() {
        return this.towerColor;
    }

    public void setTowerColor(Color towerColor) {
        this.towerColor = towerColor;
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

    public void setProfessor(boolean bool, Color color){
        if (color == Color.red){
            this.professorSpace.setHasRedProfessor(bool);
        }
        if (color == Color.yellow){
            this.professorSpace.setHasYellowProfessor(bool);
        }
        if (color == Color.blue){
            this.professorSpace.setHasBlueProfessor(bool);
        }
        if (color == Color.green){
            this.professorSpace.setHasGreenProfessor(bool);
        }
        if (color == Color.pink){
            this.professorSpace.setHasPinkProfessor(bool);
        }
    }

    public boolean hasProfessor(Color color){
        if (color == Color.red){
            return this.professorSpace.HasRedProfessor();
        }
        if (color == Color.yellow){
            return this.professorSpace.HasYellowProfessor();
        }
        if (color == Color.blue){
            return this.professorSpace.HasBlueProfessor();
        }
        if (color == Color.green){
            return this.professorSpace.HasGreenProfessor();
        }
        if (color == Color.pink){
            return this.professorSpace.HasPinkProfessor();
        }
        //ToDo: implementare come eccezione
        return false;
    }


}
