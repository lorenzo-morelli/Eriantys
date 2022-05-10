package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.ArrayList;

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
    public boolean charge(StudentSet bag, ArrayList<PeopleColor> avaiablecolor) {
        if(bag.size()>=cloudsize) {
            this.studentsAccumulator.setStudentsRandomly(cloudsize, bag, avaiablecolor);
            return true;
        }
        else{
            return false;
        }
    }

    public int getCloudsize() {
        return cloudsize;
    }

    @Override
    public String toString() {
        return "    STUDENTS : " + studentsAccumulator.toString()+
                '\n';
    }
}
