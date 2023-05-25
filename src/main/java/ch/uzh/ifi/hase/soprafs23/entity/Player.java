package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.points.Trick;
import ch.uzh.ifi.hase.soprafs23.constant.CardColor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
public class Player implements Serializable {

    @Id
    private Long id;

    @Column(nullable = false)
    private Long lobby;

    @Column(nullable = false, unique = true)
    private String username;

    private int tricks;
    private Integer bid;
    private ArrayList<Card> hand = new ArrayList<>();
    private int bonus;
    private boolean hasTurn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLobby() {
        return lobby;
    }

    public void setLobby(Long lobby) {
        this.lobby = lobby;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public void setTricks(int tricks) {
        this.tricks = tricks;
    }

    public ArrayList<Card> getHand() {
        Collections.sort(hand, this::compareCards);
        return hand;
    }

    private int compareCards(Card card1, Card card2) {
        boolean isCard1Special = isSpecial(card1);
        boolean isCard2Special = isSpecial(card2);

        if (isCard1Special && !isCard2Special) {
            return 1;
        }
        else if (isCard2Special && !isCard1Special) {
            return -1;
        }
        else if (card1.getColor() == card2.getColor()) {
            return card1.getaRank().compareTo(card2.getaRank());
        }
        else {
            return card1.getColor().compareTo(card2.getColor());
        }
    }

    private boolean isSpecial(Card card) {
        return card.getColor() == CardColor.SPECIAL || card.getColor() == CardColor.BLACK;
    }

    public Integer getBid() {
        return bid;
    }

    public int getBonus() {
        return bonus;
    }

    public int getTricks() {
        return tricks;
    }

    public boolean isHasTurn() {
        return hasTurn;
    }

    public void setHasTurn(boolean b) {
        this.hasTurn = b;
    }

    public void playCard(Card card, Trick trick) {
        if (!trick.getIsTrumpSet() && card.getColor() != CardColor.SPECIAL) {
            trick.setIsTrumpSet(true);
            trick.setTrumpColour(card);
        }
    }
}