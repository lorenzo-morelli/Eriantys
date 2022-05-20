package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enums.PeopleColor;

public class MushroomHunter extends CharacterCard{
    public MushroomHunter(){
        super(3,"MUSHROOMHUNTER");
    }
    public void useEffect(Player player, PeopleColor color,CenterTable table) {
        player.reduceCoin(getCost());
        improveCost();
        table.setMushroomColor(color);
    }

}
