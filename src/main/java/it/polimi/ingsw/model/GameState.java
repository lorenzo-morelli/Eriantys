package it.polimi.ingsw.model;
import it.polimi.ingsw.model.enums.GameMode;

import java.util.*;

public class GameState {
    private GameMode gameMode;
    private int numberOfPlayers;
    private int turnNumber;
    private int currentOrderNumber;
    private Vector<Integer> order;
    private Optional<Vector<Team>> teams;

    public GameState(){
        order = new Vector<Integer>();
        teams = new Optional<Vector<Team>>;
    }
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setCurrentOrderNumber(int currentOrderNumber) {
        this.currentOrderNumber = currentOrderNumber;
    }

    public int getCurrentOrderNumber() {
        return currentOrderNumber;
    }

    public void setOrder(Vector<Integer> order) {
        this.order = order;
    }

    public Vector<Integer> getOrder() {
        return order;
    }

    public void setTeams(Optional<Vector<Team>> teams) {
        this.teams = teams;
    }

    public Optional<Vector<Team>> getTeams() {
        return teams;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getTurnNumber() {
        return turnNumber;
    }
    public boolean isLastPlayerOfTheTurn(){
        //todo
    }
    public boolean isFirsPlayerOfTheTurn(){
        //todo
    }
    public void update(){
        // todo, or just an idea??
        /* aggiorna currentOrderNumber nel caso in cui
           isLastPlayerOfTheTurn è falso
         */
    }
}
