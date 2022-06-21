package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.StudentSet;
import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.ArrayList;

/**
 * This class contain the methods to use the character PRINCESS described in the rules
 */
public class Princess extends CharacterCard{
    private final StudentSet set;
    public Princess(StudentSet bag){
        super(2,"PRINCESS");
        set=new StudentSet();
        set.setStudentsRandomly(4,bag);
    }

    public void useEffect(Player player, PeopleColor color, CenterTable table, ArrayList<Player> players) {
        player.reduceCoin(getCost());
        improveCost();
        player.getSchoolBoard().getDinnerTable().addstudents(1,color);
        set.removestudent(1,color);
        set.setStudentsRandomly(1,table.getBag());
        table.checkProfessor(color,players);
    }

    public StudentSet getSet() {
        return set;
    }
}
