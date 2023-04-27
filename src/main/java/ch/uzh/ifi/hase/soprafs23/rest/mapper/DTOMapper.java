package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerPostDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "lobby", target = "lobby")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "bid", target = "bid")
    @Mapping(source = "tricks", target = "tricks")
    Player convertPlayerPostDTOtoEntity(PlayerPostDTO playerPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "lobby", target = "lobby")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "bid", target = "bid")
    @Mapping(source = "tricks", target = "tricks")
    PlayerGetDTO convertEntityToPlayerGetDTO(Player player);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "lobbyCode", target = "lobbyCode")
    Lobby convertLobbyPostDTOtoEntity(LobbyPostDTO lobbyPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "lobbyCode", target = "lobbyCode")
    LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);

}
