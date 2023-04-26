package ch.uzh.ifi.hase.soprafs23.Points;

import ch.uzh.ifi.hase.soprafs23.constant.CardColor;
import ch.uzh.ifi.hase.soprafs23.constant.CardRank;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Player;

import java.util.ArrayList;

import static ch.uzh.ifi.hase.soprafs23.entity.Card.byRankComparator;

public class Evaluate{
    private Player trickWinner;

    public void setTrickWinner(Player trickWinner) {
        this.trickWinner = trickWinner;
    }

    public Player getTrickWinner(){
        return trickWinner;
    }

    public Card check(Card card1, Card card2, CardColor trumpColor){
        //both cards the same color returns the higher rank
        if(card1.getColor() == card2.getColor()){
            if(byRankComparator().compare(card1, card2) == 0){
                return card1;
            }
            else return card2;
        }
        else if(card1.getColor() == trumpColor){
            if(card2.getColor() == CardColor.SPECIAL){
                if(card2.getaRank() == CardRank.ESCAPE)
                    return card1;
                else return card2;
            }
            else if(card2.getColor() == CardColor.BLACK){
                return card2;
            }
            else return card1;
        }
        else if(card2.getColor() == trumpColor){
            if(card1.getColor() == CardColor.SPECIAL){
                if(card1.getaRank() == CardRank.ESCAPE)
                    return card2;
                else return card1;
            }
            else if(card1.getColor() == CardColor.BLACK){
                return card1;
            }
            else return card2;
        }
        else if(card1.getColor() == CardColor.BLACK){
            if(card2.getColor() == CardColor.SPECIAL){
                if(byRankComparator().compare(card1, card2) == 0){
                    return card1;
                }
                else return card2;
            }
            else return card1;
        }
        else if(card2.getColor() == CardColor.BLACK){
            if(card1.getColor() == CardColor.SPECIAL){
                if(byRankComparator().compare(card1, card2) == 0){
                    return card1;
                }
                else return card2;
            }
            else return card2;
        }
        else if(byRankComparator().compare(card1, card2) == 0){
                return card1;
            }
            else return card2;
    }

    public void compareCards(Trick trick){
        ArrayList<Card> playedCards = (ArrayList<Card>) trick.getPlayedCards();
        int l = playedCards.size();
        Card highestCard = playedCards.get(0);
        for (int i = 1; i < l; i++){
            highestCard = check(highestCard, playedCards.get(i),trick.getTrumpColour());
            if (highestCard.getaRank() == CardRank.SKULL_KING){
                for(Card card : playedCards){
                    if(card.getaRank() == CardRank.MERMAID){
                        highestCard = card;
                        break;
                    }
                }
            }
        }
    }


    public void calcBonus(Trick trick) {
        int awardedBonus = 0;
        ArrayList<Card> playedCards = (ArrayList<Card>) trick.getPlayedCards();
        for (Card card : playedCards) {
            if (card.getaRank() == CardRank.SKULL_KING) {
                for (Card c : playedCards){
                    if (c.getaRank() == CardRank.PIRATE){
                        for (Card car : playedCards) {
                            if (car.getaRank() == CardRank.PIRATE) {
                                awardedBonus += 30;
                            }
                        }
                    }
                    else if (c.getaRank() == CardRank.MERMAID){
                        awardedBonus = 50;
                    }
                }
            }
        }
        getTrickWinner().setBonus(awardedBonus);
    }
}
