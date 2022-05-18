package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.StudentSet;
import it.polimi.ingsw.server.model.enums.PeopleColor;

public class Monk extends CharacterCard{

    private final StudentSet set;
    public Monk(StudentSet bag){
        super("Prendi uno studente da questa carta e piazzalo su un isola a tua scelta",1,"MONK");
        set=new StudentSet();
        set.setStudentsRandomly(4,bag);
    }

    public void useEffect(Player player, PeopleColor color, int index_island,CenterTable table) {
        player.reduceCoin(getCost());
        improveCost();
        table.getIslands().get(index_island).getInhabitants().addstudents(1,color);
        set.removestudent(1,color);
        set.setStudentsRandomly(1,table.getBag());
    }

    @Override
    public String toString() {
        return "MONK - " + super.toString() +"\n"+
                "STUDENTS: " + set.toString()+ "\n";
    }

    public StudentSet getSet() {
        return set;
    }
}
