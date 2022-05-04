package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.server.model.enums.TowerColor;

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

    public void setAllStudentsToZero() {
        setNumOfBlueStudents(0);
        setNumOfGreenStudents(0);
        setNumOfPinkStudents(0);
        setNumOfRedStudents(0);
        setNumOfYellowStudents(0);
    }

    public PeopleColor estractRandomStudent(ArrayList<PeopleColor> avaiableColor){
        int rnd = new Random().nextInt(avaiableColor.size());
        if(numofbycolor(PeopleColor.values()[rnd])==0) avaiableColor.remove(PeopleColor.values()[rnd]);
        removestudent(PeopleColor.values()[rnd]);
        return PeopleColor.values()[rnd];
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
    public int numofbycolor(PeopleColor color){
        switch (color){
            case BLUE: return getNumOfBlueStudents();
            case RED: return getNumOfRedStudents();
            case YELLOW: return getNumOfYellowStudents();
            case PINK: return getNumOfPinkStudents();
            case GREEN: return getNumOfGreenStudents();
        }
        return 0;
    }
    public int numStudentsbycolor(PeopleColor color){
        switch (color){
            case BLUE: return getNumOfBlueStudents();
            case RED: return getNumOfRedStudents();
            case YELLOW: return getNumOfYellowStudents();
            case PINK: return getNumOfPinkStudents();
            case GREEN: return getNumOfGreenStudents();
        }
        return 0;
    }

    public Player player_influence(TowerColor tower, ArrayList<Player> Players, ArrayList<Professor> Professors){ //ritorna nullo se nessuno ha influenza
        int max=0, partial_sum=0;   //2 or 3 player
        Player max_influence = null;
            for(int j=0;j<Players.size();j++) {
                   if(Players.get(j).getTowerColor().equals(tower)) partial_sum++;
                   for (int i = 0; i < Professors.size(); i++) {
                        if(Players.get(j).equals(Professors.get(i).getHeldBy())) partial_sum = partial_sum + numStudentsbycolor(Professors.get(i).getColor());
                   }
                   if(partial_sum>max) {
                       max=partial_sum;
                       max_influence=Players.get(j);
                   }
                   if(partial_sum==max){
                       max_influence=null;
                   }
                    partial_sum=0;
               }
        return max_influence; //ritorna player con piu influenza
         }

    public Team team_influence(TowerColor tower, ArrayList<Team> Teams, ArrayList<Professor> Professors){ //ritorna nullo se nessuno ha influenza
        int max=0, partial_sum=0;   //2 or 3 player
        Team max_influence = null;
        for(int j=0;j<Teams.size();j++) {
            if(Teams.get(j).getPlayer1().getTowerColor().equals(tower)) partial_sum++;
            for (int i = 0; i < Professors.size(); i++) {
                if(Teams.get(j).getPlayer1().equals(Professors.get(i).getHeldBy()) || Teams.get(j).getPlayer2().equals(Professors.get(i).getHeldBy())) partial_sum = partial_sum + numStudentsbycolor(Professors.get(i).getColor());
            }
            if(partial_sum>max) {
                max=partial_sum;
                max_influence=Teams.get(j);
            }
            if(partial_sum==max){
                max_influence=null;
            }
            partial_sum=0;
        }
        return max_influence; //ritorna team con piu influenza
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
