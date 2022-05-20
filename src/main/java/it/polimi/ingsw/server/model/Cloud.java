package it.polimi.ingsw.server.model;

public class Cloud {
    private final StudentSet studentsAccumulator;  // accumulatore per la carica e scarica di studenti
    private final int cloudsize;
    public Cloud(int numplayer){
        if(numplayer==3) {
            this.cloudsize = 4;
        }else{
            this.cloudsize=3;
        }
        this.studentsAccumulator = new StudentSet();
        //inizializzo a 0 la popolazione (uccido tutti gli abitanti)
    }

    public StudentSet getStudentsAccumulator() {
        return studentsAccumulator;
    }
    public boolean charge(StudentSet bag) {
        if(bag.size()>=cloudsize) {
            this.studentsAccumulator.setStudentsRandomly(cloudsize, bag);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public String toString() {
        return "    STUDENTS : " + studentsAccumulator +
                '\n';
    }

}
