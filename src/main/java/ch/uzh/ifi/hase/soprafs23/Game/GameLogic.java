package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Points.Score;
import ch.uzh.ifi.hase.soprafs23.Points.Trick;
import ch.uzh.ifi.hase.soprafs23.constant.CardColor;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Deck;
import ch.uzh.ifi.hase.soprafs23.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class GameLogic implements Serializable {
    private ArrayList<Score> Scoreboard = new ArrayList<Score>();

    private Deck deck;
    private GameTable gameTable;
    private int round;
    private Trick trick;

    public Deck getDeck(){return this.deck;}
    public GameTable getGameTable(){return this.gameTable;}
    public int getRound() {return round;}
    public Trick getTrick(){return this.trick;}

    public void setRound(int r){this.round = r;}

    public GameLogic(Deck d, GameTable gT){
        deck = d;
        gameTable = gT;
        round = 0;
    }

    public void setScoreboard(Score s, Player p) {
    }

    public ArrayList<Score> getScoreboard() {
        return Scoreboard;
    }

    public void distributeCards(){
        ArrayList<Card> newHand = new ArrayList<Card>();
        deck.fillDeck();

        for(Player p : gameTable.getOrder()) {
            for (int i = 1; i <= round; i++) {
                newHand.add(deck.draw());
            }
            p.setHand(newHand);
            newHand.clear();
        }
    }

    public void createTrick(){
        Trick newTrick = new Trick();
        trick = newTrick;
        trick.setIsTrumpSet(false);
    }

    public void checkHand() {
        for (Player p : gameTable.getOrder()) {
            boolean hasTrumpColour = false;
            if (p != gameTable.getStartingPlayer()) {
                for (Card c : p.getHand()) {
                    if (c.getColor() == trick.getTrumpColour()) {
                        hasTrumpColour = true;
                        break;
                    }
                }
                if (hasTrumpColour) {
                    for (Card c : p.getHand()) {
                        c.setPlayable(c.getColor() == CardColor.SPECIAL || c.getColor() == CardColor.BLACK ||c.getColor() == trick.getTrumpColour());
                    }
                }
                else {
                    for (Card c : p.getHand()) {
                        c.setPlayable(true);
                    }
                }
            }
            else{
                for(Card c : p.getHand()){
                    c.setPlayable(true);
                }
            }
        }
    }


}
