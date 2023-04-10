package ch.uzh.ifi.hase.soprafs23.entity;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private static Deck deckObject;
    private ArrayList<Card> aCards = new ArrayList<Card>(66);
    private ArrayList<Card> aDeck = new ArrayList<Card>(66);

    public static Deck getInstance(){
        if(deckObject == null) {
            deckObject = new Deck();
        }
        return deckObject;
    }

    private Deck() {
        for (Card c : Card.getCards()) {
            aCards.add(c);
        }
        fillDeck();
    }

    public void fillDeck() {
        aDeck.clear();
        for (Card c : aCards) {
            aDeck.add(c);
        }
        Collections.shuffle(aDeck);
    }

    public Card draw() {
        assert aDeck.size() != 0;
        return aDeck.remove(aDeck.size() - 1);
    }
}
