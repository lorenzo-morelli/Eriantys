package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enums.PeopleColor;

import java.util.ArrayList;

public class Thief extends CharacterCard{
    public Thief(){
        super("Scegli un colore di uno studente: ogni giocatore incluso te deve rimettere nel bag 3 studenti del colore scelto presenti nella sala (o tutti quelli che ha se ne avesse meno di 3)",3,"THIEF");
    }

    public void useEffect(Player player, ArrayList<Player> players, PeopleColor color,CenterTable table) {
        player.reduceCoin(getCost());
        improveCost();
        for (Player value : players) {
            value.getSchoolBoard().getDinnerTable().removestudentinBag(3, color, table.getBag());
        }
    }
    @Override
    public String toString() {
        return "THIEF - " + super.toString();
    }
}