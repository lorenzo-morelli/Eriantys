package it.polimi.ingsw.model;

public class Cloud {
    StudentSet studentAccumulator;  // accumulatore per la carica e scarica di studenti

    public Cloud(){
        this.studentAccumulator = new StudentSet();
    }

    public void setStudentAccumulator(StudentSet studentAccumulator) {
        this.studentAccumulator = studentAccumulator;
    }

    public StudentSet getStudentAccumulator() {
        return studentAccumulator;
    }
}
