package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.CardColor;
import ch.uzh.ifi.hase.soprafs23.constant.CardRank;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardTest {

    List<Card> cards = Card.getCards();

    @Test
    void getCards() {
        int size = cards.size();
        assertEquals(66, size);
    }

    @Test
    void getColor() {
        assertEquals(CardColor.RED, cards.get(0).getColor());
    }

    @Test
    void getaRank() {
        assertEquals(CardRank.ONE, cards.get(0).getaRank());
    }
}