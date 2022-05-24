package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.ArrayList;

public class Minstrell extends CharacterCard{
    public Minstrell(){
        super(1,"MINSTRELL");
    }
    // hp: colorsOfEntrance contiene lista dei colori da scambiare da entrance, colorsOfDinner la lista dei colori da scambiare da dinner
    //controllare che abbiano la stessa lunghezza e che gli studenti scelti (sia come colore che come numero) siano realmente presenti nei rispettivi set
    // inoltre la loro lunghezza deve essere minimo 1 e massimo 2
    public void useEffect(Player player, ArrayList<PeopleColor> colorsOfDinner, ArrayList<PeopleColor> colorsOfEntrance, CenterTable table,ArrayList<Player> players) {
        player.reduceCoin(getCost());
        improveCost();
        for(int i=0; i<colorsOfDinner.size();i++) {
            player.getSchoolBoard().getEntranceSpace().removestudent(1, colorsOfEntrance.get(i));
            player.getSchoolBoard().getEntranceSpace().addstudents(1, colorsOfDinner.get(i));
            player.getSchoolBoard().getDinnerTable().removestudent(1,colorsOfDinner.get(i));
            player.getSchoolBoard().getDinnerTable().addstudents(1,colorsOfEntrance.get(i));
        }
        for(PeopleColor color: PeopleColor.values()){
            table.checkProfessor(color,players);
        }
    }


}
