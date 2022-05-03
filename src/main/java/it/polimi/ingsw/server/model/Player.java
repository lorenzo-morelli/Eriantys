package it.polimi.ingsw.server.model;
import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.server.model.enums.TowerColor;

import java.util.*;

public class Player {
    private String nickname;
    private Deck availableCards;
    private AssistantCard choosenCard;
    private SchoolBoard schoolBoard;
    private int coins;
    private String Ip;

    public Player(String nickname,String Ip){
        // crea e assegna valori di default
        this.nickname = nickname;
        this.Ip = Ip;
        this.availableCards = new Deck();
        this.choosenCard = new AssistantCard();
        this.schoolBoard = new SchoolBoard();
        this.coins = 0;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public List<AssistantCard> listAvailableCards(){
        return this.availableCards.getCardsList();
    }
    public void removeCard(AssistantCard choosen){
        this.availableCards.remove(choosen);
    }

    public void setDinnerTable(StudentSet students){
        this.schoolBoard.setDinnerTable(students);
    }
    public void setDinnerTable(int n, PeopleColor color) {
        this.schoolBoard.setDinnerTable(n,color);
    }

    public void setEntranceSpace(StudentSet students){
        this.schoolBoard.setEntranceSpace(students);
    }

    public void setEntranceSpace(int n, PeopleColor color) {
        this.schoolBoard.setEntranceSpace(n,color);
    }

    public StudentSet getEntranceSpace(){
        return this.schoolBoard.getEntranceSpace();
    }
    public int getEntranceSpace(PeopleColor color) {
        return this.schoolBoard.getEntranceSpace(color);
    }

    public StudentSet getDinnerTable(){
        return this.schoolBoard.getDinnerTable();
    }

    public int getDinnerTable(PeopleColor color) {
        return this.schoolBoard.getDinnerTable(color);
    }
    public TowerColor getTowerColor() {
        return this.schoolBoard.getTowerColor();
    }

    public void setTowerColor(TowerColor towerColor) {
        this.schoolBoard.setTowerColor(towerColor);
    }

    public int getNumOfTowers() {
        return this.schoolBoard.getNumOfTowers();
    }

    public void setNumOfTowers(int numOfTowers) {
        this.schoolBoard.setNumOfTowers(numOfTowers);
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public static void main(){

    }

    public int getChoosenCardValues(){
        return choosenCard.getValues();
    }
    public int getChoosenCardMoves(){
        return choosenCard.getMoves();
    }

    //debug only function
    public void printList(){
        for(int i=0; i< this.availableCards.getCardsList().size(); i++){
            System.out.println("Indice: " + i+ " Valore: " +  this.availableCards.getCardsList().get(i).getValues() + " Mosse: "+ this.availableCards.getCardsList().get(i).getMoves());
        }
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }
}
