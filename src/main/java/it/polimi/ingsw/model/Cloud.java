package it.polimi.ingsw.model;

public class Cloud {
    StudentSet studentsAccumulator;  // accumulatore per la carica e scarica di studenti

    public Cloud(){
        this.studentsAccumulator = new StudentSet();
        //inizializzo a 0 la popolazione (uccido tutti gli abitanti)
        this.studentsAccumulator.setAllPopulationToZero();
    }

    public int get(Color color){
        return this.studentsAccumulator.getCardinality(color);
    }

    public void set(int n, Color color){
        this.studentsAccumulator.setCardinality(n,color);
    }
}
