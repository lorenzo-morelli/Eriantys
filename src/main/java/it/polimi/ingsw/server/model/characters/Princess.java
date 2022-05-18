package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.StudentSet;
import it.polimi.ingsw.server.model.enums.PeopleColor;

public class Princess extends CharacterCard{
    private StudentSet set;
    public Princess(StudentSet bag){
        super("Prendi uno studente da questa carta e piazzalo nella tua sala",2,"PRINCESS");
        set=new StudentSet();
        set.setStudentsRandomly(4,bag);
    }

    public void useEffect(Player player, PeopleColor color,CenterTable table) {
        player.reduceCoin(getCost());
        improveCost();
        player.getSchoolBoard().getDinnerTable().addstudents(1,color);
        set.removestudent(1,color);
        set.setStudentsRandomly(1,table.getBag());
    }

    @Override
    public String toString() {
        return "PRINCESS - " + super.toString() +"\n"+
                "STUDENTS: " + set.toString()+ "\n";
    }
}
