package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.ArrayList;

/**
 * This class contain the methods to use the character THIEF described in the rules
 * @author Ignazio Neto Dell'Acqua
 */
public class Thief extends CharacterCard{
    public Thief(){
        super(3,"THIEF");
    }

    public void useEffect(Player player, ArrayList<Player> players, PeopleColor color,CenterTable table) {
        player.reduceCoin(getCost());
        improveCost();
        for (Player value : players) {
            value.getSchoolBoard().getDinnerTable().removeStudentInBag(3, color, table.getBag());
        }
        table.checkProfessor(color,players);
    }
}