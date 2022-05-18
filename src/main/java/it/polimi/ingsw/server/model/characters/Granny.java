package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;

public class Granny extends CharacterCard{

    private int numDivieti;
    public Granny(){
        super("Piazza un divieto su un isola a tua scelta. La prima volta che madre natura termina il suo movimento li, rimettete la tessera divieto sulla carta senza calcolare influenza ne piazzare torri su di essa",2,"GRANNY");
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

    @Override
    public String toString() {
        return "GRANNY - " + super.toString() +"\n"+
                "DIVIETI: " + numDivieti+ "\n";
    }
}
