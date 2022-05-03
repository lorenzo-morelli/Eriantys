package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;

public class StudentSet {
    private int numOfRedStudents;
    private int numOfYellowStudents;
    private int numOfBlueStudents;
    private int numOfPinkStudents;
    private int numOfGreenStudents;
    public StudentSet(){}

    public StudentSet(int numOfRedStudents,int numOfYellowStudents,int numOfBlueStudents,int numOfPinkStudents,int numOfGreenStudents) {
                this.numOfRedStudents=numOfRedStudents;
                this.numOfGreenStudents=numOfGreenStudents;
                this.numOfBlueStudents=numOfBlueStudents;
                this.numOfPinkStudents=numOfPinkStudents;
                this.numOfYellowStudents=numOfYellowStudents;
    }

    public void setStudents(int n, PeopleColor color) {
        if (color == PeopleColor.BLUE) {
            setNumOfBlueStudents(n);
        }
        if (color == PeopleColor.YELLOW) {
            setNumOfYellowStudents(n);
        }
        if (color == PeopleColor.GREEN) {
            setNumOfGreenStudents(n);
        }
        if (color == PeopleColor.RED) {
            setNumOfRedStudents(n);
        }
        if (color == PeopleColor.PINK) {
            setNumOfPinkStudents(n);
        }
    }

    public int getStudents(PeopleColor color) {
        if (color == PeopleColor.BLUE) {
            return this.getNumOfBlueStudents();
        }
        if (color == PeopleColor.YELLOW) {
            return this.getNumOfYellowStudents();
        }
        if (color == PeopleColor.GREEN) {
            return this.getNumOfGreenStudents();
        }
        if (color == PeopleColor.RED) {
            return this.getNumOfRedStudents();
        }

        return this.getNumOfPinkStudents();

    }

    public void setAllStudentsToZero() {
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
