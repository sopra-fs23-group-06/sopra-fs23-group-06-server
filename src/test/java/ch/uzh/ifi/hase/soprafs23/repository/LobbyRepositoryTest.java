package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LobbyRepositoryTest {

    @Qualifier("lobbyRepository")
    @Autowired
    private LobbyRepository testLobbyRepository;

    @BeforeEach
    public void setup() { testLobbyRepository.deleteAll(); }

    @Test
    public void findByLobbyCode_success() {
        // create user list
        Player user = new Player();
        user.setId(1L);
        user.setLobby(1L);
        user.setUsername("username");
        ArrayList<Player> list = new ArrayList<Player>();
        list.add(user);
        // create Lobby
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setLobbyCode(123456L);
        lobby.setPlayers(list);

        // save the Lobby entity using the LobbyRepository
        testLobbyRepository.save(lobby);
        testLobbyRepository.flush();

        // when
        Lobby found = testLobbyRepository.findByLobbyCode(lobby.getLobbyCode());

        // then
        assertNotNull(found.getId());
        assertEquals(found.getId(), lobby.getId());
        assertEquals(found.getLobbyCode(), lobby.getLobbyCode());
        assertTrue(compareArrayLists(found.getPlayers(), lobby.getPlayers()));
    }

    @Test
    public void findByLobbyCode_LobbyNotFound() {
        // create user list
        Player user = new Player();
        user.setId(1L);
        user.setLobby(1L);
        user.setUsername("username");
        ArrayList<Player> list = new ArrayList<Player>();
        list.add(user);
        // create Lobby
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setLobbyCode(123456L);
        lobby.setPlayers(list);

        // save the Lobby entity using the LobbyRepository
        testLobbyRepository.save(lobby);
        testLobbyRepository.flush();

        // check that the Lobby is null
        assertNull(testLobbyRepository.findByLobbyCode(954321L));
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
            Field[] fields = Player.class.getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object value1 = field.get(player1);
                    Object value2 = field.get(player2);
                    if (!Objects.equals(value1, value2)) {
                        return false;
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }

}
