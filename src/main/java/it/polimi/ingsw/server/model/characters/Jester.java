package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.StudentSet;
import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.ArrayList;

/**
 * This class contain the methods to use the character JESTER described in the rules
 */
public class Jester extends CharacterCard{
    private final StudentSet set;
    public Jester(StudentSet bag){
        super(1,"JESTER");
        set=new StudentSet();
        set.setStudentsRandomly(6,bag);
    }
    /**
     *  colorsOfEntrance is the list of color to swap from entrance, colorsOfJester is the list of color to swap from this card
     */
    public void useEffect(Player player, ArrayList<PeopleColor> colorsOfJester,  ArrayList<PeopleColor> colorsOfEntrance) {
        player.reduceCoin(getCost());
        improveCost();
        for(int i=0; i<colorsOfJester.size();i++) {
            player.getSchoolBoard().getEntranceSpace().removeStudent(1, colorsOfEntrance.get(i));
            player.getSchoolBoard().getEntranceSpace().addStudents(1, colorsOfJester.get(i));
            set.removeStudent(1,colorsOfJester.get(i));
            set.addStudents(1,colorsOfEntrance.get(i));
        }
    }

    public StudentSet getSet() {
        return set;
    }
}
