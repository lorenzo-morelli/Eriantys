package it.polimi.ingsw.server.model;

/**
 * This class is the data model that describes and implements
 * the assistant cards, formalized with the two characteristic
 * values, the pair <value, moves>
 *
 * @author Ignazio Neto Dell'Acqua
 */

public class AssistantCard {
    private float values;
    private int moves;


    public AssistantCard(int values, int moves) {
        this.values = values;
        this.moves = moves;
    }

    public int getMoves() {
        return moves;
    }

    public float getValues() {
        return values;
    }

    /* no need to change the content of the cards
    once they have been built (see Deck class)
     */

    /**
     * Method needed to handle special rules of the games
     * (clever way to allow two users to choose the same card
     * under certain conditions)
     */
    public void lowPriority() {
        values += 0.5;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AssistantCard) {
            if (this.getMoves() != ((AssistantCard) obj).getMoves()) return false;
            return this.getValues() == ((AssistantCard) obj).getValues();
        }
        return false;
    }

    public void improveMoves(int moves) {
        this.moves += moves;
    }

    @Override
    public String toString() {
        return (int) values +
                "/" + moves;
    }
}
