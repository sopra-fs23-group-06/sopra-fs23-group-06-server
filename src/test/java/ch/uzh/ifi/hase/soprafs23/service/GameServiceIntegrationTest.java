package ch.uzh.ifi.hase.soprafs23.service;

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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the GameService and LobbyRepository
 *
 * @see GameService
 */
@WebAppConfiguration
@SpringBootTest
public class GameServiceIntegrationTest {

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
    public void recordBid_validInput_BidRecorded() {
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
        ArrayList<Player> list = new ArrayList<Player>();
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
    public void startGame_validInput_GameStarted() {
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
    public void startGame_invalidLobbyCode_ExceptionThrown() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // then
        assertThrows(ResponseStatusException.class, () -> gameService.startGame(123456L));
    }

    //try to get the round of an invalid lobby. Check that an exception is thrown
    @Test
    public void getRound_invalidLobbyCode_ExceptionThrown() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // then
        assertThrows(ResponseStatusException.class, () -> gameService.getRound(123456L));
    }

    //Populate a lobby with 1 player. Check that the round is 0
    @Test
    public void getRound_validInput_RoundIsZero() {
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

//    @Test
//    public void getPlayerHand_returnsPlayerHand() {
//        // given
//        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());
//
//        // when
//        Lobby createdLobby = lobbyService.createLobby();
//
//        // generate a deck
//        Deck deck = new Deck();
//        deck.fillDeck();
//        Player player = new Player();
//        player.setLobby(createdLobby.getLobbyCode());
//        player.setUsername("username");
//        player.setId(1L);
//        ArrayList<Card> playerHand = new ArrayList<Card>();
//        playerHand.add(deck.draw());
//        player.setHand(playerHand);
//        ArrayList<Player> list = new ArrayList<Player>();
//        list.add(player);
//        createdLobby.setPlayers(list);
//        lobbyRepository.flush();
//        createdLobby.getGameLogic().setPlayers(createdLobby.getPlayers());
//        ArrayList<Player> rhjvb = createdLobby.getPlayers();
//        ArrayList<Player> a = lobbyRepository.findByLobbyCode(createdLobby.getLobbyCode()).getPlayers();
//
//        // call getPlayerHand method for player1
//        ArrayList<Card> player1Hand = gameService.getPlayerHand(1L, createdLobby.getLobbyCode());
//
//        // assert that the method returns the correct hand
//        assertEquals(createdLobby.getPlayers().get(0).getHand().size(), player1Hand.size());
//        assertEquals(Suit.HEARTS, player1Hand.get(0).getSuit());
//        assertEquals(Value.ACE, player1Hand.get(0).getValue());
//        assertEquals(Suit.CLUBS, player1Hand.get(1).getSuit());
//        assertEquals(Value.KING, player1Hand.get(1).getValue());
//    }

    /**
     * Helper method to compare the contents of two ArrayLists of Players
     */
    private boolean compareArrayLists(ArrayList<Player> list1, ArrayList<Player> list2) {
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
