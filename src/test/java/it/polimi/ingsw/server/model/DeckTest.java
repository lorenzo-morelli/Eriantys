package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void testdeck() throws Exception{
        Deck deck=new Deck();
        assertEquals(10,deck.getCardsList().size());
        AssistantCard card=new AssistantCard(1,1);
        assertTrue(deck.inDeck(card));
        assertFalse(deck.remove(card));
        assertFalse(deck.inDeck(card));
        assertEquals(9,deck.getCardsList().size());
        AssistantCard card2 = new AssistantCard(2, 1);
        assertTrue(deck.inDeck(card2));
        assertFalse(deck.remove(card2));
        assertFalse(deck.inDeck(card2));
        assertEquals(8,deck.getCardsList().size());
        AssistantCard card3 = new AssistantCard(3, 2);
        assertTrue(deck.inDeck(card3));
        assertFalse(deck.remove(card3));
        assertFalse(deck.inDeck(card3));
        assertEquals(7,deck.getCardsList().size());
        AssistantCard card6 = new AssistantCard(6, 3);
        assertTrue(deck.inDeck(card6));
        assertFalse(deck.remove(card6));
        assertFalse(deck.inDeck(card6));
        assertEquals(6,deck.getCardsList().size());
        AssistantCard card4 = new AssistantCard(4, 2);
        assertTrue(deck.inDeck(card4));
        assertFalse(deck.remove(card4));
        assertFalse(deck.inDeck(card4));
        assertEquals(5,deck.getCardsList().size());
        AssistantCard card5 = new AssistantCard(5, 3);
        assertTrue(deck.inDeck(card5));
        assertFalse(deck.remove(card5));
        assertFalse(deck.inDeck(card5));
        assertEquals(4,deck.getCardsList().size());
        AssistantCard card9 = new AssistantCard(9, 5);
        assertTrue(deck.inDeck(card9));
        assertFalse(deck.remove(card9));
        assertFalse(deck.inDeck(card9));
        assertThrows(IllegalArgumentException.class,()->{
            assertFalse(deck.remove(card3));
        });
        assertEquals(3,deck.getCardsList().size());
        AssistantCard card10 = new AssistantCard(10, 5);
        assertTrue(deck.inDeck(card10));
        assertFalse(deck.remove(card10));
        assertFalse(deck.inDeck(card10));
        assertEquals(2,deck.getCardsList().size());
        AssistantCard card7 = new AssistantCard(7, 4);
        assertTrue(deck.inDeck(card7));
        assertFalse(deck.remove(card7));
        assertFalse(deck.inDeck(card7));
        assertEquals(1,deck.getCardsList().size());
        AssistantCard card8 = new AssistantCard(8, 4);
        assertTrue(deck.inDeck(card8));
        assertTrue(deck.remove(card8));
        assertFalse(deck.inDeck(card8));
        assertEquals(0,deck.getCardsList().size());
    }
}