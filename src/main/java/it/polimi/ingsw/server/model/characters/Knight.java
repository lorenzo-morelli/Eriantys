package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;

public class Knight extends CharacterCard implements SimpleEffect {
    public Knight(){
        super("In questo turno, durante il calcolo dell'influenza hai 2 punti di influenza addizionali",2,"KNIGHT");
    }
    @Override
    public void useEffect(Player player, CenterTable table) {
        player.reduceCoin(getCost());
        improveCost();
        table.setKnightEffect(player);
    }
    @Override
    public String toString() {
        return "KNIGHT "+ super.toString();
    }
}
