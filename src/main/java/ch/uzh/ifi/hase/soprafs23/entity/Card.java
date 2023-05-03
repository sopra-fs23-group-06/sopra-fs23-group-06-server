package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.CardColor;
import ch.uzh.ifi.hase.soprafs23.constant.CardOption;
import ch.uzh.ifi.hase.soprafs23.constant.CardRank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Card implements Serializable {

    private final CardRank aRank;
    private final CardColor aColor;
    private CardOption aOption;

    private boolean playable;

    private Card(CardRank rank, CardColor color) {
        aRank = rank;
        aColor = color;
        aOption = CardOption.NONE;
        playable = true;
    }
    private static final ArrayList<Card> CARDS =
            new ArrayList<Card>(66);

    static {
        for (CardColor color : CardColor.values()) {
            if (color == CardColor.SPECIAL) {
                //ESCAPE, PIRATE, MERMAID, SCARY_MARY, SKULL_KING
                for (CardRank rank : CardRank.values()) {
                    if(rank.ordinal() == 15 || rank.ordinal() == 0){
                        for(int i=0; i<5;i++){
                            CARDS.add(new Card(rank,color));
                        }
                    }
                    if(rank.ordinal()==14){
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
                    if (rank == CardRank.ESCAPE){
                        continue;
                    }
                    if (rank == CardRank.MERMAID) {
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

    public CardOption getaOption(){ return this.aOption;}

    public void setaOption(CardOption option){ this.aOption = option;}
    public void setScaryMary(String option){
        if(option.equals("PIRATE")){
            setaOption(CardOption.PIRATE);
        }
        if(option.equals("ESCAPE")) {
            setaOption(CardOption.ESCAPE);
        }
    }

    public static Comparator<Card> byRankComparator() {
        return(card1, card2) -> card1.getaRank().compareTo(card2.getaRank());
    }

}