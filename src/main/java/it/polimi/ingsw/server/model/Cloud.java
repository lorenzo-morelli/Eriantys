package it.polimi.ingsw.server.model;

public class Cloud {
    private StudentSet studentsAccumulator;  // accumulatore per la carica e scarica di studenti

    public Cloud(){
        this.studentsAccumulator = new StudentSet();
        //inizializzo a 0 la popolazione (uccido tutti gli abitanti)
        this.studentsAccumulator.setAllStudentsToZero();
    }

    public void setStudents(StudentSet studentsAccumulator) {
        this.studentsAccumulator = studentsAccumulator;
    }

    public StudentSet getStudentsAccumulator() {
        return studentsAccumulator;
    }
}
