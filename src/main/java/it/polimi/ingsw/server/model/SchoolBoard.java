package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.server.model.enums.TowerColor;

import java.util.ArrayList;
import java.util.Random;

public class SchoolBoard {
    private final StudentSet entranceSpace;
    private final StudentSet dinnerTable;
    private final TowerColor towerColor;
    private int numOfTowers;

    public SchoolBoard(int numOfPlayer, StudentSet bag, ArrayList<PeopleColor> avaiablePeopleColorinBag,ArrayList<TowerColor> avaiableTower) {
        this.entranceSpace = new StudentSet();   //2 o 3 giocatori
        if (numOfPlayer == 3) {
            this.entranceSpace.setStudentsRandomly(9, bag, avaiablePeopleColorinBag);
            this.numOfTowers = 6;
        } else {
            this.entranceSpace.setStudentsRandomly(7, bag, avaiablePeopleColorinBag);
            this.numOfTowers = 8;
        }
        this.dinnerTable = new StudentSet();
        int rnd = new Random().nextInt(avaiableTower.size());
        this.towerColor = TowerColor.values()[rnd];
        avaiableTower.remove(TowerColor.values()[rnd]);
    }
    public SchoolBoard(Team team, StudentSet bag, ArrayList<PeopleColor> avaiablePeopleColorinBag,ArrayList<TowerColor> avaiableTower) {
        this.entranceSpace = new StudentSet();    //4 giocatori
        this.entranceSpace.setStudentsRandomly(7, bag, avaiablePeopleColorinBag);
        this.numOfTowers = 8;
        this.dinnerTable = new StudentSet();
        if (!team.isFull()) {
            int rnd = new Random().nextInt(avaiableTower.size());
            this.towerColor = TowerColor.values()[rnd];
            avaiableTower.remove(TowerColor.values()[rnd]);
        } else {
            this.towerColor = team.getPlayer1().getTowerColor();
        }
    }
    public TowerColor getTowerColor() {
        return towerColor;
    }
    public void placeTower(){ this.numOfTowers = numOfTowers-1; }
    public void removeTower(){ this.numOfTowers = numOfTowers+1;}
    public void load_dinner(PeopleColor color){
        entranceSpace.removestudent(color);
        dinnerTable.addstudents(1,color);
    }

    public void load_entrance(Cloud cloud){
       entranceSpace.addstudents(cloud.getStudentsAccumulator().numStudentsbycolor(PeopleColor.RED),PeopleColor.RED);
       entranceSpace.addstudents(cloud.getStudentsAccumulator().numStudentsbycolor(PeopleColor.PINK),PeopleColor.PINK);
       entranceSpace.addstudents(cloud.getStudentsAccumulator().numStudentsbycolor(PeopleColor.GREEN),PeopleColor.GREEN);
       entranceSpace.addstudents(cloud.getStudentsAccumulator().numStudentsbycolor(PeopleColor.BLUE),PeopleColor.BLUE);
       entranceSpace.addstudents(cloud.getStudentsAccumulator().numStudentsbycolor(PeopleColor.YELLOW),PeopleColor.YELLOW);
    }

    public StudentSet getDinnerTable() {
        return dinnerTable;
    }

    public StudentSet getEntranceSpace() {
        return entranceSpace;
    }


}
