package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.ArrayList;

public class Farmer extends CharacterCard{

    public Farmer(){
        super("Durante questo turno, prendi il controllo dei Professori anche in caso di parità di numero di studenti",2,"FARMER");
    }
    public void useEffect(Player player, CenterTable table, ArrayList<Player> players) {
        player.reduceCoin(getCost());
        improveCost();
        table.setFarmerEffect(player);
        for(PeopleColor color: PeopleColor.values()){
            table.checkProfessor(color,players);
        }
    }

    @Override
    public String toString() {
        return "FARMER "+ super.toString();
    }
}