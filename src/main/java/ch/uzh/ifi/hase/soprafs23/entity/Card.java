package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.CardColor;
import ch.uzh.ifi.hase.soprafs23.constant.CardRank;

import java.util.ArrayList;

public class Card {

    private final CardRank aRank;
    private final CardColor aColor;

    private Card(CardRank rank, CardColor color) {
        aRank = rank;
        aColor = color;
    }
    private static final ArrayList<Card> CARDS =
            new ArrayList<Card>(66);

    static {
        for (CardColor color : CardColor.values()) {
            if (color == CardColor.SPECIAL) {
                //ESCAPE, PIRATE, MERMAID, SCARY_MARY, SKULL_KING
                for (CardRank rank : CardRank.values()) {
                    if (rank.ordinal() >= 13) {
                        CARDS.add(new Card(rank, color));
                    }
                }
                for (CardRank rank : CardRank.values()) {
                    if (rank == CardRank.ESCAPE) {
                        break;
                    }
                    CARDS.add(new Card(rank, color));
                }
            }
        }
    }
    public static ArrayList<Card> getCards() {
        return CARDS;
    }

    public CardColor getColor(Card pCard) {
        return pCard.aColor;
    }

    public CardRank getaRank(Card pCard) {
        return pCard.aRank;
    }

}
