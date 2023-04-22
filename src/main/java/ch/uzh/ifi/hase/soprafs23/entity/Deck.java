package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Deck implements Serializable {

    private ArrayList<Card> aCards = new ArrayList<Card>(66);
    private ArrayList<Card> aDeck = new ArrayList<Card>(66);

    public Deck() {
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
        //Collections.shuffle(aDeck);
    }

    public ArrayList<Card> getaDeck(){return aDeck;}

    public Card draw() {
        try {
            return aDeck.remove(aDeck.size() - 1);
        }
        catch (Exception e) {
            System.out.println("Something went wrong... the deck is empty");
        }
        return null;
    }
}
