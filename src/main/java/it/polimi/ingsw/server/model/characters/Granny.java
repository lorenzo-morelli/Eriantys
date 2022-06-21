package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;

/**
 * This class contain the info and the methods to use the card GRANNY described in the rules
 */
public class Granny extends CharacterCard{

    private int numDivieti;
    public Granny(){
        super(2,"GRANNY");
        numDivieti=4;
    }

    public void useEffect(Player player,int index_island,CenterTable table) {
        player.reduceCoin(getCost());
        improveCost();
        table.getIslands().get(index_island).setBlocked(true);
    }

    public int getNumDivieti() {
        return numDivieti;
    }

    public void improveDivieti() {
        this.numDivieti ++;
    }

}
