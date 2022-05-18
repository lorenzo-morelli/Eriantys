package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;

public class Postman extends CharacterCard{

    public Postman(){
        super("Puoi muovere madre natura fino a due isole addizionali rispetto a quanto indicato sulla carta assistente che hai giocato",1,"POSTMAN");
    }
    public void useEffect(Player player) {
        player.reduceCoin(getCost());
        improveCost();
        player.getChoosedCard().improveMoves(2);
    }
@Override
    public String toString() {
        return "POSTAMAN - " + super.toString();
    }
}
