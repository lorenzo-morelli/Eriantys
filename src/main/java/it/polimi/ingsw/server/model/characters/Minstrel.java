package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.ArrayList;

/**
 * This class contain the methods to use the character MINSTREL described in the rules
 */
public class Minstrel extends CharacterCard{
    public Minstrel(){
        super(1,"MINSTRELL");
    }

    /**
     *  colorsOfEntrance is the list of color to swap from entrance, colorsOfDinner is the list of color to swap from dinner
     */
    public void useEffect(Player player, ArrayList<PeopleColor> colorsOfDinner, ArrayList<PeopleColor> colorsOfEntrance, CenterTable table,ArrayList<Player> players) {
        player.reduceCoin(getCost());
        improveCost();
        for(int i=0; i<colorsOfDinner.size();i++) {
            player.getSchoolBoard().getEntranceSpace().removeStudent(1, colorsOfEntrance.get(i));
            player.getSchoolBoard().getEntranceSpace().addStudents(1, colorsOfDinner.get(i));
            player.getSchoolBoard().getDinnerTable().removeStudent(1,colorsOfDinner.get(i));
            player.getSchoolBoard().getDinnerTable().addStudents(1,colorsOfEntrance.get(i));
        }
        for(PeopleColor color: PeopleColor.values()){
            table.checkProfessor(color,players);
        }
    }


}
