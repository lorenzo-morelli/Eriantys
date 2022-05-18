package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;

public class Farmer extends CharacterCard implements SimpleEffect {

    public Farmer(){
        super("Durante questo turno, prendi il controllo dei Professori anche in caso di parità di numero di studenti",2,"FARMER");
    }
    @Override
    public void useEffect(Player player, CenterTable table) {
        player.reduceCoin(getCost());
        improveCost();
        table.setFarmerEffect(player);
    }

    @Override
    public String toString() {
        return "FARMER "+ super.toString();
    }
}