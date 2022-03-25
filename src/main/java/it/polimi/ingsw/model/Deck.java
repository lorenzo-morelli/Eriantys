package it.polimi.ingsw.model;
import java.util.ArrayList;
public class Deck {
    private ArrayList<AssistantCard> cards;

    public Deck(){
        this.cards= new ArrayList<>();
        // adesso c'è da inizializzare il deck appena costruito
        this.cards.add(new AssistantCard(1,1));
        this.cards.add(new AssistantCard(2,1));
        this.cards.add(new AssistantCard(3,2));
        this.cards.add(new AssistantCard(4,2));
        this.cards.add(new AssistantCard(5,3));
        this.cards.add(new AssistantCard(6,3));
        this.cards.add(new AssistantCard(7,4));
        this.cards.add(new AssistantCard(8,4));
        this.cards.add(new AssistantCard(9,5));
        this.cards.add(new AssistantCard(10,5));
    }

    public ArrayList<AssistantCard> getCards() {
        return this.cards;
    }

    public void setCards(ArrayList<AssistantCard> cards) {
        this.cards = cards;
    }

    //todo: funzioni di utilità: rimozione e ricerca di una carta dal deck
}
