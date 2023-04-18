package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.CardColor;
import ch.uzh.ifi.hase.soprafs23.constant.CardRank;

import java.util.ArrayList;

public class Card {

    private final CardRank aRank;
    private final CardColor aColor;

    private boolean playable;

    private Card(CardRank rank, CardColor color) {
        aRank = rank;
        aColor = color;
        playable = true;
    }
    private static final ArrayList<Card> CARDS =
            new ArrayList<Card>(66);

    static {
        for (CardColor color : CardColor.values()) {
            if (color == CardColor.SPECIAL) {
                //ESCAPE, PIRATE, MERMAID, SCARY_MARY, SKULL_KING
                for (CardRank rank : CardRank.values()) {
                    if(rank.ordinal() >= 13 && rank.ordinal() < 15){
                        for(int i=0; i<5;i++){
                            CARDS.add(new Card(rank,color));
                        }
                    }
                    if(rank.ordinal()==15){
                        for(int i=0;i<2;i++){
                            CARDS.add(new Card(rank, color));
                        }
                    }
                    if (rank.ordinal() >= 16) {
                        CARDS.add(new Card(rank, color));
                    }
                }
            }
            else{
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

    public CardColor getColor() {
        return this.aColor;
    }

    public void setPlayable(boolean playable) {
        this.playable = playable;
    }

    public boolean getPlayable(){
        return playable;
    }

    public CardRank getaRank() {
        return this.aRank;
    }

}
