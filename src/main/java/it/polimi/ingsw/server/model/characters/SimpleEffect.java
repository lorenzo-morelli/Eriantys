package it.polimi.ingsw.server.model.characters;

import it.polimi.ingsw.server.model.CenterTable;
import it.polimi.ingsw.server.model.Player;

/**
 * All the character that has Simple effects implements this Interface (They need only the player and the table to manage its effect)
 */
public interface SimpleEffect {
    @SuppressWarnings("unused")
    void useEffect(Player player, CenterTable table);

}
