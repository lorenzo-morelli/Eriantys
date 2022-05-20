package it.polimi.ingsw.server.model;
import it.polimi.ingsw.server.model.enums.GameMode;

public class Player implements Comparable<Player>{
    private final String nickname;
    private final Deck availableCards;
    private AssistantCard choosedCard;
    private final SchoolBoard schoolBoard;
    private int coins;
    private final String Ip;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_WHITE="\033[1;97m";
    public static final String ANSI_GRAY="\033[1;90m";

    public Player(String nickname,String Ip,Model model) {
        // crea e assegna valori di default
        this.nickname = nickname;
        this.Ip = Ip;
        this.availableCards = new Deck();
        this.choosedCard = null;
        this.schoolBoard = new SchoolBoard(model.getNumberOfPlayers(), model.getTable().getBag(),model.getTable().getAvaiableTowerColor());
        if(model.getGameMode().equals(GameMode.EXPERT) ) this.coins = 100; //todo: modificato per testing: reale valore 1
        else{coins=-1;}
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
            this.schoolBoard = new SchoolBoard(team, model.getTable().getBag(), model.getTable().getAvaiableTowerColor());
            if (model.getGameMode().equals(GameMode.EXPERT)) this.coins = 1;
            else{coins=-1;}
        }
        else{
            throw new IllegalArgumentException();
        }
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

    public String toString() {
        switch (getSchoolBoard().getTowerColor()) {
            case BLACK:
                return ANSI_BLACK + "    PLAYER : " + nickname + ANSI_RESET + "\n" +
                        (choosedCard == null ? "    MOVES : 0\n" : "    MOVES : " + choosedCard.getMoves() + "\n") +
                        "    SCHOOL\n"
                        + schoolBoard.toString() +
                        (coins >= 0 ? "    COINS : " + coins + "\n" : "\n");
            case WHITE:
                return ANSI_WHITE + "    PLAYER : " + nickname + ANSI_RESET + "\n" +
                        (choosedCard == null ? "    MOVES : 0\n" : "    MOVES : " + choosedCard.getMoves() + "\n") +
                        "    SCHOOL\n"
                        + schoolBoard.toString() +
                        (coins >= 0 ? "    COINS : " + coins + "\n" : "\n");
            case GREY:
                return ANSI_GRAY + "    PLAYER : " + nickname + ANSI_RESET + "\n" +
                        (choosedCard == null ? "    MOVES : 0\n" : "    MOVES : " + choosedCard.getMoves() + "\n") +
                        "    SCHOOL\n"
                        + schoolBoard.toString() +
                        (coins >= 0 ? "    COINS : " + coins + "\n" : "\n");
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
}
