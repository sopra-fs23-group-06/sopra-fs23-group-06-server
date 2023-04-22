package ch.uzh.ifi.hase.soprafs23.Points;

import ch.uzh.ifi.hase.soprafs23.constant.CardRank;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Player;

import java.util.ArrayList;

public class Evaluate {
    private Player trickWinner;

    public void setTrickWinner(Player trickWinner) {
        this.trickWinner = trickWinner;
    }

    public void setBonus() {
        Trick trick = new Trick();
        int bonus = 0;
        ArrayList<Card> playedCards = (ArrayList<Card>) trick.getPlayedCards();
        for (Card card : playedCards) {
            if (card.getaRank() == CardRank.SKULL_KING) {
                for (Card c : playedCards){
                    if (c.getaRank() == CardRank.PIRATE){
                        for (Card car : playedCards) {
                            if (car.getaRank() == CardRank.PIRATE) {
                                bonus += 30;
                            }
                        }
                    }
                    else if (c.getaRank() == CardRank.MERMAID){
                        bonus = 50;
                    }

                }
            }
            trickWinner.setBonus(bonus);
        }
    }
}
