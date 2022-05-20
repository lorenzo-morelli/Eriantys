package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.StudentSet;
import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.ArrayList;

public class Jester extends CharacterCard{
    private final StudentSet set;
    public Jester(StudentSet bag){
        super(1,"JESTER");
        set=new StudentSet();
        set.setStudentsRandomly(6,bag);
    }
   // hp: colorsOfEntrance contiene lista dei colori da scambiare da entrance, colorsOfJester la lista dei colori da scambiare dalla carta
    //controllare che abbiano la stessa lunghezza e che gli studenti scelti (sia come colore che come numero) siano realmente presenti nei rispettivi set
    // inoltre la loro lunghezza deve essere minimo 1 e massimo 3
    public void useEffect(Player player, ArrayList<PeopleColor> colorsOfJester,  ArrayList<PeopleColor> colorsOfEntrance) {
        player.reduceCoin(getCost());
        improveCost();
        for(int i=0; i<colorsOfJester.size();i++) {
            player.getSchoolBoard().getEntranceSpace().removestudent(1, colorsOfEntrance.get(i));
            player.getSchoolBoard().getEntranceSpace().addstudents(1, colorsOfJester.get(i));
            set.removestudent(1,colorsOfJester.get(i));
            set.addstudents(1,colorsOfEntrance.get(i));
        }
    }

    public StudentSet getSet() {
        return set;
    }
}
