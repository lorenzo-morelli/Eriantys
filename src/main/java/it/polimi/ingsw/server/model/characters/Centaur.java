package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;

/**
 * This class contain the methods to use the character CENTAUR described in the rules
 */
public class Centaur extends CharacterCard implements SimpleEffect {

    public Centaur(){
        super(3,"CENTAUR");
    }

    @Override
    public void useEffect(Player player, CenterTable table) {
        player.reduceCoin(getCost());
        improveCost();
        table.setCentaurEffect(true);
    }


}
