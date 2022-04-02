package it.polimi.ingsw.model;
import it.polimi.ingsw.dataobjects.*;

abstract class Model {
    private Player player0;
    private Player player1;
    private table CenterTable;
    private GameState gameState;
    public Model(){
        player0 = new Player();
        player1 = new Player();
        table = new CenterTable;
        gameState = new GameState();

    }

    public Player getPlayer0() {
        return player0;
    }

    public Player getPlayer1() {
        return player1;
    }

    public GameState getGameState() {
        return gameState;
    }

    public table getCenterTable() {
        return CenterTable;
    }

    public void setPlayer0(Player player0) {
        this.player0 = player0;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setCenterTable(table centerTable) {
        CenterTable = centerTable;
    }
}
