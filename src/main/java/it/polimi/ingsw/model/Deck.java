package it.polimi.ingsw.model;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private List<AssistantCard> cards;

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

    // Da un lato vogliamo avere completo accesso all'intera lista di carte affinché
    // l'utente possa sceglierne una e selezionarla
    // dall'altro non vogliamo un getter con cui poi si possa modificare per riferimento
    // l'arraylist (si romperebbe l'incapsulazione dei dati)

    public List<AssistantCard> getCardsList() {
        return Collections.unmodifiableList(this.cards);
    }

    public void remove(int index) {
        this.cards.remove(index);
    }

}




