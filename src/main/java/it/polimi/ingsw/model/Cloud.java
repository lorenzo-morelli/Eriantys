package it.polimi.ingsw.model;

public class Cloud {
    StudentSet studentAccumulator;  // accumulatore per la carica e scarica di studenti

    public Cloud(){
        this.studentAccumulator = new StudentSet();
        //inizializzo a 0 la popolazione (rado al suolo gli abitanti)
        this.studentAccumulator.setAllPopulationToZero();
    }

    public void setStudentAccumulator(StudentSet studentAccumulator) {
        this.studentAccumulator = studentAccumulator;
    }

    public StudentSet getStudentAccumulator() {
        return studentAccumulator;
    }
}
