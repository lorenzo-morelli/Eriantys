package it.polimi.ingsw.server.model;
import it.polimi.ingsw.server.model.enums.GameMode;
import it.polimi.ingsw.server.model.enums.TowerColor;

public class Player implements Comparable<Player>{
    private final String nickname;
    private final Deck availableCards;
    private AssistantCard choosedCard;
    private final SchoolBoard schoolBoard;

    private final int numplayerinteam;
    private int coins;
    private final String Ip;

    public Player(String nickname,String Ip,Model model) {
        // crea e assegna valori di default
        this.nickname = nickname;
        this.Ip = Ip;
        this.availableCards = new Deck();
        this.choosedCard = null;
        this.numplayerinteam=0;
        this.schoolBoard = new SchoolBoard(model.getNumberOfPlayers(), model.getTable().getBag(), model.getTable().getAvaiablePeopleColorinBag(), model.getTable().getAvaiableTowerColor());
        if(model.getGameMode().equals(GameMode.EXPERT) ) this.coins = 1;
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
            numplayerinteam= team.setPlayer(this);
            this.schoolBoard = new SchoolBoard(team, model.getTable().getBag(), model.getTable().getAvaiablePeopleColorinBag(), model.getTable().getAvaiableTowerColor());
            if (model.getGameMode().equals(GameMode.EXPERT)) this.coins = 1;
        }
        else{
            throw new IllegalArgumentException();
        }
    }
    public void setChoosedCard(AssistantCard choosedCard) {
        this.choosedCard = choosedCard;
    }

    public AssistantCard getChoosedCard() {
        return choosedCard;
    }

    @Override
    public int compareTo(Player player) {
        float compareValues=player.choosedCard.getValues();
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

    public int getNumplayerinteam() {
        return numplayerinteam;
    }
}
