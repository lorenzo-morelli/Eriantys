package it.polimi.ingsw.server.model;

import java.util.*;

public class Deck {
    private final List<AssistantCard> cards;

    public Deck() {
        this.cards = new ArrayList<>();
        // adesso c'è da inizializzare il deck appena costruito
        this.cards.add(new AssistantCard(1, 1));
        this.cards.add(new AssistantCard(2, 1));
        this.cards.add(new AssistantCard(3, 2));
        this.cards.add(new AssistantCard(4, 2));
        this.cards.add(new AssistantCard(5, 3));
        this.cards.add(new AssistantCard(6, 3));
        this.cards.add(new AssistantCard(7, 4));
        this.cards.add(new AssistantCard(8, 4));
        this.cards.add(new AssistantCard(9, 5));
        this.cards.add(new AssistantCard(10, 5));
    }

    public List<AssistantCard> getCardsList() {
        return Collections.unmodifiableList(this.cards);
    }

    public boolean remove(AssistantCard choosed) {
        if (inDeck(choosed)) {
            cards.removeIf(assistantCard -> assistantCard.equals(choosed));
            return cards.size() == 0;
        }else {
            throw new IllegalArgumentException();
        }
    }
    public boolean inDeck(AssistantCard card){
        for (AssistantCard assistantCard : cards) {
            if (assistantCard.getValues() == card.getValues() && assistantCard.getMoves() == card.getMoves())
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return  cards.toString();
    }
}
