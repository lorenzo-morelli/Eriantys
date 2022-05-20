package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.ArrayList;

public class Farmer extends CharacterCard{

    public Farmer(){
        super(2,"FARMER");
    }
    public void useEffect(Player player, CenterTable table, ArrayList<Player> players) {
        player.reduceCoin(getCost());
        improveCost();
        table.setFarmerEffect(player);
        for(PeopleColor color: PeopleColor.values()){
            if(player.getSchoolBoard().getDinnerTable().numStudentsbycolor(color)>0)
            {table.checkProfessor(color,players);}
        }
    }

}