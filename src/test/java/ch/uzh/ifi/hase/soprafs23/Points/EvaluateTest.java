package ch.uzh.ifi.hase.soprafs23.Points;

import ch.uzh.ifi.hase.soprafs23.Points.Evaluate;
import ch.uzh.ifi.hase.soprafs23.Points.Trick;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class EvaluateTest {
    static ArrayList<Card> cards = Card.getCards();
    static Card pirate;
    static Card mermaid;
    static Card skullking;
    static Card one;

    @BeforeAll
    static void setup(){
        pirate = cards.get(60);
        mermaid = cards.get(58);
        skullking = cards.get(65);
        one = cards.get(13);
    }

    @Test
    public void noBonus(){
        Trick trick = mock(Trick.class);
        Player trickWinner = new Player();
        trick.addPlayedCards(one);
        trick.addPlayedCards(one);
        Evaluate.calcBonus(trick, trickWinner);
        assertEquals(0,trickWinner.getBonus());
    }

    @Test
    public void mermaidBonus(){
        Trick trick = new Trick();
        Player trickWinner = new Player();
        trick.addPlayedCards(mermaid);
        trick.addPlayedCards(skullking);
        Evaluate.calcBonus(trick, trickWinner);
        assertEquals(50,trickWinner.getBonus());
    }

    @Test
    public void skullkingBonus(){
        Trick trick = new Trick();
        Player trickWinner = new Player();
        trick.addPlayedCards(pirate);
        trick.addPlayedCards(pirate);
        trick.addPlayedCards(skullking);
        Evaluate.calcBonus(trick, trickWinner);
        assertEquals(60,trickWinner.getBonus());
    }
}