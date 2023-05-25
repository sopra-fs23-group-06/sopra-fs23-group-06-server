package ch.uzh.ifi.hase.soprafs23.game;

import ch.uzh.ifi.hase.soprafs23.points.*;
import ch.uzh.ifi.hase.soprafs23.constant.CardColor;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Deck;
import ch.uzh.ifi.hase.soprafs23.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameLogic implements Serializable {

    private Deck deck;
    private GameTable gameTable;
    private int round;
    private Trick trick;
    private Scoreboard scoreboard;
    private ArrayList<Player> players;
    private int roundsToEndGame = 10;

    public Deck getDeck() {
        return this.deck;
    }

    public GameTable getGameTable() {
        return this.gameTable;
    }

    public int getRound() {
        return round;
    }

    public Trick getTrick() {
        return this.trick;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void addPlayers(Player playerToAdd) {
        this.players.add(playerToAdd);
    }

    public void setRound(int r) {
        this.round = r;
    }

    public void setTrick(Trick t) {
        this.trick = t;
    }

    public GameLogic() {
        deck = new Deck();
        gameTable = new GameTable();
        round = 0;
    }

    public void setupScoreboard(ArrayList<Player> players) {
        scoreboard = new Scoreboard(players, roundsToEndGame);

    }

    public void setScoreboard(Score score) {
        scoreboard.setScoreboard(score);
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public void distributeCards() {
        deck.fillDeck();
        for (Player p : players) {
            ArrayList<Card> newHand = new ArrayList<>();
            for (int i = 0; i < getRound(); i++) {
                newHand.add(deck.draw());
            }
            p.setHand(newHand);
        }
    }

    public void createTrick() {
        trick = new Trick();
        trick.setIsTrumpSet(false);
    }

    public void checkHand() {
        boolean allBidsMade = checkAllBidsMade();
        for (Player p : gameTable.getOrder()) {
            if (allBidsMade) {
                handlePlayableCardsForPlayer(p);
            }
            else {
                setAllCardsUnplayable(p);
            }
        }
    }

    private void handlePlayableCardsForPlayer(Player player) {
        if (player.isHasTurn()) {
            boolean hasTrumpColour = player != gameTable.getTrickStarter() && hasTrumpColour(player.getHand());
            updateCardPlayability(player.getHand(), hasTrumpColour);
        }
        else {
            setAllCardsUnplayable(player);
        }
    }

    private void updateCardPlayability(List<Card> hand, boolean hasTrumpColour) {
        for (Card card : hand) {
            if (hasTrumpColour) {
                card.setPlayable(isPlayableCard(card, trick.getTrumpColour()));
            }
            else {
                card.setPlayable(true);
            }
        }
    }

    private boolean hasTrumpColour(List<Card> hand) {
        for (Card c : hand) {
            if (c.getColor() == trick.getTrumpColour()) {
                return true;
            }
        }
        return false;
    }

    private boolean isPlayableCard(Card card, CardColor trumpColour) {
        return card.getColor() == CardColor.SPECIAL || card.getColor() == CardColor.BLACK || card.getColor() == trumpColour;
    }

    private void setAllCardsUnplayable(Player player) {
        for (Card c : player.getHand()) {
            c.setPlayable(false);
        }
    }

    public void nextRound() {
        setRound(getRound() + 1);
        List<Player> order = getGameTable().getOrder();
        getGameTable().setRoundStarter(order.get((order.indexOf(gameTable.getRoundStarter()) + 1) % order.size()));
        getGameTable().setTrickStarter(gameTable.getRoundStarter());
        distributeCards();
        nextTrick();
    }

    public void nextTrick() {
        createTrick();
        getGameTable().getTrickStarter().setHasTurn(true);
    }

    public void endTrick() {
        Player trickWinner = Evaluate.evaluate(getGameTable(), getTrick());
        trickWinner.setTricks(trickWinner.getTricks() + 1);
        getGameTable().setTrickStarter(trickWinner);

        if (addTricksPerRound() == getRound()) {
            endRound();
        }
        else {
            nextTrick();
        }
    }

    public Player getTrickWinner() {
        if (getGameTable().getOrder().isEmpty()) {
            return null;
        }
        else {
            return Evaluate.evaluate(getGameTable(), getTrick());
        }
    }

    private void endRound() {
        distributePoints();
        resetBids();
        resetTricks();
        if (getRound() == roundsToEndGame) {
            endGame();
        }
        else {
            nextRound();
        }
    }

    public void setRoundToEndGame(int r) {
        roundsToEndGame = r;
    }

    public int getRoundToEndGame() {
        return roundsToEndGame;
    }

    private void endGame() {
        setRound(11);
        //IMPLEMENT MORE?
    }

    private void distributePoints() {
        ArrayList<Player> curPlayers = getPlayers();
        for (Player player : curPlayers) {
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
        ArrayList<Player> curPlayers = getPlayers();
        for (Player player : curPlayers) {
            player.setBid(null);
        }
    }

    private void resetTricks() {
        ArrayList<Player> curPlayers = getPlayers();
        for (Player player : curPlayers) {
            player.setTricks(0);
        }
    }

    private int addTricksPerRound() {
        ArrayList<Player> curPlayers = getPlayers();
        int totalTricks = 0;
        for (Player player : curPlayers) {
            totalTricks += player.getTricks();
        }
        return totalTricks;
    }

    private boolean checkAllBidsMade() {
        ArrayList<Player> playerList = getPlayers();
        for (Player player : playerList) {
            if (player.getBid() == null) {
                return false;
            }
        }
        return true;
    }
}