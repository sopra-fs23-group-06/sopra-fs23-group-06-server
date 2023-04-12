package ch.uzh.ifi.hase.soprafs23.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck deck = Deck.getInstance();
    private Deck deck2 = Deck.getInstance();

    //deck.fillDeck();
    @Test
    void getInstance() {
        assertEquals(deck, deck2);
    }

    @Test
    void fillDeck() {
    }

    @Test
    void draw() {
    }
}