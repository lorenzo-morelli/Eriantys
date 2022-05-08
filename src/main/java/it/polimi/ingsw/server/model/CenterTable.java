package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.*;
import it.polimi.ingsw.server.model.enums.Character;

import java.util.ArrayList;
import java.util.Collections;
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

    public CenterTable(int numplayer, GameMode gamemode) {

        islands=new ArrayList<>();
        StudentSet islandbag=new StudentSet(2,2,2,2,2);
        ArrayList<PeopleColor> avaiableColorIslandBag = new ArrayList<>();
        Collections.addAll(avaiableColorIslandBag, PeopleColor.values());

        for(int i=0;i<12;i++){
            if(i==0 || i==6) {
                islands.add(new Island(0,islandbag,avaiableColorIslandBag));
            }
            else {
                islands.add(new Island(1,islandbag,avaiableColorIslandBag));
            }
        }

        motherNaturePosition=0;

        bag=new StudentSet(24,24,24,24,24);
        avaiablePeopleColorinBag = new ArrayList<>();
        Collections.addAll(avaiablePeopleColorinBag, PeopleColor.values());

        avaiableTowerColor= new ArrayList<>();
        if (numplayer == 3) {
            Collections.addAll(avaiableTowerColor, TowerColor.values());
        } else {
            avaiableTowerColor.add(TowerColor.WHITE);
            avaiableTowerColor.add(TowerColor.BLACK);
        }

        professors=new ArrayList<>();
        for(PeopleColor Color: PeopleColor.values()) {
            professors.add(new Professor(Color));
        }

        clouds =new ArrayList<>();
        for(int i=0;i<numplayer;i++){
            clouds.add(new Cloud(numplayer));
            clouds.get(i).charge(bag,avaiablePeopleColorinBag);
        }

        if(gamemode.equals(GameMode.EXPERT)){  //da controllare
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
    public ArrayList<TowerColor> getAvaiableTowerColor() {
        return avaiableTowerColor;
    }
    public void changeProfessor(Player player,PeopleColor color){
        for (Professor professor : professors) {
            if (professor.getColor().equals(color)) professor.setHeldBy(player);
        }
    }
    public void chargeClouds() {
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
        player.getSchoolBoard().getEntranceSpace().removestudent(1,color);
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
        islands.get(index_island).controllIsland(influence_player);
    }

    public void ConquestIsland(int index_island, ArrayList<Team> teams, Team influence_team){
        for (Team team : teams) {
            if (team.getPlayer1().getSchoolBoard().getTowerColor().equals(islands.get(index_island).getTowerColor())) {
                team.getPlayer1().getSchoolBoard().removeTower();
                team.getPlayer2().getSchoolBoard().removeTower();
            }
        }
        islands.get(index_island).controllIsland(influence_team);
    }

    public void MergeIsland(int index_1, int index_2){
        islands.get(index_1).placeTower();
        for(PeopleColor Color: PeopleColor.values()){
            islands.get(index_1).getInhabitants().addstudents(islands.get(index_2).getInhabitants().numStudentsbycolor(Color), Color);
        }
        islands.get(index_1).setBlocked(islands.get(index_1).isBlocked());
        islands.remove(index_2);
        if(index_2 < index_1) motherNaturePosition--;
        if(index_2==0 && index_1== islands.size()) motherNaturePosition=islands.size()-1;
    }

    public ArrayList<Cloud> getClouds() {
        return clouds;
    }

    public ArrayList<Character> getCharacterCards() {
        return characterCards;
    }
}