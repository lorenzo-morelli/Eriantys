package it.polimi.ingsw.server.model;

public class Cloud {
    private final StudentSet studentsAccumulator;  // accumulator for charging and discharging students
    private final int cloudSize;
    public Cloud(int numOfPlayers){
        if(numOfPlayers ==3) {
            this.cloudSize = 4;
        }else{
            this.cloudSize =3;
        }
        this.studentsAccumulator = new StudentSet();
        //initialize the population to 0 (kill all the inhabitants)
    }

    public StudentSet getStudentsAccumulator() {
        return studentsAccumulator;
    }
    public boolean charge(StudentSet bag) {
        if(bag.size()>= cloudSize) {
            this.studentsAccumulator.setStudentsRandomly(cloudSize, bag);
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
