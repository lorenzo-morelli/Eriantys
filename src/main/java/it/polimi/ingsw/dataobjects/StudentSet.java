package it.polimi.ingsw.dataobjects;

public class StudentSet {
    private int numOfRedStudents;
    private int numOfYellowStudents;
    private int numOfBlueStudents;
    private int numOfPinkStudents;
    private int numOfGreenStudents;

    public StudentSet(){
        // sono interi, non c'è bisogno di costruirli
    }

    public void setStudents(int n,StudentColor color){
        if(color == StudentColor.blue){
            setNumOfBlueStudents(n);
        }
        if(color == StudentColor.yellow){
            setNumOfYellowStudents(n);
        }
        if(color == StudentColor.green){
            setNumOfGreenStudents(n);
        }
        if(color == StudentColor.red){
            setNumOfRedStudents(n);
        }
        if(color == StudentColor.pink){
            setNumOfPinkStudents(n);
        }
    }

    public int getStudents(StudentColor color) {
        if(color == StudentColor.blue){
            return this.getNumOfBlueStudents();
        }
        if(color == StudentColor.yellow){
            return this.getNumOfYellowStudents();
        }
        if(color == StudentColor.green){
            return this.getNumOfGreenStudents();
        }
        if(color == StudentColor.red){
            return this.getNumOfRedStudents();
        }

        return this.getNumOfPinkStudents();

    }

    public void setAllStudentsToZero(){
        setNumOfBlueStudents(0);
        setNumOfGreenStudents(0);
        setNumOfPinkStudents(0);
        setNumOfRedStudents(0);
        setNumOfYellowStudents(0);
    }

    private void setNumOfBlueStudents(int numOfBlueStudents) {
        this.numOfBlueStudents = numOfBlueStudents;
    }

    private int getNumOfBlueStudents() {
        return numOfBlueStudents;
    }

    private void setNumOfGreenStudents(int numOfGreenStudents) {
        this.numOfGreenStudents = numOfGreenStudents;
    }

    private int getNumOfGreenStudents() {
        return numOfGreenStudents;
    }

    private void setNumOfPinkStudents(int numOfPinkStudents) {
        this.numOfPinkStudents = numOfPinkStudents;
    }

    private int getNumOfPinkStudents() {
        return numOfPinkStudents;
    }

    private void setNumOfRedStudents(int numOfRedStudents) {
        this.numOfRedStudents = numOfRedStudents;
    }

    private int getNumOfRedStudents() {
        return numOfRedStudents;
    }

    private void setNumOfYellowStudents(int numOfYellowStudents) {
        this.numOfYellowStudents = numOfYellowStudents;
    }

    private int getNumOfYellowStudents() {
        return numOfYellowStudents;
    }
}
