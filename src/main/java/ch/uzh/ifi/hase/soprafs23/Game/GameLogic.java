package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Points.Score;
import ch.uzh.ifi.hase.soprafs23.Points.Trick;
import ch.uzh.ifi.hase.soprafs23.constant.CardColor;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Deck;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;

public class GameLogic implements Serializable {
    private ArrayList<Score> Scoreboard = new ArrayList<Score>();

    private Deck deck;
    private GameTable gameTable;
    private int round;
    private Trick trick;
    private ArrayList<Player> players;

    public Deck getDeck(){return this.deck;}
    public GameTable getGameTable(){return this.gameTable;}
    public int getRound() {return round;}
    public Trick getTrick(){return this.trick;}

    public ArrayList<Player> getPlayers() {return players;}
    public void setPlayers(ArrayList<Player> players) {this.players = players;}
    public void addPlayers(Player playerToAdd){this.players.add(playerToAdd);}
    public void setRound(int r){this.round = r;}

    public GameLogic(){
        deck = new Deck();
        gameTable = new GameTable();
        round = 0;
    }

    public void setScoreboard(Score s, Player p) {
    }

    public ArrayList<Score> getScoreboard() {
        return Scoreboard;
    }

    public void distributeCards(){
        deck.fillDeck();
        for(Player p : players) {
            ArrayList<Card> newHand = new ArrayList<Card>();
            for (int i = 0; i < getRound(); i++) {
                newHand.add(deck.draw());
            }
            p.setHand(newHand);
        }
    }

    public void createTrick(){
        Trick newTrick = new Trick();
        trick = newTrick;
        trick.setIsTrumpSet(false);
    }

    public void checkHand() {
        boolean hasTrumpColour = false;
        for (Player p : gameTable.getOrder()) {
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
