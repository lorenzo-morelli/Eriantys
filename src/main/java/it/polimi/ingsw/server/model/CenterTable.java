package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.characters.*;
import it.polimi.ingsw.server.model.characters.Character;
import it.polimi.ingsw.server.model.enums.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CenterTable {
    private final ArrayList<Cloud> clouds;
    private final ArrayList<Island> islands;
    private int motherNaturePosition;
    private final StudentSet bag;
    private final ArrayList<Professor> professors;
    private final ArrayList<TowerColor> avaiableTowerColor;
    private final ArrayList<CharacterCard> characterCards;
    private boolean centaurEffect;
    private Player farmerEffect;
    private PeopleColor mushroomColor;
    private Player knightEffect;

    public static final String ANSI_CYAN = "\033[0;36m";
    public static final String ANSI_RESET = "\u001B[0m";

    public CenterTable(int numplayer, GameMode gamemode) {

        islands=new ArrayList<>();
        StudentSet islandbag=new StudentSet(2,2,2,2,2);

        for(int i=0;i<12;i++){
            if(i==0 || i==6) {
                islands.add(new Island(0,islandbag));
            }
            else {
                islands.add(new Island(1,islandbag));
            }
        }

        motherNaturePosition=0;

        bag=new StudentSet(24,24,24,24,24);

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
            clouds.get(i).charge(bag);
        }

        if(gamemode.equals(GameMode.EXPERT)){
            characterCards=new ArrayList<>();
            characterCards.add(new Minstrell());/*  //da controllare

            ArrayList<Integer> picks=new ArrayList<>();
            var ref = new Object() {
                int pick;
            };

            for(int i=0;i<3;i++) {
                do{
                    ref.pick = new Random().nextInt(Character.values().length);
                }while(picks.stream().anyMatch(j->j.equals(ref.pick)));
                picks.add(ref.pick);
                switch (Character.values()[ref.pick]){
                    case MONK: cards.add(new Monk(this));
                    break;
                    case THIEF: cards.add(new Thief(this));
                        break;
                    case FARMER: cards.add(new Farmer(this));
                        break;
                    case GRANNY: cards.add(new Granny(this));
                        break;
                    case HERALD: cards.add(new Herald(this));
                        break;
                    case JESTER: cards.add(new Jester(this));
                        break;
                    case KNIGHT: cards.add(new Knight(this));
                        break;
                    case CENTAUR: cards.add(new Centaur(this));
                        break;
                    case POSTMAN: cards.add(new Postman(this));
                        break;
                    case PRINCESS: cards.add(new Princess(this));
                        break;
                    case MINSTRELL: cards.add(new Minstrell(this));
                        break;
                    case MUSHROOM_HUNTER: cards.add(new MushroomHunter(this));
                        break;
                }
            }*/
        }
        else{
            characterCards=null;
        }
        centaurEffect=false;
        farmerEffect=null;
        mushroomColor=null;
        knightEffect=null;
    }

    public ArrayList<Professor> getProfessors() {
        return professors;
    }
    public StudentSet getBag() {
        return bag;
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
            cloud.charge(bag);
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
        if(farmerEffect!=null && farmerEffect.getSchoolBoard().getDinnerTable().numStudentsbycolor(color)==max) moreInfluenced=farmerEffect;
        if(moreInfluenced!=null) changeProfessor(moreInfluenced,color);
        setFarmerEffect(null);
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
        islands.get(index_1).setNumberOfTowers(islands.get(index_1).getNumberOfTowers()+islands.get(index_2).getNumberOfTowers());
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

    @Override
    public String toString() {
        return  "-----------------------------------------TABLE----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "\n----------------ISLANDS---------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" +  printislands() +"------------------BAG-----------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n"+
                "    SIZE : " + bag.size() + "    " + bag +
                "\n\n----------------CLOUDS----------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" + printclouds() +
                "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"+
                (characterCards==null ? "" :
                        characterCards.toString() +"\n")+
                "---------------PROFESSORS-------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n"+
                printprofessors() + "\n\n--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n" ;
    }

    public String printclouds(){
        StringBuilder result= new StringBuilder();
        int i=1;
        for (Cloud cloud : clouds) {
            result.append(ANSI_CYAN).append("    CLOUD : ").append(i).append(ANSI_RESET).append("\n").append(cloud.toString());
            i++;
        }
        return result.toString();
    }

    public String printislands(){
        StringBuilder result= new StringBuilder();
        int i=1;
        for (Island island : islands) {
            result.append(ANSI_CYAN).append("    ISLAND : ").append(i);
            if(i==(motherNaturePosition+1)){
                result.append(" - MOTHER NATURE IS HERE ").append(ANSI_RESET).append("\n").append(island);
            }
            else{
                result.append(ANSI_RESET).append("\n").append(island);
            }
            i++;
        }
        return result.toString();
    }

    public String printprofessors(){
        StringBuilder result= new StringBuilder();
        int i=0;
        for (Professor prof : professors) {
            result.append(i != 0 ? " |  " + prof.toString() : prof.toString());
            i++;
        }
        return result.toString();
    }

    public ArrayList<CharacterCard> getCards() {
        return characterCards;
    }

    public boolean isCentaurEffect() {
        return centaurEffect;
    }

    public void setCentaurEffect(boolean centaurEffect) {
        this.centaurEffect = centaurEffect;
    }

    public PeopleColor getMushroomColor() {
        return mushroomColor;
    }

    public void setMushroomColor(PeopleColor mushroomColor) {
        this.mushroomColor = mushroomColor;
    }

    public Player getKnightEffect() {
        return knightEffect;
    }

    public void setKnightEffect(Player knightEffect) {
        this.knightEffect = knightEffect;
    }
    public Player getFarmerEffect() {
        return farmerEffect;
    }

    public void setFarmerEffect(Player farmerEffect) {
        this.farmerEffect = farmerEffect;
    }
}