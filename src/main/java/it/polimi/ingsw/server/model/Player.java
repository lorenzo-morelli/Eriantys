package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.GameMode;

import java.util.Objects;

/**
 * Class representing the "Player" data object, which contains
 * all the information necessary for the server (and for the business
 * logic of the game)
 *
 * @author Ignazio Neto Dell'Acqua
 */
public class Player implements Comparable<Player> {
    private String nickname;
    private final Deck availableCards;
    private AssistantCard choosedCard;
    private final SchoolBoard schoolBoard;
    private int coins;
    private final String Ip;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_WHITE = "\033[1;97m";
    public static final String ANSI_GRAY = "\033[1;90m";
    private boolean isDisconnected;

    /**
     * The main player constructor
     * @param nickname the nickname of the player
     * @param Ip the ip address of the player
     * @param model reference to the model
     * @param debug boolean value to unlock coins (debug purpose)
     */

    public Player(String nickname, String Ip, Model model, boolean debug) {
        this.nickname = nickname;
        this.Ip = Ip;
        this.availableCards = new Deck();
        this.choosedCard = null;
        this.schoolBoard = new SchoolBoard(model.getNumberOfPlayers(), model.getTable().getBag(), model.getTable().getAvailableTowerColor());
        if (debug) {
            if (model.getGameMode().equals(GameMode.EXPERT)) this.coins = 100;
            else {
                coins = -1;
            }
        } else {
            if (model.getGameMode().equals(GameMode.EXPERT)) this.coins = 1;
            else {
                coins = -1;
            }
        }
        isDisconnected = false;
    }

    /**
     * Requires teamNumber == (1 || 2) and a check that causes the team to be
     * re-chosen if team is already full :team.isFull())
     * @param nickname the username of the player
     * @param Ip the ip address of the player
     * @param teamNumber the team number of the player
     * @param model reference to the model
     * @param debug debug flag to unlock coins (debug purpose)
     */
    public Player(String nickname, String Ip, int teamNumber, Model model, boolean debug) {
        if (teamNumber < 3 && teamNumber > 0) {
            this.nickname = nickname;
            this.Ip = Ip;
            this.availableCards = new Deck();
            this.choosedCard = null;
            Team team;
            if (model.getTeams().get(0).getTeamNumber() == teamNumber) {
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
            this.schoolBoard = new SchoolBoard(team, model.getTable().getBag(), model.getTable().getAvailableTowerColor());
            if (debug) {
                if (model.getGameMode().equals(GameMode.EXPERT)) this.coins = 100;
                else {
                    coins = -1;
                }
            } else {
                if (model.getGameMode().equals(GameMode.EXPERT)) this.coins = 1;
                else {
                    coins = -1;
                }
            }


        } else {
            throw new IllegalArgumentException();
        }
        isDisconnected = false;
    }

    /**
     * Method to set the assistant card
     * @param choosedCard the card to choose
     * @return boolean value that represent the status of the operation
     */
    public boolean setChoosedCard(AssistantCard choosedCard) {
        this.choosedCard = choosedCard;
        return availableCards.remove(choosedCard);
    }

    public AssistantCard getChoosedCard() {
        return choosedCard;
    }

    /**
     * Comparator of players
     * @param player the object to be compared.
     * @return 1 if  the chosen card has a value greater than the one of the other player, -1 else
     */
    @Override
    public int compareTo(Player player) {
        float compareValues = player.getChoosedCard().getValues();
        if ((this.choosedCard.getValues() - compareValues) > 0) return 1;
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
                return getPlayerInfo(nickname, ANSI_BLACK);
            case WHITE:
                return getPlayerInfo(nickname, ANSI_WHITE);
            case GREY:
                return getPlayerInfo(nickname, ANSI_GRAY);
        }
        return null;
    }

    private String getPlayerInfo(String nickname, String ansiBlack) {
        return ansiBlack + "    PLAYER : " + this.nickname + ANSI_RESET + (isDisconnected ? "    IL GIOCATORE E' DISCONNESSO\n" : "\n") +
                (choosedCard == null ? "    CARD CHOOSED : NESSUNA\n" : "    CARD CHOOSED - VALUE : " + choosedCard.getValues() + " - MOVES : " + choosedCard.getMoves() + "\n") +
                "    SCHOOL\n"
                + schoolBoard.toString() +
                (coins >= 0 ? "    COINS : " + coins + "\n" : (Objects.equals(nickname, this.nickname) ? "" : "\n")) + (Objects.equals(nickname, this.nickname) ? "    AVAILABLE CARDS: " + availableCards.toString() + "\n" : "");
    }

    public int getCoins() {
        return coins;
    }

    public void increaseCoin() {
        this.coins++;
    }

    public void reduceCoin(int cost) {
        this.coins -= cost;
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
