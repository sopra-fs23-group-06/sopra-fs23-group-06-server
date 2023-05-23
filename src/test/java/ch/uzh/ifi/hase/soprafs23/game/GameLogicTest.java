package ch.uzh.ifi.hase.soprafs23.game;

import ch.uzh.ifi.hase.soprafs23.points.Trick;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameLogicTest {
    static List<Card> cards = Card.getCards();
    static Card pirate;
    static Card mermaid;
    static Card skullking;
    static Card oneRed;
    static Card oneBlue;
    static Card oneBlack;
    static Card twoRed;
    static Card scarymary;
    static Card escape;

    @BeforeAll
    static void setup(){
        pirate = cards.get(60);
        mermaid = cards.get(58);
        skullking = cards.get(65);
        oneBlue = cards.get(13);
        oneBlack = cards.get(39);
        oneRed = cards.get(0);
        twoRed = cards.get(1);
        scarymary = cards.get(64);
        escape = cards.get(52);
    }
    @Test
    void checkHandAllBidsNotMade(){
        GameLogic gameLogic = new GameLogic();
        GameTable gameTable = gameLogic.getGameTable();

        Player player1 = new Player();
        player1.setId(1L);
        player1.setLobby(123456L);
        player1.setUsername("username1");
        Player player2 = new Player();
        player2.setId(2L);
        player2.setLobby(123456L);
        player2.setUsername("username2");

        ArrayList<Card> player1Hand = new ArrayList<>();
        oneRed.setPlayable(true);
        player1Hand.add(oneRed);
        player1.setHand(player1Hand);
        ArrayList<Card> player2Hand = new ArrayList<>();
        twoRed.setPlayable(true);
        player2Hand.add(twoRed);
        player2.setHand(player2Hand);

        ArrayList<Player> players = new ArrayList<>();
        gameLogic.setPlayers(players);
        gameLogic.addPlayers(player1);
        gameLogic.addPlayers(player2);
        gameTable.addPlayer(player1);
        gameTable.addPlayer(player2);
        gameTable.setTrickStarter(player2);
        gameLogic.checkHand();

        assertNotNull(player1.getHand());
        assertNotNull(player2.getHand());
        assertFalse(player1.getHand().get(0).getPlayable());
        assertFalse(player2.getHand().get(0).getPlayable());
    }
    @Test
    void checkHandAllBidsMade(){
        GameLogic gameLogic = new GameLogic();
        GameTable gameTable = gameLogic.getGameTable();

        Player player1 = new Player();
        player1.setId(1L);
        player1.setLobby(123456L);
        player1.setUsername("username1");
        player1.setBid(1);
        player1.setHasTurn(true);
        Player player2 = new Player();
        player2.setId(2L);
        player2.setLobby(123456L);
        player2.setUsername("username2");
        player2.setBid(1);

        ArrayList<Card> player1Hand = new ArrayList<>();
        oneRed.setPlayable(true);
        player1Hand.add(oneRed);
        player1.setHand(player1Hand);
        ArrayList<Card> player2Hand = new ArrayList<>();
        twoRed.setPlayable(true);
        player2Hand.add(twoRed);
        player2.setHand(player2Hand);

        ArrayList<Player> players = new ArrayList<>();
        gameLogic.setPlayers(players);
        gameLogic.addPlayers(player1);
        gameLogic.addPlayers(player2);
        gameTable.addPlayer(player1);
        gameTable.addPlayer(player2);
        gameTable.setTrickStarter(player1);
        gameLogic.checkHand();

        assertNotNull(player1.getHand());
        assertNotNull(player2.getHand());
        assertTrue(player1.getHand().get(0).getPlayable());
        assertFalse(player2.getHand().get(0).getPlayable());
    }

    @Test
    void checkHandAllBidsMadeNoTrumpColour(){
        GameLogic gameLogic = new GameLogic();
        Trick trick = new Trick();
        GameTable gameTable = gameLogic.getGameTable();

        Player player1 = new Player();
        player1.setId(1L);
        player1.setLobby(123456L);
        player1.setUsername("username1");
        player1.setBid(1);
        player1.setHasTurn(true);
        Player player2 = new Player();
        player2.setId(2L);
        player2.setLobby(123456L);
        player2.setUsername("username2");
        player2.setBid(1);

        ArrayList<Card> player1Hand = new ArrayList<>();
        oneRed.setPlayable(true);
        player1Hand.add(oneRed);
        player1.setHand(player1Hand);
        ArrayList<Card> player2Hand = new ArrayList<>();
        twoRed.setPlayable(true);
        player2Hand.add(twoRed);
        player2.setHand(player2Hand);

        trick.setTrumpColour(oneBlue);
        gameLogic.setTrick(trick);
        ArrayList<Player> players = new ArrayList<>();
        gameLogic.setPlayers(players);
        gameLogic.addPlayers(player1);
        gameLogic.addPlayers(player2);
        gameTable.addPlayer(player1);
        gameTable.addPlayer(player2);
        gameTable.setTrickStarter(player2);
        gameLogic.checkHand();

        assertNotNull(player1.getHand());
        assertNotNull(player2.getHand());
        assertTrue(player1.getHand().get(0).getPlayable());
        assertFalse(player2.getHand().get(0).getPlayable());
    }
    @Test
    void checkHandAllBidsMadeHasTrumpColour(){
        GameLogic gameLogic = new GameLogic();
        Trick trick = new Trick();
        GameTable gameTable = gameLogic.getGameTable();

        Player player1 = new Player();
        player1.setId(1L);
        player1.setLobby(123456L);
        player1.setUsername("username1");
        player1.setBid(1);
        player1.setHasTurn(true);
        Player player2 = new Player();
        player2.setId(2L);
        player2.setLobby(123456L);
        player2.setUsername("username2");
        player2.setBid(1);

        ArrayList<Card> player1Hand = new ArrayList<>();
        oneRed.setPlayable(true);
        player1Hand.add(oneRed);
        player1.setHand(player1Hand);
        ArrayList<Card> player2Hand = new ArrayList<>();
        twoRed.setPlayable(true);
        player2Hand.add(twoRed);
        player2.setHand(player2Hand);

        trick.setTrumpColour(oneRed);
        gameLogic.setTrick(trick);
        ArrayList<Player> players = new ArrayList<>();
        gameLogic.setPlayers(players);
        gameLogic.addPlayers(player1);
        gameLogic.addPlayers(player2);
        gameTable.addPlayer(player1);
        gameTable.addPlayer(player2);
        gameTable.setTrickStarter(player2);
        gameLogic.checkHand();

        assertNotNull(player1.getHand());
        assertNotNull(player2.getHand());
        assertTrue(player1.getHand().get(0).getPlayable());
        assertFalse(player2.getHand().get(0).getPlayable());
    }
    @Test
    void endTrick(){
        GameLogic gameLogic = new GameLogic();
        Trick trick = new Trick();
        GameTable gameTable = gameLogic.getGameTable();

        Player player1 = new Player();
        player1.setId(1L);
        player1.setLobby(123456L);
        player1.setUsername("username1");
        player1.setBid(1);
        player1.setHasTurn(true);
        player1.setTricks(0);
        Player player2 = new Player();
        player2.setId(2L);
        player2.setLobby(123456L);
        player2.setUsername("username2");
        player2.setBid(1);
        player2.setTricks(0);

        ArrayList<Card> player1Hand = new ArrayList<>();
        oneRed.setPlayable(true);
        player1Hand.add(oneRed);
        player1.setHand(player1Hand);
        ArrayList<Card> player2Hand = new ArrayList<>();
        twoRed.setPlayable(true);
        player2Hand.add(twoRed);
        player2.setHand(player2Hand);

        trick.addPlayedCards(oneRed);
        trick.addPlayedCards(pirate);
        trick.addPlayedCards(skullking);
        trick.addPlayedCards(mermaid);
        trick.setIsTrumpSet(true);
        trick.setTrumpColour(oneRed);
        gameLogic.setTrick(trick);
        ArrayList<Player> players = new ArrayList<>();
        gameLogic.setPlayers(players);
        gameLogic.addPlayers(player1);
        gameLogic.addPlayers(player2);
        gameTable.addPlayer(player1);
        gameTable.addPlayer(player2);
        gameTable.setTrickStarter(player2);
        gameLogic.endTrick();

        assertEquals(0, player2.getTricks());
        assertEquals(1, player1.getTricks());
        assertEquals(gameTable.getTrickStarter(), player1);
    }
    @Test
    void endRound(){
        GameLogic gameLogic = new GameLogic();
        Trick trick = new Trick();
        GameTable gameTable = gameLogic.getGameTable();

        Player player1 = new Player();
        player1.setId(1L);
        player1.setLobby(123456L);
        player1.setUsername("username1");
        player1.setBid(1);
        player1.setHasTurn(true);
        player1.setTricks(0);
        Player player2 = new Player();
        player2.setId(2L);
        player2.setLobby(123456L);
        player2.setUsername("username2");
        player2.setBid(1);
        player2.setTricks(0);

        ArrayList<Card> player1Hand = new ArrayList<>();
        oneRed.setPlayable(true);
        player1Hand.add(oneRed);
        player1.setHand(player1Hand);
        ArrayList<Card> player2Hand = new ArrayList<>();
        twoRed.setPlayable(true);
        player2Hand.add(twoRed);
        player2.setHand(player2Hand);

        trick.addPlayedCards(oneRed);
        trick.addPlayedCards(pirate);
        trick.addPlayedCards(skullking);
        trick.addPlayedCards(mermaid);
        trick.setIsTrumpSet(true);
        trick.setTrumpColour(oneRed);
        gameLogic.setTrick(trick);
        ArrayList<Player> gameLogicPlayers = new ArrayList<>();
        ArrayList<Player> scoreboardPlayers = new ArrayList<>();
        scoreboardPlayers.add(player1);
        scoreboardPlayers.add(player2);
        gameLogic.setupScoreboard(scoreboardPlayers);
        gameLogic.setPlayers(gameLogicPlayers);
        gameLogic.addPlayers(player1);
        gameLogic.addPlayers(player2);
        gameTable.addPlayer(player1);
        gameTable.addPlayer(player2);
        gameTable.setTrickStarter(player2);
        gameLogic.setRound(1);
        gameLogic.setRoundToEndGame(1);
        gameLogic.endTrick();

        assertEquals(11, gameLogic.getRound());
        assertEquals(0, player1.getTricks());
        assertEquals(0, player2.getTricks());
        assertNull(player1.getBid());
        assertNull(player2.getBid());
        assertEquals(gameLogic.getScoreboard().getScoreboard().get(0).get(0).getCurPlayer(), player1.getUsername());
        assertEquals(1, gameLogic.getScoreboard().getScoreboard().get(0).get(0).getCurBid());
        assertEquals(70, gameLogic.getScoreboard().getScoreboard().get(0).get(0).getCurPoints());
        assertEquals(1, gameLogic.getScoreboard().getScoreboard().get(0).get(0).getCurTricks());
        assertEquals(gameLogic.getScoreboard().getScoreboard().get(1).get(0).getCurPlayer(), player2.getUsername());
        assertEquals(1, gameLogic.getScoreboard().getScoreboard().get(1).get(0).getCurBid());
        assertEquals(gameLogic.getScoreboard().getScoreboard().get(1).get(0).getCurPoints(), -10);
        assertEquals(0, gameLogic.getScoreboard().getScoreboard().get(1).get(0).getCurTricks());
    }
}
