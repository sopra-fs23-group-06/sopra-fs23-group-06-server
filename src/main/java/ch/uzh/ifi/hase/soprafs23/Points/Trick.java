package ch.uzh.ifi.hase.soprafs23.Points;

import ch.uzh.ifi.hase.soprafs23.entity.Card;

import java.util.ArrayList;
import java.util.List;

public class Trick {
    private List<Card> playedCards = new ArrayList<Card>();

    public void addPlayedCards(Card c){
        playedCards.add(c);
    }

    public List<Card> getPlayedCards() {
        return playedCards;
    }
}
