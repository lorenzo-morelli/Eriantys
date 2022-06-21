package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;

/**
 * This class contain the methods to use the character KINGHT described in the rules
 */
public class Knight extends CharacterCard implements SimpleEffect {
    public Knight(){
        super(2,"KNIGHT");
    }
    @Override
    public void useEffect(Player player, CenterTable table) {
        player.reduceCoin(getCost());
        improveCost();
        table.setKnightEffect(player);
    }

}
