package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.StudentSet;
import it.polimi.ingsw.server.model.enums.PeopleColor;

/**
 * This class contain the methods to use the character MONK described in the rules
 * @author Ignazio Neto Dell'Acqua
 */
public class Monk extends CharacterCard{

    private final StudentSet set;
    public Monk(StudentSet bag){
        super(1,"MONK");
        set=new StudentSet();
        set.setStudentsRandomly(4,bag);
    }

    public void useEffect(Player player, PeopleColor color, int index_island,CenterTable table) {
        player.reduceCoin(getCost());
        improveCost();
        table.getIslands().get(index_island).getInhabitants().addStudents(1,color);
        set.removeStudent(1,color);
        set.setStudentsRandomly(1,table.getBag());
    }

    public StudentSet getSet() {
        return set;
    }
}
