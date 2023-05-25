package ch.uzh.ifi.hase.soprafs23.points;

import ch.uzh.ifi.hase.soprafs23.game.GameTable;
import ch.uzh.ifi.hase.soprafs23.constant.CardColor;
import ch.uzh.ifi.hase.soprafs23.constant.CardOption;
import ch.uzh.ifi.hase.soprafs23.constant.CardRank;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static ch.uzh.ifi.hase.soprafs23.entity.Card.byRankComparator;

public class Evaluate {

    private Evaluate() {
        throw new IllegalStateException("Utility class");
    }

    public static Player evaluate(GameTable gameTable, Trick trick) {
        List<Player> order = gameTable.getOrder();
        Card high = compareCards(trick);
        int index = trick.getPlayedCards().indexOf(high);
        Player winner = order.get((order.indexOf(gameTable.getTrickStarter()) + index) % order.size());
        calcBonus(trick, winner);
        return winner;
    }

    public static Card check(Card card1, Card card2, CardColor trumpColor) {
        if (card1.getColor() == card2.getColor()) {
            return getHigherRankCard(card1, card2);
        }
        else if (card1.getColor() == trumpColor) {
            return handleTrumpColor(card1, card2);
        }
        else if (card2.getColor() == trumpColor) {
            return handleTrumpColor(card2, card1);
        }
        else if (card1.getColor() == CardColor.BLACK) {
            return handleBlackColor(card1, card2);
        }
        else if (card2.getColor() == CardColor.BLACK) {
            return handleBlackColor(card2, card1);
        }
        else {
            return getHigherRankCard(card1, card2);
        }
    }

    private static Card getHigherRankCard(Card card1, Card card2) {
        return byRankComparator().compare(card1, card2) >= 0 ? card1 : card2;
    }

    private static Card handleTrumpColor(Card trumpCard, Card otherCard) {
        if (otherCard.getColor() == CardColor.SPECIAL) {
            return otherCard.getaRank() == CardRank.ESCAPE ? trumpCard : otherCard;
        }
        else if (otherCard.getColor() == CardColor.BLACK) {
            return otherCard;
        }
        else {
            return trumpCard;
        }
    }

    private static Card handleBlackColor(Card blackColorCard, Card otherCard) {
        if (otherCard.getColor() == CardColor.SPECIAL) {
            return getHigherRankCard(blackColorCard, otherCard);
        }
        else {
            return blackColorCard;
        }
    }

    public static Card checkScary(Card card1, Card card2) {
        if (isScaryMary(card1)) {
            return compareScaryMary(card1, card2);
        }
        else {
            return compareOtherCards(card1, card2);
        }
    }

    private static boolean isScaryMary(Card card) {
        return card.getaRank() == CardRank.SCARY_MARY;
    }

    private static Card compareScaryMary(Card card1, Card card2) {
        if (card1.getaOption() == CardOption.ESCAPE) {
            return card2.getaRank() == CardRank.ESCAPE ? card1 : card2;
        }
        else {
            return card2.getaRank() == CardRank.SKULL_KING ? card2 : card1;
        }
    }

    private static Card compareOtherCards(Card card1, Card card2) {
        if (card2.getaOption() == CardOption.ESCAPE) {
            return card1;
        }
        else {
            return card1.getaRank() == CardRank.SKULL_KING || card1.getaRank() == CardRank.PIRATE ? card1 : card2;
        }
    }

    public static Card compareCards(Trick trick) {
        ArrayList<Card> playedCards = trick.getPlayedCards();
        int l = playedCards.size();
        Card highestCard = playedCards.get(0);
        for (int i = 1; i < l; i++) {
            if (playedCards.get(i).getaRank() == CardRank.SCARY_MARY || highestCard.getaRank() == CardRank.SCARY_MARY) {
                highestCard = checkScary(highestCard, playedCards.get(i));
            }
            else {
                highestCard = check(highestCard, playedCards.get(i), trick.getTrumpColour());
            }
        }
        if (highestCard.getaRank() == CardRank.SKULL_KING) {
            for (Card card : playedCards) {
                if (card.getaRank() == CardRank.MERMAID) {
                    highestCard = card;
                    break;
                }
            }
        }
        return highestCard;
    }

    public static void calcBonus(Trick trick, Player winner) {
        int awardedBonus = 0;
        ArrayList<Card> playedCards = trick.getPlayedCards();
        for (Card card : playedCards) {
            if (card.getaRank() == CardRank.SKULL_KING) {
                for (Card c : playedCards) {
                    if (c.getaRank() == CardRank.PIRATE || c.getaRank() == CardRank.SCARY_MARY) {
                        awardedBonus += 30;
                    }
                    else if (c.getaRank() == CardRank.MERMAID) {
                        awardedBonus = 50;
                        break;
                    }
                }
            }
        }
        winner.setBonus(awardedBonus);
    }
}