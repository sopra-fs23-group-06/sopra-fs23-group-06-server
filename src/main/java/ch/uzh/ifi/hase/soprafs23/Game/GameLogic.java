package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Points.Score;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Deck;

import java.util.ArrayList;
import java.util.Scanner;

public class GameLogic {
    private ArrayList<Score> Scoreboard = new ArrayList<Score>();

    private Round round = Round.getInstance();
    private Table table = Table.getInstance();
    private Deck deck = Deck.getInstance();


    public void setScoreboard(Score s, Player p) {
    }

    public ArrayList<Score> getScoreboard() {
        return Scoreboard;
    }

    public void distributeCards(){
        ArrayList<Card> newHand = new ArrayList<Card>();
        deck.fillDeck();

        for(Player p : table.getOrder()) {
            for (int i = 1; i <= round.getRound(); i++) {
                newHand.add(deck.draw());
            }
            p.setHand(newHand);
            newHand.clear();
        }
    }
}
