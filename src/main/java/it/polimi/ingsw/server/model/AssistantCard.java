package it.polimi.ingsw.server.model;


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

    /* nessun bisogno di cambiare il contenuto delle carte //todo eng
    una volta che sono state costruite (vedere classe Deck)
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
