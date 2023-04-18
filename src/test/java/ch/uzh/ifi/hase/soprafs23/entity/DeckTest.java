package ch.uzh.ifi.hase.soprafs23.entity;

import ch.qos.logback.core.encoder.EchoEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck deck = new Deck();
    ArrayList<Card> drawnCards = new ArrayList<Card>(66);
    ArrayList<Card> drawnCardsAfterShuffle = new ArrayList<Card>(66);



    @Test
    void fillDeck() {
        for(int i=0;i<66;i++){
            drawnCards.add(deck.draw());
        }
        deck.fillDeck();
        for(int i=0;i<66;i++){
            drawnCardsAfterShuffle.add(deck.draw());
        }
        assertNotEquals(drawnCards, drawnCardsAfterShuffle);
    }

    @Test
    void draw() {
        for(int i=1;i<67; i++){
            deck.draw();
        }
        assertThrows(Exception.class, (Executable) deck.draw());
    }
}