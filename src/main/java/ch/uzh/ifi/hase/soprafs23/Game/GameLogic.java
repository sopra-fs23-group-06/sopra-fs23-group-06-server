package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Points.Score;
import ch.uzh.ifi.hase.soprafs23.Points.Trick;
import ch.uzh.ifi.hase.soprafs23.constant.CardColor;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Deck;

import java.util.ArrayList;

public class GameLogic {
    private ArrayList<Score> Scoreboard = new ArrayList<Score>();

    private Deck deck;
    private GameTable gameTable;
    private Round round;
    private Trick trick;

    public GameLogic(Deck d, GameTable gT, Round r){
        deck = d;
        gameTable = gT;
        round = r;
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
            for (int i = 1; i <= round.getRound(); i++) {
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
        Boolean hasTrumpColour = false;
        for (Player p : gameTable.getOrder()) {
            if (p != gameTable.getStartingPlayer()) {
                for (Card c : p.getHand()) {
                    if (c.getColor() == trick.getTrumpColour()) {
                        hasTrumpColour = true;
                    }
                }
                if (hasTrumpColour == true) {
                    for (Card c : p.getHand()) {
                        if (c.getColor() != CardColor.SPECIAL && c.getColor() != trick.getTrumpColour()) {
                            c.setPlayable(false);
                        }
                        else {
                            c.setPlayable(true);
                        }
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
