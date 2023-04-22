package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerPostDTO;
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
        PlayerPostDTO playerPostDTO = new PlayerPostDTO();
        playerPostDTO.setId(1L);
        playerPostDTO.setLobby(1L);
        playerPostDTO.setUsername("username");

        // MAP -> Create user
        Player player = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);

        // check content
        assertEquals(playerPostDTO.getId(), player.getId());
        assertEquals(playerPostDTO.getLobby(), player.getLobby());
        assertEquals(playerPostDTO.getUsername(), player.getUsername());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        Player player = new Player();
        player.setId(1L);
        player.setLobby(1L);
        player.setUsername("username");

        // MAP -> Create UserGetDTO
        PlayerGetDTO playerGetDTO = DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player);

        // check content
        assertEquals(player.getId(), playerGetDTO.getId());
        assertEquals(player.getUsername(), playerGetDTO.getUsername());
        assertEquals(player.getLobby(), playerGetDTO.getLobby());
    }
    @Test
    public void testCreateLobby_fromLobbyPostDTO_toLobby_success() {
        // create user list
        Player player = new Player();
        player.setId(1L);
        player.setLobby(1L);
        player.setUsername("username");
        ArrayList<Player> list = new ArrayList<Player>();
        list.add(player);
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
        Player player = new Player();
        player.setId(1L);
        player.setLobby(1L);
        player.setUsername("username");
        ArrayList<Player> list = new ArrayList<Player>();
        list.add(player);
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
