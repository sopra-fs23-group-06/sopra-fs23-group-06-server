package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperTest {
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setId(1L);
        userPostDTO.setLobby(1L);
        userPostDTO.setUsername("username");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getId(), user.getId());
        assertEquals(userPostDTO.getLobby(), user.getLobby());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        user.setId(1L);
        user.setLobby(1L);
        user.setUsername("username");

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getLobby(), userGetDTO.getLobby());
    }
    @Test
    public void testCreateLobby_fromLobbyPostDTO_toLobby_success() {
        // create user list
        User user = new User();
        user.setId(1L);
        user.setLobby(1L);
        user.setUsername("username");
        ArrayList<User> list = new ArrayList<User>();
        list.add(user);
        // create LobbyPostDTO
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setId(2L);
        lobbyPostDTO.setLobbyCode(123456L);
        lobbyPostDTO.setPlayers(list);

        // MAP -> Create lobby
        Lobby lobby = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        // check content
        assertEquals(lobbyPostDTO.getId(), lobby.getId());
        assertEquals(lobbyPostDTO.getLobbyCode(), lobby.getLobbyCode());
        assertEquals(lobbyPostDTO.getPlayers(), lobby.getPlayers());
    }
    @Test
    public void testGetLobby_fromLobby_toLobbyGetDTO_success() {
        // create user list
        User user = new User();
        user.setId(1L);
        user.setLobby(1L);
        user.setUsername("username");
        ArrayList<User> list = new ArrayList<User>();
        list.add(user);
        // create Lobby
        Lobby lobby = new Lobby();
        lobby.setId(2L);
        lobby.setLobbyCode(123456L);
        lobby.setPlayers(list);

        // MAP -> Create LobbyGetDTO
        LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);

        // check content
        assertEquals(lobby.getId(), lobbyGetDTO.getId());
        assertEquals(lobby.getLobbyCode(), lobbyGetDTO.getLobbyCode());
        assertEquals(lobby.getPlayers(), lobbyGetDTO.getPlayers());
    }
}
