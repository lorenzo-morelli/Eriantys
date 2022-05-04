package it.polimi.ingsw.server.model;
import it.polimi.ingsw.server.model.enums.GameMode;
import it.polimi.ingsw.server.model.enums.PeopleColor;
import it.polimi.ingsw.server.model.enums.TowerColor;

import java.util.*;

public class Player implements Comparable{
    private String nickname;
    private final Deck availableCards;
    private AssistantCard choosedCard;
    private final SchoolBoard schoolBoard;
    private int coins;
    private String Ip;
    private int numPlayerinTeam;

    public Player(String nickname,String Ip,Model model){
        // crea e assegna valori di default
        this.nickname = nickname;
        this.Ip = Ip;
        this.availableCards = new Deck();
        this.choosedCard = null;
        this.schoolBoard = new SchoolBoard(model.getNumberOfPlayers(), model.getTable().getBag(), model.getTable().getAvaiablePeopleColorinBag(), model.getTable().getDisponibleTowerColor());
        if(model.getGameMode().equals(GameMode.EXPERT) ) this.coins = 0;
    }
    //requies teamnumber== "1 or 2" e un controllo che fa riscegliere il team se team è gia pieno :team.isFull())
    public Player(String nickname,String Ip,int teamnumber,Model model){
        // crea e assegna valori di default
        this.nickname = nickname;
        this.Ip = Ip;
        this.availableCards = new Deck();
        this.choosedCard = null;
        Team team;
        if (model.getTeams().get(0).getTeamNumber() == teamnumber) team = model.getTeams().get(0);
        else team = model.getTeams().get(1);
        numPlayerinTeam= team.setPlayer(this);
        this.schoolBoard = new SchoolBoard(team, model.getNumberOfPlayers(), model.getTable().getBag(), model.getTable().getAvaiablePeopleColorinBag(), model.getTable().getDisponibleTowerColor());
        if(model.getGameMode().equals(GameMode.EXPERT) ) this.coins = 0;
    }
    public TowerColor getTowerColor() {
        return this.schoolBoard.getTowerColor();
    }

    public void setChoosedCard(AssistantCard choosedCard) {
        this.choosedCard = choosedCard;
    }
    @Override
    public int compareTo(Object player) {
        int compareValues=((Player)player).choosedCard.getValues();
        return this.choosedCard.getValues()-compareValues;
    }

    public Deck getAvailableCards() {
        return availableCards;
    }
}
