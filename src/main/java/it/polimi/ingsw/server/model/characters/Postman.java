package it.polimi.ingsw.server.model.characters;
import it.polimi.ingsw.server.model.Player;

public class Postman extends CharacterCard{

    public Postman(){
        super(1,"POSTMAN");
    }
    public void useEffect(Player player) {
        player.reduceCoin(getCost());
        improveCost();
        player.getChoosedCard().improveMoves(2);
    }
}
