package ch.uzh.ifi.hase.soprafs23.service;

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
 * Test class for the LobbyService and LobbyRepository
 *
 * @see LobbyService
 */
@WebAppConfiguration
@SpringBootTest
public class LobbyServiceIntegrationTest {

    @Qualifier("lobbyRepository")
    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private LobbyService lobbyService;

    @BeforeEach
    public void setup() {
        lobbyRepository.deleteAll();
    }

    @Test
    public void createLobby_validInput_LobbyCreated() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // when
        Lobby createdLobby = lobbyService.createLobby();

        // then
        assertNotNull(createdLobby);
        assertEquals(createdLobby.getId(), lobbyRepository.findByLobbyCode(createdLobby.getLobbyCode()).getId());
        assertEquals(createdLobby.getLobbyCode(), lobbyRepository.findByLobbyCode(createdLobby.getLobbyCode()).getLobbyCode());
        assertNotNull(createdLobby.getGameLogic());
        assertEquals(Collections.EMPTY_LIST, createdLobby.getPlayers());
    }

    @Test
    public void getLobbyByLobbyCode_validInput_LobbyFound() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // when
        Lobby createdLobby = lobbyService.createLobby();

        // then
        assertNotNull(lobbyService.checkLobby(createdLobby.getLobbyCode()));
        assertEquals(lobbyService.checkLobby(createdLobby.getLobbyCode()).getId(), createdLobby.getId());
        assertEquals(lobbyService.checkLobby(createdLobby.getLobbyCode()).getLobbyCode(), createdLobby.getLobbyCode());
        assertNotNull(lobbyService.checkLobby(createdLobby.getLobbyCode()).getGameLogic());
        assertEquals(Collections.EMPTY_LIST, lobbyService.checkLobby(createdLobby.getLobbyCode()).getPlayers());
    }

    @Test
    public void getLobbyByLobbyCode_invalidInput_LobbyNotFound() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // then
        assertThrows(ResponseStatusException.class, () -> lobbyService.checkLobby(123456L));
    }

    @Test
    public void addPlayerToLobby_validInput_PlayerAdded() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        Lobby createdLobby = lobbyService.createLobby();

        // create user list
        Player user = new Player();
        user.setId(1L);
        user.setLobby(createdLobby.getLobbyCode());
        user.setUsername("username");
        ArrayList<Player> list = new ArrayList<Player>();
        list.add(user);

        // add player to lobby
        Player playerToAdd = lobbyService.addToLobby(user);

        // then
        assertNotEquals(Collections.EMPTY_LIST, lobbyService.checkLobby(createdLobby.getLobbyCode()).getPlayers());
        assertTrue(compareArrayLists(list, (ArrayList<Player>) lobbyService.getUsers(user.getLobby())));
        assertEquals(lobbyService.checkLobby(user.getLobby()).getLobbyCode(), user.getLobby());
    }

    @Test
    public void addPlayerToLobby_usernameTaken_PlayerNotAdded() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        Lobby createdLobby = lobbyService.createLobby();

        // create user list
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
        user2.setUsername("username");

        // then
        assertThrows(ResponseStatusException.class, () -> lobbyService.addToLobby(user2));
    }

    //create a Lobby with 6 players. Check that the Lobby is full and that no more players can be added
    @Test
    public void addPlayerToLobby_LobbyFull_PlayerNotAdded() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

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
        Player playerToAdd2 = lobbyService.addToLobby(user2);

        // create third user
        Player user3 = new Player();
        user3.setId(3L);
        user3.setLobby(createdLobby.getLobbyCode());
        user3.setUsername("username3");
        Player playerToAdd3 = lobbyService.addToLobby(user3);

        // create fourth user
        Player user4 = new Player();
        user4.setId(4L);
        user4.setLobby(createdLobby.getLobbyCode());
        user4.setUsername("username4");
        Player playerToAdd4 = lobbyService.addToLobby(user4);

        // create fifth user
        Player user5 = new Player();
        user5.setId(5L);
        user5.setLobby(createdLobby.getLobbyCode());
        user5.setUsername("username5");
        Player playerToAdd5 = lobbyService.addToLobby(user5);

        // create sixth user
        Player user6 = new Player();
        user6.setId(6L);
        user6.setLobby(createdLobby.getLobbyCode());
        user6.setUsername("username6");
        Player playerToAdd6 = lobbyService.addToLobby(user6);

        // create seventh user
        Player user7 = new Player();
        user7.setId(7L);
        user7.setLobby(createdLobby.getLobbyCode());
        user7.setUsername("username7");

        // then
        assertThrows(ResponseStatusException.class, () -> lobbyService.addToLobby(user7));

    }

    @Test
    public void joinLobbyByLobbyCode_validInput_LobbyFound() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // when
        Lobby createdLobby = lobbyService.createLobby();

        // then
        assertNotNull(lobbyService.joinLobby(createdLobby.getLobbyCode()));
        assertEquals(lobbyService.joinLobby(createdLobby.getLobbyCode()).getId(), createdLobby.getId());
        assertEquals(lobbyService.joinLobby(createdLobby.getLobbyCode()).getLobbyCode(), createdLobby.getLobbyCode());
        assertNotNull(lobbyService.joinLobby(createdLobby.getLobbyCode()).getGameLogic());
        assertEquals(Collections.EMPTY_LIST, lobbyService.joinLobby(createdLobby.getLobbyCode()).getPlayers());
    }

    @Test
    public void joinLobbyByLobbyCode_invalidInput_LobbyNotFound() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // then
        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobby(123456L));
    }

    //create Lobby, populate it with 2 players, use lobbyService.getUsers(createdLobby.getLobbyCode()) to get the list of players

    @Test
    public void getUsers_validInput_ListOfPlayersReturned() {
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
        Player playerToAdd2 = lobbyService.addToLobby(user2);

        // then
        assertTrue(compareArrayLists(list, (ArrayList<Player>) lobbyService.getUsers(createdLobby.getLobbyCode())));
    }

    //populate Lobby with 1 player, then remove that player. Check that the Lobby is empty
    @Test
    public void removePlayerFromLobby_validInput_PlayerRemoved() {
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
        assertTrue(compareArrayLists(list, (ArrayList<Player>) lobbyService.getUsers(createdLobby.getLobbyCode())));

        // when
        lobbyService.removeUser(user);

        // then
        assertEquals(Collections.EMPTY_LIST, lobbyService.getUsers(createdLobby.getLobbyCode()));
    }

    //populate lobby with 1 player, then close the lobby. Check that lobbyService.checkLobby(createdLobby.getLobbyCode()) throws an exception
    @Test
    public void closeLobby_validInput_LobbyClosed() {
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

        // check if lobbyRepository contains createdLobby
        assertEquals(lobbyRepository.findByLobbyCode(createdLobby.getLobbyCode()).getLobbyCode(), createdLobby.getLobbyCode());

        // when
        lobbyService.closeLobby(createdLobby.getLobbyCode());

        // then
        assertThrows(ResponseStatusException.class, () -> lobbyService.checkLobby(createdLobby.getLobbyCode()));
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
        assertEquals(lobbyService.recordBid(user, createdLobby.getLobbyCode()).getBid(), user.getBid());
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
        lobbyService.startGame(createdLobby.getLobbyCode());

        // then
        assertEquals(1, lobbyService.checkLobby(createdLobby.getLobbyCode()).getGameLogic().getRound());
    }

    //try to start a game with an invalid lobby code. Check that an exception is thrown
    @Test
    public void startGame_invalidLobbyCode_ExceptionThrown() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // then
        assertThrows(ResponseStatusException.class, () -> lobbyService.startGame(123456L));
    }

    //try to get the round of an invalid lobby. Check that an exception is thrown
    @Test
    public void getRound_invalidLobbyCode_ExceptionThrown() {
        // given
        assertEquals(Collections.EMPTY_LIST, lobbyRepository.findAll());

        // then
        assertThrows(ResponseStatusException.class, () -> lobbyService.getRound(123456L));
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
        assertEquals(0, lobbyService.getRound(createdLobby.getLobbyCode()));
    }

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