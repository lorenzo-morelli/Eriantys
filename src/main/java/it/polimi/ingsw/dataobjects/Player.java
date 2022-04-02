package it.polimi.ingsw.dataobjects;
import java.util.*;

public class Player {
    private String nickname;
    private int userId;
    private Deck availableCards;
    private AssistantCard choosenCard;
    private SchoolBoard schoolBoard;
    private int coins;

    public Player(){
        // crea e assegna valori di default
        this.nickname = "Steve";
        this.userId = 0;
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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
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
    public void setDinnerTable(int n, StudentColor color) {
        this.schoolBoard.setDinnerTable(n,color);
    }

    public void setEntranceSpace(StudentSet students){
        this.schoolBoard.setEntranceSpace(students);
    }

    public void setEntranceSpace(int n, StudentColor color) {
        this.schoolBoard.setEntranceSpace(n,color);
    }

    public StudentSet getEntranceSpace(){
        return this.schoolBoard.getEntranceSpace();
    }
    public int getEntranceSpace(StudentColor color) {
        return this.schoolBoard.getEntranceSpace(color);
    }

    public StudentSet getDinnerTable(){
        return this.schoolBoard.getDinnerTable();
    }

    public int getDinnerTable(StudentColor color) {
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
}
