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

    public PeopleColor estractRandomStudent(ArrayList<PeopleColor> avaiableColor) {
        if(avaiableColor.size()>0) {
            int rnd = new Random().nextInt(avaiableColor.size());
            removestudent(1, avaiableColor.get(rnd));
            PeopleColor color= avaiableColor.get(rnd);
            if (numStudentsbycolor(avaiableColor.get(rnd)) == 0) {
                avaiableColor.remove(rnd);
            }
            return color;
        }
        throw new IllegalArgumentException();
    }

    public int numStudentsbycolor(PeopleColor color){
        switch (color){
            case BLUE: return numOfBlueStudents;
            case RED: return numOfRedStudents;
            case YELLOW: return numOfYellowStudents;
            case PINK: return numOfPinkStudents;
            case GREEN: return numOfGreenStudents;
        }
        throw new IllegalArgumentException();
    }
    public int size(){
        return numStudentsbycolor(PeopleColor.BLUE)+numStudentsbycolor(PeopleColor.RED)+numStudentsbycolor(PeopleColor.YELLOW)+numStudentsbycolor(PeopleColor.PINK)+numStudentsbycolor(PeopleColor.GREEN);
    }
    public void removestudent(int n, PeopleColor color){
        if(numStudentsbycolor(color)<n){
            addstudents(-numStudentsbycolor(color),color);
            return;
        }
        addstudents(-n,color);
    }
    public void addstudents(int n, PeopleColor color){
        switch (color){
            case BLUE: numOfBlueStudents+=n;
            break;
            case RED: numOfRedStudents+=n;
            break;
            case YELLOW: numOfYellowStudents+=n;
            break;
            case PINK: numOfPinkStudents+=n;
            break;
            case GREEN: numOfGreenStudents+=n;
            break;
        }
    }

    public void setStudentsRandomly(int n,StudentSet bag,ArrayList<PeopleColor> avaiableColor) {//rivedere
        for(int i=0;i<n;i++){
            addstudents(1,bag.estractRandomStudent(avaiableColor));
        }
    }

}
