package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.game.GameLogic;
import ch.uzh.ifi.hase.soprafs23.game.GameTable;
import ch.uzh.ifi.hase.soprafs23.points.Scoreboard;
import ch.uzh.ifi.hase.soprafs23.points.Trick;
import ch.uzh.ifi.hase.soprafs23.constant.CardColor;
import ch.uzh.ifi.hase.soprafs23.constant.CardRank;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Deck;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the GameService and LobbyRepository
 *
 * @see GameService
 */
@WebAppConfiguration
@SpringBootTest
class GameServiceIntegrationTest {

    @Qualifier("lobbyRepository")
    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private LobbyService lobbyService;

    @BeforeEach
    public void setup() {
        lobbyRepository.deleteAll();
    }

    //record bid, check that the bid is recorded
    @Test
    void recordBid_validInput_BidRecorded() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // when
        Lobby createdLobby = lobbyService.createLobby();

        // create user
        Player user = new Player();
        user.setId(1L);
        user.setLobby(createdLobby.getLobbyCode());
        user.setUsername("username");
        user.setBid(0);
        ArrayList<Player> list = new ArrayList<>();
        list.add(user);

        // add player to lobby
        Player playerToAdd = lobbyService.addToLobby(user);

        // check if lobbyRepository contains createdLobby
        assertEquals(lobbyRepository.findByLobbyCode(createdLobby.getLobbyCode()).getLobbyCode(), createdLobby.getLobbyCode());

        // then
        assertEquals(gameService.recordBid(user, createdLobby.getLobbyCode()).getBid(), user.getBid());
        assertEquals(user.getBid(), lobbyService.getUsers(createdLobby.getLobbyCode()).get(0).getBid());
        assertEquals(lobbyRepository.findByLobbyCode(createdLobby.getLobbyCode()).getPlayers().get(0).getBid(), user.getBid());
    }

    //populate lobby with 2 players, then start the game. Check that the round is 1
    @Test
    void startGame_validInput_GameStarted() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // when
        Lobby createdLobby = lobbyService.createLobby();

        // create first user
        Player user = new Player();
        user.setId(1L);
        user.setLobby(createdLobby.getLobbyCode());
        user.setUsername("username");
        ArrayList<Player> list = new ArrayList<>();
        list.add(user);

        // add player to lobby
        Player playerToAdd = lobbyService.addToLobby(user);

        // create second user
        Player user2 = new Player();
        user2.setId(2L);
        user2.setLobby(createdLobby.getLobbyCode());
        user2.setUsername("username2");
        list.add(user2);

        // add player to lobby
        Player playerToAdd2 = lobbyService.addToLobby(user2);

        // then
        assertTrue(compareArrayLists(list, (ArrayList<Player>) lobbyService.getUsers(createdLobby.getLobbyCode())));

        // when
        gameService.startGame(createdLobby.getLobbyCode());

        // then
        assertEquals(1, lobbyService.checkLobby(createdLobby.getLobbyCode()).getGameLogic().getRound());
    }

    //try to start a game with an invalid lobby code. Check that an exception is thrown
    @Test
    void startGame_invalidLobbyCode_ExceptionThrown() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // then
        assertThrows(ResponseStatusException.class, () -> gameService.startGame(123456L));
    }

    //try to get the round of an invalid lobby. Check that an exception is thrown
    @Test
    void getRound_invalidLobbyCode_ExceptionThrown() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // then
        assertThrows(ResponseStatusException.class, () -> gameService.getRound(123456L));
    }

    //Populate a lobby with 1 player. Check that the round is 0
    @Test
    void getRound_validInput_RoundIsZero() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // when
        Lobby createdLobby = lobbyService.createLobby();

        // create first user
        Player user = new Player();
        user.setId(1L);
        user.setLobby(createdLobby.getLobbyCode());
        user.setUsername("username");
        ArrayList<Player> list = new ArrayList<Player>();
        list.add(user);

        // add player to lobby
        Player playerToAdd = lobbyService.addToLobby(user);

        // then
        assertEquals(0, gameService.getRound(createdLobby.getLobbyCode()));
    }

    @Test
    void getPlayerHand_validInput_returnsPlayerHand() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // when
        Lobby createdLobby = lobbyService.createLobby();

        // generate a deck
        Deck deck = new Deck();
        deck.fillDeck();
        Player player = new Player();
        player.setLobby(createdLobby.getLobbyCode());
        player.setUsername("username");
        player.setId(1L);
        ArrayList<Card> playerHand = new ArrayList<>();
        playerHand.add(deck.draw());
        player.setHand(playerHand);
        //ArrayList<Player> list = new ArrayList<Player>();
        //list.add(player);
        lobbyService.addToLobby(player);
        createdLobby = lobbyRepository.findByLobbyCode(createdLobby.getLobbyCode());


        // call getPlayerHand method for player1
        ArrayList<Card> player1Hand = gameService.getPlayerHand(1L, createdLobby.getLobbyCode());

        // assert that the method returns the correct hand
        assertNotNull(player1Hand);
        assertEquals(createdLobby.getPlayers().get(0).getHand().size(), player1Hand.size());
        assertEquals(createdLobby.getPlayers().get(0).getHand().get(0).getaRank(), player1Hand.get(0).getaRank());
        assertEquals(createdLobby.getPlayers().get(0).getHand().get(0).getColor(), player1Hand.get(0).getColor());
        assertEquals(createdLobby.getPlayers().get(0).getHand().get(0).getPlayable(), player1Hand.get(0).getPlayable());
    }
    @Test
    void getPlayerHand_invalidLobbyCode_throwsException() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // when
        Lobby createdLobby = lobbyService.createLobby();

        // generate a deck
        Deck deck = new Deck();
        deck.fillDeck();
        Player player = new Player();
        player.setLobby(createdLobby.getLobbyCode());
        player.setUsername("username");
        player.setId(1L);
        ArrayList<Card> playerHand = new ArrayList<>();
        playerHand.add(deck.draw());
        player.setHand(playerHand);
        lobbyService.addToLobby(player);

        // then
        Long lobbyCode = createdLobby.getLobbyCode() - 1L;
        assertThrows(ResponseStatusException.class, () -> {
            gameService.getPlayerHand(1L, lobbyCode);
        });
    }


    @Test
    void getPlayerHand_playerDoesNotExist_returnsNull() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // when
        Lobby createdLobby = lobbyService.createLobby();

        // generate a deck
        Deck deck = new Deck();
        deck.fillDeck();
        Player player = new Player();
        player.setLobby(createdLobby.getLobbyCode());
        player.setUsername("username");
        player.setId(1L);
        ArrayList<Card> playerHand = new ArrayList<>();
        playerHand.add(deck.draw());
        player.setHand(playerHand);
        lobbyService.addToLobby(player);

        ArrayList<Card> player1Hand = gameService.getPlayerHand(2L, createdLobby.getLobbyCode());


        // assert that the method returns null
        assertNull(player1Hand);
    }
@Test
void playCard_validInput_cardPlayed() {
    // given
    assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

    // when
    Lobby createdLobby = lobbyService.createLobby();

    // generate a deck
    Deck deck = new Deck();
    deck.fillDeck();

    // create first player
    Player player1 = new Player();
    player1.setLobby(createdLobby.getLobbyCode());
    player1.setUsername("username");
    player1.setId(1L);
    ArrayList<Card> player1Hand = new ArrayList<>();
    while (true) {
        Card card = deck.draw();
        if (card.getaRank() == CardRank.SEVEN && card.getColor() == CardColor.RED) {
            player1Hand.add(card);
            break;
        }
    }
    player1.setHand(player1Hand);
    player1.setHasTurn(true);
    lobbyService.addToLobby(player1);

    // create second player
    deck.fillDeck();
    Player player2 = new Player();
    player2.setId(2L);
    player2.setLobby(createdLobby.getLobbyCode());
    player2.setUsername("username2");
    ArrayList<Card> player2Hand = new ArrayList<>();
    while (true) {
        Card card2 = deck.draw();
        if (card2.getaRank() == CardRank.SIX && card2.getColor() == CardColor.RED) {
            player2Hand.add(card2);
            break;
        }
    }
    player2.setHand(player2Hand);
    lobbyService.addToLobby(player2);

    // get the lobby again to ensure we have the latest version
    createdLobby = lobbyRepository.findByLobbyCode(createdLobby.getLobbyCode());

    // add both players to the game table and set round starter
    GameTable gameTable = createdLobby.getGameTable();
    gameTable.addPlayer(player1);
    gameTable.addPlayer(player2);
    gameTable.setRoundStarter(player2);

    // set up the game logic
    GameLogic gameLogic = createdLobby.getGameLogic();
    gameLogic.nextRound();
    gameLogic.setupScoreboard(createdLobby.getPlayers());
    gameLogic.getPlayers().get(0).setHand(player1Hand);
    gameLogic.getPlayers().get(1).setHand(player2Hand);
    lobbyRepository.save(createdLobby);

    // play the card from player 1's hand
    Card cardToPlay = player1Hand.get(0);
    gameService.playCard(player1.getId(), player1.getLobby(), cardToPlay.getaRank().toString(), cardToPlay.getColor().toString(), cardToPlay.getaOption().toString());
    gameService.afterPlayCard(player1.getId(), player1.getLobby());

    // Load the lobby object from the repository
    Lobby lobby = lobbyRepository.findByLobbyCode(createdLobby.getLobbyCode());

    // Assert that player1's hand is an empty list
    assertEquals(0, lobby.getPlayers().get(0).getHand().size());

    // Assert that player1's hasTurn boolean is set to false
    assertFalse(lobby.getPlayers().get(0).isHasTurn());

    // Assert that the array playedCards in the "Trick" object contains the card played by player1
    Trick trick = lobby.getGameLogic().getTrick();
    assertEquals((trick.getPlayedCards().get(0).getaRank()), player1Hand.get(0).getaRank());
    assertEquals((trick.getPlayedCards().get(0).getColor()), player1Hand.get(0).getColor());
    assertEquals((trick.getPlayedCards().get(0).getaOption()), player1Hand.get(0).getaOption());

    // Assert that trick.getIsTrumpSet returns true
    assertTrue(trick.getIsTrumpSet());

    // Assert that trick.getTrumpColour returns CardColor.RED
    assertEquals(CardColor.RED, trick.getTrumpColour());

    // Assert that player2.isHasTurn returns true
    assertTrue(lobby.getPlayers().get(1).isHasTurn());

}

    @Test
    void getOrder_validInput_returnOrder() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // when
        Lobby createdLobby = lobbyService.createLobby();

        // generate a deck
        Deck deck = new Deck();
        deck.fillDeck();

        // create first player
        Player player1 = new Player();
        player1.setLobby(createdLobby.getLobbyCode());
        player1.setUsername("username");
        player1.setId(1L);
        ArrayList<Card> player1Hand = new ArrayList<>();
        while (true) {
            Card card = deck.draw();
            if (card.getaRank() == CardRank.SEVEN && card.getColor() == CardColor.RED) {
                player1Hand.add(card);
                break;
            }
        }
        player1.setHand(player1Hand);
        player1.setHasTurn(true);
        lobbyService.addToLobby(player1);

        // create second player
        deck.fillDeck();
        Player player2 = new Player();
        player2.setId(2L);
        player2.setLobby(createdLobby.getLobbyCode());
        player2.setUsername("username2");
        ArrayList<Card> player2Hand = new ArrayList<>();
        while (true) {
            Card card2 = deck.draw();
            if (card2.getaRank() == CardRank.SIX && card2.getColor() == CardColor.RED) {
                player2Hand.add(card2);
                break;
            }
        }
        player2.setHand(player2Hand);
        lobbyService.addToLobby(player2);

        // get the lobby again to ensure we have the latest version
        createdLobby = lobbyRepository.findByLobbyCode(createdLobby.getLobbyCode());

        // add both players to the game table and set round starter
        GameTable gameTable = createdLobby.getGameTable();
        gameTable.addPlayer(player1);
        gameTable.addPlayer(player2);
        gameTable.setRoundStarter(player2);

        // set up the game logic
        GameLogic gameLogic = createdLobby.getGameLogic();
        gameLogic.nextRound();
        gameLogic.setupScoreboard(createdLobby.getPlayers());
        gameLogic.getPlayers().get(0).setHand(player1Hand);
        gameLogic.getPlayers().get(1).setHand(player2Hand);
        Lobby lobby = lobbyRepository.save(createdLobby);

        // get order
        List<Player> order = gameService.getOrder(createdLobby.getLobbyCode());

        assertNotNull(order);

        // assert that the ID's are in the correct order
        assertEquals(player1.getId(), order.get(0).getId());
        assertEquals(player2.getId(), order.get(1).getId());

        // Assert that the lobbyCodes are in the correct order
        assertEquals(player1.getLobby(), order.get(0).getLobby());
        assertEquals(player2.getLobby(), order.get(1).getLobby());

        // assert that the usernames are in the correct order
        assertEquals(player1.getUsername(), order.get(0).getUsername());
        assertEquals(player2.getUsername(), order.get(1).getUsername());

    }
    @Test
    void getTableCards_validInput_returnTableCards() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // when
        Lobby createdLobby = lobbyService.createLobby();

        // generate a deck
        Deck deck = new Deck();
        deck.fillDeck();

        // create first player
        Player player1 = new Player();
        player1.setLobby(createdLobby.getLobbyCode());
        player1.setUsername("username");
        player1.setId(1L);
        ArrayList<Card> player1Hand = new ArrayList<>();
        while (true) {
            Card card = deck.draw();
            if (card.getaRank() == CardRank.SEVEN && card.getColor() == CardColor.RED) {
                player1Hand.add(card);
                break;
            }
        }
        player1.setHand(player1Hand);
        player1.setHasTurn(true);
        lobbyService.addToLobby(player1);

        // create second player
        deck.fillDeck();
        Player player2 = new Player();
        player2.setId(2L);
        player2.setLobby(createdLobby.getLobbyCode());
        player2.setUsername("username2");
        ArrayList<Card> player2Hand = new ArrayList<>();
        while (true) {
            Card card2 = deck.draw();
            if (card2.getaRank() == CardRank.SIX && card2.getColor() == CardColor.RED) {
                player2Hand.add(card2);
                break;
            }
        }
        player2.setHand(player2Hand);
        lobbyService.addToLobby(player2);

        // get the lobby again to ensure we have the latest version
        createdLobby = lobbyRepository.findByLobbyCode(createdLobby.getLobbyCode());

        // add both players to the game table and set round starter
        GameTable gameTable = createdLobby.getGameTable();
        gameTable.addPlayer(player1);
        gameTable.addPlayer(player2);
        gameTable.setRoundStarter(player2);

        // set up the game logic
        GameLogic gameLogic = createdLobby.getGameLogic();
        gameLogic.nextRound();
        gameLogic.setupScoreboard(createdLobby.getPlayers());
        gameLogic.getPlayers().get(0).setHand(player1Hand);
        gameLogic.getPlayers().get(1).setHand(player2Hand);
        Lobby lobby = lobbyRepository.save(createdLobby);


        // play the card from player 1's hand
        Card cardToPlay = player1Hand.get(0);
        gameService.playCard(player1.getId(), player1.getLobby(), cardToPlay.getaRank().toString(), cardToPlay.getColor().toString(), cardToPlay.getaOption().toString());

        // get cards on table
        ArrayList<Card> cards = gameService.getTableCards(createdLobby.getLobbyCode());

        assertNotNull(cards);
        assertEquals(player1Hand.size(), cards.size());
        assertEquals(player1Hand.get(0).getaRank(), cards.get(0).getaRank());
        assertEquals(player1Hand.get(0).getColor(), cards.get(0).getColor());
        assertEquals(player1Hand.get(0).getaOption(), cards.get(0).getaOption());

    }

    @Test
    void getScoreboard_validInput_returnScoreboard() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // when
        Lobby createdLobby = lobbyService.createLobby();

        // generate a deck
        Deck deck = new Deck();
        deck.fillDeck();

        // create first player
        Player player1 = new Player();
        player1.setLobby(createdLobby.getLobbyCode());
        player1.setUsername("username");
        player1.setId(1L);
        ArrayList<Card> player1Hand = new ArrayList<>();
        while (true) {
            Card card = deck.draw();
            if (card.getaRank() == CardRank.SEVEN && card.getColor() == CardColor.RED) {
                player1Hand.add(card);
                break;
            }
        }
        player1.setHand(player1Hand);
        player1.setHasTurn(true);
        lobbyService.addToLobby(player1);

        // create second player
        deck.fillDeck();
        Player player2 = new Player();
        player2.setId(2L);
        player2.setLobby(createdLobby.getLobbyCode());
        player2.setUsername("username2");
        ArrayList<Card> player2Hand = new ArrayList<>();
        while (true) {
            Card card2 = deck.draw();
            if (card2.getaRank() == CardRank.SIX && card2.getColor() == CardColor.RED) {
                player2Hand.add(card2);
                break;
            }
        }
        player2.setHand(player2Hand);
        lobbyService.addToLobby(player2);

        // get the lobby again to ensure we have the latest version
        createdLobby = lobbyRepository.findByLobbyCode(createdLobby.getLobbyCode());

        // add both players to the game table and set round starter
        GameTable gameTable = createdLobby.getGameTable();
        gameTable.addPlayer(player1);
        gameTable.addPlayer(player2);
        gameTable.setRoundStarter(player2);

        // set up the game logic
        GameLogic gameLogic = createdLobby.getGameLogic();
        gameLogic.nextRound();
        gameLogic.setupScoreboard(createdLobby.getPlayers());
        gameLogic.getPlayers().get(0).setHand(player1Hand);
        gameLogic.getPlayers().get(1).setHand(player2Hand);
        lobbyRepository.save(createdLobby);

        // get scoreboard
        Scoreboard scoreboard = gameService.getScoreboard(createdLobby.getLobbyCode());

        assertNotNull(scoreboard);
        assertEquals(scoreboard.getScoreboard().get(0).get(0).getCurPlayer(), player1.getUsername());
        assertEquals(1, scoreboard.getScoreboard().get(0).get(0).getCurRound());
        assertNull(scoreboard.getScoreboard().get(0).get(0).getCurBid());
        assertNull(scoreboard.getScoreboard().get(0).get(0).getCurPoints());

        assertEquals(scoreboard.getScoreboard().get(1).get(0).getCurPlayer(), player2.getUsername());
        assertEquals(1, scoreboard.getScoreboard().get(1).get(0).getCurRound());
        assertNull(scoreboard.getScoreboard().get(1).get(0).getCurBid());
        assertNull(scoreboard.getScoreboard().get(1).get(0).getCurPoints());

    }


    /**
     * Helper method to compare the contents of two ArrayLists of Players
     */
    private boolean compareArrayLists(List<Player> list1, List<Player> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list1.size(); i++) {
            Player player1 = list1.get(i);
            Player player2 = list2.get(i);
            if (!Objects.equals(player1.getId(), player2.getId())
                    || !Objects.equals(player1.getUsername(), player2.getUsername())
                    || !Objects.equals(player1.getLobby(), player2.getLobby())) {
                return false;
            }
        }
        return true;
    }
}
