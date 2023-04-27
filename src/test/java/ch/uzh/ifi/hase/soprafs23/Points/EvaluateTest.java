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
    }

 /*   @Test
    public void noBonus(){
        Trick trick = mock(Trick.class);
        Player trickWinner = mock(Player.class);
        Evaluate evaluate = mock(Evaluate.class);
        List<Card> noBonus = new ArrayList<>();
        noBonus.add(one);
        noBonus.add(skullking);
        Mockito.when(trick.getPlayedCards()).thenReturn(noBonus);
        Mockito.when(trickWinner).thenReturn(trickWinner);
        evaluate.calcBonus(trick);
        assertEquals(0,trickWinner.getBonus());
    }

    @Test
    public void mermaidBonus(){
        Trick trick = mock(Trick.class);
        Player trickWinner = new Player();
        Evaluate evaluate = mock(Evaluate.class);
        List<Card> mermaidBonus = new ArrayList<>();
        mermaidBonus.add(mermaid);
        mermaidBonus.add(skullking);
        Mockito.when(trick.getPlayedCards()).thenReturn(mermaidBonus);
        Mockito.when(trickWinner).thenReturn(trickWinner);
        evaluate.calcBonus(trick);
        assertEquals(50,trickWinner.getBonus());
    }

    @Test
    public void skullkingBonus(){
        Trick trick = mock(Trick.class);
        Player trickWinner = mock(Player.class);
        Evaluate evaluate = mock(Evaluate.class);
        List<Card> skullkingBonus = new ArrayList<>();
        skullkingBonus.add(skullking);
        skullkingBonus.add(pirate);
        skullkingBonus.add(pirate);
        Mockito.when(trick.getPlayedCards()).thenReturn(skullkingBonus);
        Mockito.when(trickWinner).thenReturn(trickWinner);
        evaluate.calcBonus(trick);
        assertEquals(60,trickWinner.getBonus());
    }*/
}