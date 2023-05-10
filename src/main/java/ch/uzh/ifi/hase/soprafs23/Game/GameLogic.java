package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Points.*;
import ch.uzh.ifi.hase.soprafs23.constant.CardColor;
import ch.uzh.ifi.hase.soprafs23.constant.CardOption;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Deck;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameLogic implements Serializable {
    //private ArrayList<Score> Scoreboard = new ArrayList<Score>();

    private Deck deck;
    private GameTable gameTable;
    private int round;
    private Trick trick;

    private Scoreboard scoreboard;
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

    public void setupScoreboard(ArrayList<Player> players) {
        scoreboard = new Scoreboard(players);

    }

    public void setScoreboard(Score score) {
        scoreboard.setScoreboard(score);
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
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
        trick = new Trick();
        trick.setIsTrumpSet(false);
    }

    public void checkHand() {
        for (Player p : gameTable.getOrder()) {
            if (p.isHasTurn()) {
                boolean hasTrumpColour = false;
                if (p != gameTable.getTrickStarter()) {
                    for (Card c : p.getHand()) {
                        if (c.getColor() == trick.getTrumpColour()) {
                            hasTrumpColour = true;
                        }
                    }
                    if (hasTrumpColour) {
                        for (Card c : p.getHand()) {
                            c.setPlayable(c.getColor() == CardColor.SPECIAL || c.getColor() == CardColor.BLACK || c.getColor() == trick.getTrumpColour());
                        }
                    }
                    else {
                        for (Card c : p.getHand()) {
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
            else {
                for (Card c : p.getHand()) {
                    c.setPlayable(false);
                }
            }
        }
    }

    public void nextRound() {
        setRound(getRound()+1);
        List<Player> order = getGameTable().getOrder();
        getGameTable().setRoundStarter(order.get((order.indexOf(gameTable.getRoundStarter()) +1) % order.size()));
        getGameTable().setTrickStarter(gameTable.getRoundStarter());
        distributeCards();
        nextTrick();
    }

    public void nextTrick(){
        createTrick();
        getGameTable().getTrickStarter().setHasTurn(true);
    }



    public void endTrick() {
        Player trickWinner = Evaluate.evaluate(getGameTable(), getTrick());
        trickWinner.setTricks(trickWinner.getTricks()+1);
        getGameTable().setTrickStarter(trickWinner);

        if(addTricksPerRound() == getRound()){
            endRound();
        }
        else{
            nextTrick();
        }
    }

    private void endRound() {
        distributePoints();
        resetBids();
        resetTricks();
        if(getRound() == 10){endGame();}
        else{nextRound();};
    }

    private void endGame() {
        setRound(getRound()+1);
        //IMPLEMENT MORE?
    }


    private void distributePoints() {
        ArrayList<Player> players = getPlayers();
        for (Player player : players){
            int points = Calculate.calculatePoints(player, getRound());
            Score score = new Score();
            score.setCurPlayer(player);
            score.setCurRound(getRound());
            score.setCurBid(player.getBid());
            score.setCurPoints(points);
            score.setCurTricks(player.getTricks());
            setScoreboard(score);
        }
    }

    private void resetBids() {
        ArrayList<Player> players = getPlayers();
        for (Player player : players){
            player.setBid(null);
        }
    }

    private void resetTricks() {
        ArrayList<Player> players = getPlayers();
        for (Player player : players){
            player.setTricks(0);
        }
    }


    private int addTricksPerRound() {
        ArrayList<Player> players = getPlayers();
        int totalTricks = 0;
        for (Player player : players){
            totalTricks += player.getTricks();
        }
        return totalTricks;
    }

}
