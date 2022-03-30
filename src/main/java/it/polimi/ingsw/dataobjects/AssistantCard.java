package it.polimi.ingsw.dataobjects;

public class AssistantCard {
    private int values;
    private int moves;


    public AssistantCard(int values,int moves){
        this.values = values;
        this.moves = moves;
    }
    public AssistantCard(){
        // creazione di una carta nulla
        this.values = 0;
        this.moves = 0;
    }

    public int getMoves() {
        return moves;
    }

    public int getValues() {
        return values;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public void setValues(int values) {
        this.values = values;
    }
}
