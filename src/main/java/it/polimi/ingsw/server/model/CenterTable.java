package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.*;
import it.polimi.ingsw.server.model.enums.Character;

import java.util.ArrayList;
import java.util.Random;

public class CenterTable {
    private final ArrayList<Cloud> clouds;
    private final ArrayList<Island> islands;
    private int motherNaturePosition;
    private final StudentSet bag;
    private final ArrayList<Professor> professors;
    private final ArrayList<PeopleColor> avaiablePeopleColorinBag;
    private final ArrayList<TowerColor> avaiableTowerColor;
    private final ArrayList<Character> characterCards;

    public CenterTable(int numplayer, GameMode gamemode){

        islands=new ArrayList<>();
        StudentSet islandbag=new StudentSet(2,2,2,2,2);
        ArrayList<PeopleColor> avaiableColorislandBag = new ArrayList<>();
        avaiableColorislandBag.add(PeopleColor.RED);
        avaiableColorislandBag.add(PeopleColor.BLUE);
        avaiableColorislandBag.add(PeopleColor.YELLOW);
        avaiableColorislandBag.add(PeopleColor.GREEN);
        avaiableColorislandBag.add(PeopleColor.PINK);

        for(int i=0;i<12;i++){
            if(i==0 || i==6) {
                islands.add(new Island(0,islandbag,avaiableColorislandBag));
            }
            else {
                islands.add(new Island(1,islandbag,avaiableColorislandBag));
            }
        }

        motherNaturePosition=0;

        bag=new StudentSet(24,24,24,24,24);
        avaiablePeopleColorinBag = new ArrayList<>();
        avaiablePeopleColorinBag.add(PeopleColor.RED);
        avaiablePeopleColorinBag.add(PeopleColor.BLUE);
        avaiablePeopleColorinBag.add(PeopleColor.YELLOW);
        avaiablePeopleColorinBag.add(PeopleColor.GREEN);
        avaiablePeopleColorinBag.add(PeopleColor.PINK);

        avaiableTowerColor= new ArrayList<>();
        if (numplayer == 3) {
            avaiableTowerColor.add(TowerColor.WHITE);
            avaiableTowerColor.add(TowerColor.BLACK);
            avaiableTowerColor.add(TowerColor.GREY);
        } else {
            avaiableTowerColor.add(TowerColor.WHITE);
            avaiableTowerColor.add(TowerColor.BLACK);
        }

        professors=new ArrayList<>();
        professors.add(new Professor(PeopleColor.RED));
        professors.add(new Professor(PeopleColor.YELLOW));
        professors.add(new Professor(PeopleColor.BLUE));
        professors.add(new Professor(PeopleColor.GREEN));
        professors.add(new Professor(PeopleColor.PINK));

        clouds =new ArrayList<>();
        for(int i=0;i<numplayer;i++){
            clouds.add(new Cloud(numplayer));
            clouds.get(i).charge(bag,avaiableColorislandBag);
        }

        if(gamemode.equals(GameMode.EXPERT)){
            characterCards=new ArrayList<>();
            ArrayList<Integer> picks=new ArrayList<>();
            var ref = new Object() {
                int pick;
            };

            for(int i=0;i<3;i++) {
                do{
                    ref.pick = new Random().nextInt(Character.values().length);
                }while(picks.stream().anyMatch(j->j.equals(ref.pick)));
                picks.add(ref.pick);
                characterCards.add(Character.values()[ref.pick]);
            }
        }
        else{
            characterCards=null;
        }
    }

    public ArrayList<Professor> getProfessors() {
        return professors;
    }
    public StudentSet getBag() {
        return bag;
    }
    public ArrayList<PeopleColor> getAvaiablePeopleColorinBag() {
        return avaiablePeopleColorinBag;
    }
    public ArrayList<TowerColor> getDisponibleTowerColor() {
        return avaiableTowerColor;
    }
    public void changeProfessor(Player player,PeopleColor color){
        for (Professor professor : professors) {
            if (professor.getColor().equals(color)) professor.setHeldBy(player);
        }
    }
    public void chargeClouds(){
        for (Cloud cloud : clouds) {
            cloud.charge(bag, avaiablePeopleColorinBag);
        }
    }

    public ArrayList<Island> getIslands() {
        return islands;
    }

    public void movemother(int moves){
        motherNaturePosition= (motherNaturePosition + moves)% islands.size();
    }

    public int getMotherNaturePosition() {
        return motherNaturePosition;
    }
    public void load_island(Player player,PeopleColor color,int index_island){
        player.getSchoolBoard().getEntranceSpace().removestudent(color);
        islands.get(index_island).getInhabitants().addstudents(1,color);
    }
    public void checkProfessor(PeopleColor color, ArrayList<Player> players){
        int max=0;
        Player moreInfluenced=null;
        for (Player player : players) {
            if (player.getSchoolBoard().getDinnerTable().numStudentsbycolor(color) > max) {
                max = player.getSchoolBoard().getDinnerTable().numStudentsbycolor(color);
                moreInfluenced = player;
            } else if (player.getSchoolBoard().getDinnerTable().numStudentsbycolor(color) == max) {
                moreInfluenced = null;
            }
        }
        if(moreInfluenced!=null) changeProfessor(moreInfluenced,color);
    }
    public void ConquestIsland(int index_island, ArrayList<Player> players, Player influence_player){
        for (Player player : players) {
            if (player.getSchoolBoard().getTowerColor().equals(islands.get(index_island).getTowerColor())) {
                player.getSchoolBoard().removeTower();
            }
        }
        islands.get(index_island).controlIsland(influence_player);
    }
    public void MergeIsland(int index_1, int index_2){
        islands.get(index_1).placeTower();
        islands.get(index_1).getInhabitants().addstudents( islands.get(index_2).getInhabitants().numStudentsbycolor(PeopleColor.RED),PeopleColor.RED);
        islands.get(index_1).getInhabitants().addstudents( islands.get(index_2).getInhabitants().numStudentsbycolor(PeopleColor.BLUE),PeopleColor.BLUE);
        islands.get(index_1).getInhabitants().addstudents( islands.get(index_2).getInhabitants().numStudentsbycolor(PeopleColor.GREEN),PeopleColor.GREEN);
        islands.get(index_1).getInhabitants().addstudents( islands.get(index_2).getInhabitants().numStudentsbycolor(PeopleColor.YELLOW),PeopleColor.YELLOW);
        islands.get(index_1).getInhabitants().addstudents( islands.get(index_2).getInhabitants().numStudentsbycolor(PeopleColor.PINK),PeopleColor.PINK);
        islands.get(index_1).setBlocked(islands.get(index_1).isBlocked());
        islands.remove(index_2);
        if(index_2 < index_1) motherNaturePosition--;
        if(index_2==0 && index_1== islands.size()) motherNaturePosition=islands.size()-1;
    }
}