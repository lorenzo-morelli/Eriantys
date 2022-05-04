package it.polimi.ingsw.server.model;

public class AssistantCard {
    private final int values;
    private final int moves;


    public AssistantCard(int values,int moves){
        this.values = values;
        this.moves = moves;
    }
    public int getMoves() {
        return moves;
    }

    public int getValues() {
        return values;
    }

    /* nessun bisogno di cambiare il contenuto delle carte
    una volta che sono state costruite (vedere classe Deck)
     */
}
