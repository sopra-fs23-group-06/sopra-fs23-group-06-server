package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.CardColor;
import ch.uzh.ifi.hase.soprafs23.constant.CardRank;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardTest {

    ArrayList<Card> cards = Card.getCards();

    @Test
    void getCards() {
        int size = cards.size();
        assertEquals(size, 66);
    }

    @Test
    void getColor() {
        assertEquals(cards.get(0).getColor(), CardColor.RED);
    }

    @Test
    void getaRank() {
        assertEquals(cards.get(0).getaRank(), CardRank.ONE);
    }
}