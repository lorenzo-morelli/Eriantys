package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.ArrayList;
import java.util.Random;

public class StudentSet {
    private int numOfRedStudents;
    private int numOfYellowStudents;
    private int numOfBlueStudents;
    private int numOfPinkStudents;
    private int numOfGreenStudents;
    public StudentSet(){
        this.numOfRedStudents=0;
        this.numOfGreenStudents=0;
        this.numOfBlueStudents=0;
        this.numOfPinkStudents=0;
        this.numOfYellowStudents=0;
    }

    public StudentSet(int numOfRedStudents,int numOfYellowStudents,int numOfBlueStudents,int numOfPinkStudents,int numOfGreenStudents) {
                this.numOfRedStudents=numOfRedStudents;
                this.numOfGreenStudents=numOfGreenStudents;
                this.numOfBlueStudents=numOfBlueStudents;
                this.numOfPinkStudents=numOfPinkStudents;
                this.numOfYellowStudents=numOfYellowStudents;
    }

    public PeopleColor estractRandomStudent(ArrayList<PeopleColor> avaiableColor){
        int rnd = new Random().nextInt(avaiableColor.size());
        if(numStudentsbycolor(PeopleColor.values()[rnd])==0) avaiableColor.remove(PeopleColor.values()[rnd]);
        removestudent(PeopleColor.values()[rnd]);
        return PeopleColor.values()[rnd];
    }

    private void setNumOfBlueStudents(int numOfBlueStudents) {
        this.numOfBlueStudents = numOfBlueStudents;
    }

    private void setNumOfGreenStudents(int numOfGreenStudents) {
        this.numOfGreenStudents = numOfGreenStudents;
    }

    private void setNumOfPinkStudents(int numOfPinkStudents) {
        this.numOfPinkStudents = numOfPinkStudents;
    }

    private void setNumOfRedStudents(int numOfRedStudents) {
        this.numOfRedStudents = numOfRedStudents;
    }

    private void setNumOfYellowStudents(int numOfYellowStudents) {
        this.numOfYellowStudents = numOfYellowStudents;
    }

    public int numStudentsbycolor(PeopleColor color){
        switch (color){
            case BLUE: return numOfBlueStudents;
            case RED: return numOfRedStudents;
            case YELLOW: return numOfYellowStudents;
            case PINK: return numOfPinkStudents;
            case GREEN: return numOfGreenStudents;
        }
        return 0;
    }
    public int size(){
        return numStudentsbycolor(PeopleColor.BLUE)+numStudentsbycolor(PeopleColor.RED)+numStudentsbycolor(PeopleColor.YELLOW)+numStudentsbycolor(PeopleColor.PINK)+numStudentsbycolor(PeopleColor.GREEN);
    }
    public void removestudent(PeopleColor color){
        setStudents(numStudentsbycolor(color)-1,color);
    }
    public void addstudents(int n, PeopleColor color){
        setStudents(numStudentsbycolor(color)+n,color);
    }

    public void setStudents(int n, PeopleColor color) {
        switch (color){
            case BLUE: setNumOfBlueStudents(n);
            case RED: setNumOfRedStudents(n);
            case YELLOW: setNumOfYellowStudents(n);
            case PINK: setNumOfPinkStudents(n);
            case GREEN: setNumOfGreenStudents(n);
        }
    }

    public void setStudentsRandomly(int n,StudentSet bag,ArrayList<PeopleColor> avaiableColor){//rivedere
        for(int i=0;i<n;i++){
            addstudents(1,bag.estractRandomStudent(avaiableColor));
        }
    }

}
