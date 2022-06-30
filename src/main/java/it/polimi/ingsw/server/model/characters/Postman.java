package it.polimi.ingsw.server.model.characters;
import it.polimi.ingsw.server.model.Player;

/**
 * This class contain the methods to use the character POSTMAN described in the rules
 * @author Ignazio Neto Dell'Acqua
 */
public class Postman extends CharacterCard{

    public Postman(){
        super(1,"POSTMAN");
    }
    public void useEffect(Player player) {
        player.reduceCoin(getCost());
        improveCost();
        player.getChosenCard().increaseMoves(2);
    }
}
