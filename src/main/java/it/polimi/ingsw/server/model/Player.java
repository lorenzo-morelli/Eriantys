package it.polimi.ingsw.server.model;
import it.polimi.ingsw.server.model.enums.GameMode;

import java.util.Objects;

public class Player implements Comparable<Player>{
    private String nickname;
    private final Deck availableCards;
    private AssistantCard choosedCard;
    private final SchoolBoard schoolBoard;
    private int coins;
    private final String Ip;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_WHITE="\033[1;97m";
    public static final String ANSI_GRAY="\033[1;90m";
    private boolean isDisconnected;

    public Player(String nickname,String Ip,Model model) {
        // crea e assegna valori di default
        this.nickname = nickname;
        this.Ip = Ip;
        this.availableCards = new Deck();
        this.choosedCard = null;
        this.schoolBoard = new SchoolBoard(model.getNumberOfPlayers(), model.getTable().getBag(),model.getTable().getAvaiableTowerColor());
        if(model.getGameMode().equals(GameMode.EXPERT) ) this.coins = 100; //todo: modificato per testing: reale valore 1
        else{coins=-1;}
        isDisconnected=false;
    }
    //requies teamnumber== "1 or 2" e un controllo che fa riscegliere il team se team è gia pieno :team.isFull())
    public Player(String nickname,String Ip,int teamnumber,Model model) {
        if(teamnumber<3 && teamnumber>0) {
            this.nickname = nickname;
            this.Ip = Ip;
            this.availableCards = new Deck();
            this.choosedCard = null;
            Team team;
            if (model.getTeams().get(0).getTeamNumber() == teamnumber) {
                if (!(model.getTeams().get(0).isFull())) {
                    team = model.getTeams().get(0);
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                if (!(model.getTeams().get(1).isFull())) {
                    team = model.getTeams().get(1);
                } else {
                    throw new IllegalArgumentException();
                }
            }
            team.setPlayer(this);
            this.schoolBoard = new SchoolBoard(team, model.getTable().getBag(), model.getTable().getAvaiableTowerColor());
            if (model.getGameMode().equals(GameMode.EXPERT)) this.coins = 1;
            else{coins=-1;}
        }
        else{
            throw new IllegalArgumentException();
        }
        isDisconnected=false;
    }
    public boolean setChoosedCard(AssistantCard choosedCard) {
        this.choosedCard = choosedCard;
        return availableCards.remove(choosedCard);
    }
    public AssistantCard getChoosedCard() {
        return choosedCard;
    }

    @Override
    public int compareTo(Player player) {
        float compareValues=player.getChoosedCard().getValues();
        if((this.choosedCard.getValues()-compareValues)>0) return 1;
        else return -1;
    }

    public Deck getAvailableCards() {
        return availableCards;
    }

    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    public String getNickname() {
        return nickname;
    }

    public String getIp() {
        return Ip;
    }

    public String toString(String nickname) {
        switch (getSchoolBoard().getTowerColor()) {
            case BLACK:
                return ANSI_BLACK + "    PLAYER : " + this.nickname + ANSI_RESET + (isDisconnected ? "    IL GIOCATORE E' DISCONNESSO\n":"\n") +
                        (choosedCard == null ? "    CARD CHOOSED : NESSUNA\n" : "    CARD CHOOSED - VALUE : " + choosedCard.getValues() + " - MOVES : "+choosedCard.getMoves()+ "\n") +
                        "    SCHOOL\n"
                        + schoolBoard.toString() +
                        (coins >= 0 ? "    COINS : " + coins + "\n" : (Objects.equals(nickname, this.nickname) ?"": "\n"))+(Objects.equals(nickname, this.nickname) ?"    AVAIABLE CARDS: "+ availableCards.toString()+"\n": "");
            case WHITE:
                return ANSI_WHITE + "    PLAYER : " + this.nickname + ANSI_RESET + (isDisconnected ? "    IL GIOCATORE E' DISCONNESSO\n":"\n") +
                        (choosedCard == null ? "    CARD CHOOSED : NESSUNA\n" : "    CARD CHOOSED - VALUE : " + choosedCard.getValues() + " - MOVES : "+choosedCard.getMoves()+ "\n") +
                        "    SCHOOL\n"
                        + schoolBoard.toString() +
                        (coins >= 0 ? "    COINS : " + coins + "\n" : (Objects.equals(nickname, this.nickname) ?"": "\n"))+(Objects.equals(nickname, this.nickname) ?"    AVAIABLE CARDS: "+ availableCards.toString()+"\n": "");
            case GREY:
                return ANSI_GRAY + "    PLAYER : " + this.nickname + ANSI_RESET + (isDisconnected ? "    IL GIOCATORE E' DISCONNESSO\n":"\n")+
                        (choosedCard == null ? "    CARD CHOOSED : NESSUNA\n" : "    CARD CHOOSED - VALUE : " + choosedCard.getValues() + " - MOVES : "+choosedCard.getMoves()+ "\n") +
                        "    SCHOOL\n"
                        + schoolBoard.toString() +
                        (coins >= 0 ? "    COINS : " + coins + "\n" : (Objects.equals(nickname, this.nickname) ?"": "\n"))+(Objects.equals(nickname, this.nickname) ?"    AVAIABLE CARDS: "+ availableCards.toString()+"\n": "");
        }
        return null;
    }

    public int getCoins() {
        return coins;
    }
    public void improveCoin(){
        this.coins++;
    }
    public void reduceCoin(int cost){
        this.coins-=cost;
    }

    public void setDisconnected(boolean disconnected) {
        isDisconnected = disconnected;
    }

    public boolean isDisconnected() {
        return isDisconnected;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
