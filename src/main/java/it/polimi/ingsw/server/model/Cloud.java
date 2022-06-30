package it.polimi.ingsw.server.model;

/**
 * This class models cloud tiles,
 * seen as a simple student accumulators
 *
 * @author Ignazio Neto dell'Acqua
 */
public class Cloud {
    private final StudentSet studentsAccumulator;
    private final int cloudSize;

    /**
     * The constructor build this with the cloudSize in function of the numOfPlayers as described in the rules, and also initialize the students accumulator
     */
    public Cloud(int numOfPlayers){
        if(numOfPlayers ==3) {
            this.cloudSize = 4;
        }else{
            this.cloudSize =3;
        }
        this.studentsAccumulator = new StudentSet();
    }

    public StudentSet getStudentsAccumulator() {
        return studentsAccumulator;
    }

    /**
     * This method permit tho charge the cloud (the student accumulator) by extract "cloudSize" student from the Bag (randomly)
     * @return true if there are enough students in the bag, false if not
     */
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
