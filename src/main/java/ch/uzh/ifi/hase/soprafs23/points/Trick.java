package ch.uzh.ifi.hase.soprafs23.points;

import ch.uzh.ifi.hase.soprafs23.constant.CardColor;
import ch.uzh.ifi.hase.soprafs23.entity.Card;

import java.io.Serializable;
import java.util.ArrayList;

public class Trick implements Serializable {
    private ArrayList<Card> playedCards = new ArrayList<>();
    private CardColor trumpColour;
    private boolean isTrumpSet;

    public void setTrumpColour(Card firstCardPlayed) {
        trumpColour = firstCardPlayed.getColor();
    }

    public CardColor getTrumpColour() {
        return trumpColour;
    }

    public void setIsTrumpSet(boolean trumpSet) {
        isTrumpSet = trumpSet;
    }

    public boolean getIsTrumpSet() {
        return isTrumpSet;
    }

    public void addPlayedCards(Card c) {
        playedCards.add(c);
    }

    public ArrayList<Card> getPlayedCards() {
        return playedCards;
    }
}