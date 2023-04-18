package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Points.Trick;
import ch.uzh.ifi.hase.soprafs23.constant.CardColor;
import ch.uzh.ifi.hase.soprafs23.entity.Card;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;

public class Player {
    private String username;
    private int tricks;
    private int bid;
    private ArrayList<Card> Hand = new ArrayList<Card>();
    private int bonus;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public void setHand(ArrayList<Card> hand) {
        Hand = hand;
    }

    public void setTricks(int tricks) {
        this.tricks = tricks;
    }

    public ArrayList<Card> getHand() {
        return Hand;
    }

    public int getBid() {
        return bid;
    }

    public int getBonus() {
        return bonus;
    }

    public int getTricks() {
        return tricks;
    }

    public String getUsername() {
        return username;
    }

    public Card playCard(Card card, Trick trick){
        if(trick.getIsTrumpSet()!=true){
            if(card.getColor()!=CardColor.SPECIAL){
                trick.setIsTrumpSet(true);
                trick.setTrumpColour(card);
            }
        }
        return card;
    }
}
