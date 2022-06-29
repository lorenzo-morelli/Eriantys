package it.polimi.ingsw.server.model;

import java.util.*;

/**
 * This class defines and implements the assistant deck of cards
 *
 * @author Fernando Morea
 * @author Ignazio Neto Dell'Acqua
 */
public class Deck {
    private final List<AssistantCard> cards;

    public Deck() {
        this.cards = new ArrayList<>();
        // now it is time to initialize the newly built deck
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

    /**
     * Method without side effects since it returns an immutable list
     * @return collection of unmodifiable Assistant Cards
     */
    public List<AssistantCard> getCardsList() {
        return Collections.unmodifiableList(this.cards);
    }

    /**
     * Remove card from the deck
     * @param chose card to delete
     * @return a boolean flag that represent the status of the operation
     */
    public boolean remove(AssistantCard chose) {
        if (inDeck(chose)) {
            cards.removeIf(assistantCard -> assistantCard.equals(chose));
            return cards.size() == 0;
        }else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Boolean function to establish if a card is in the deck
     * @param card card to verify
     * @return boolean value
     */
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
