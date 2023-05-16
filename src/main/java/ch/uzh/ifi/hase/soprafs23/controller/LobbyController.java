package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class LobbyController {

  private final LobbyService lobbyService;
  LobbyController(LobbyService lobbyService) {this.lobbyService = lobbyService;}

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PlayerGetDTO addUserToLobby(@RequestBody PlayerPostDTO playerPostDTO) {
        // create user
        Player playerInput = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);

        Player addedPlayer = lobbyService.addToLobby(playerInput);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(addedPlayer);
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby() {
        // create user
        Lobby createdLobby = lobbyService.createLobby();
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @GetMapping("/lobbies/{lobbyCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO checkLobby(@PathVariable Long lobbyCode) {
        // create user
        Lobby createdLobby = lobbyService.checkLobby(lobbyCode);
        // convert internal representation of lobby back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }


    @PutMapping("/lobbies/{lobbyCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO joinLobby(@PathVariable Long lobbyCode) {
        // create user
        Lobby updatedLobby = lobbyService.joinLobby(lobbyCode);
        // convert internal representation of lobby back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(updatedLobby);
    }

    @PostMapping("/games/{lobbyCode}/gameSettings")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void GameSettings(@RequestBody PlayerPostDTO playerPostDTO,@RequestParam int roundToEndGame, @RequestParam int maxPlayerSize, @PathVariable Long lobbyCode){
        // convert user
        Player playerInput = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);
        if (playerInput.getId() != 1L) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only the host can change the game settings");
        }
        if (roundToEndGame > 10){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The maximum of Rounds is 10 ");
        }
        if (maxPlayerSize <2){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The minimum of Players are 2");
        }
        lobbyService.gameSettings(lobbyCode,roundToEndGame,maxPlayerSize);
    }


    @GetMapping("/lobbies/{lobbyCode}/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PlayerGetDTO> getAllUsers(@PathVariable Long lobbyCode) {
        // fetch all users in the internal representation
        List<Player> players = lobbyService.getUsers(lobbyCode);
        List<PlayerGetDTO> playerGetDTOS = new ArrayList<>();

        // convert each user to the API representation
        for (Player player : players) {
            playerGetDTOS.add(DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player));
        }
        return playerGetDTOS;
    }// maybe change to GET lobbies/lobbyId


    @PutMapping("/lobbies/{lobbyCode}/leaveHandler")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void leaveUser(@RequestBody PlayerPostDTO playerPostDTO) {
        Player leavingPlayer = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);
        lobbyService.removeUser(leavingPlayer);
    }


    @PutMapping("/lobbies/{lobbyCode}/kickHandler")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void removeUser(@RequestBody PlayerPostDTO playerPostDTO, @RequestHeader("userId") Long userId) {
        Player leavingPlayer = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);
        if (userId != 1L) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only the host can kick other players");
        }
        lobbyService.removeUser(leavingPlayer);
    }

    @PutMapping("/lobbies/{lobbyCode}/closeHandler")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void closeLobby(@PathVariable Long lobbyCode, @RequestHeader("userId") Long userId) {
        if (userId != 1L) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only the host can close the lobby");
        }
        lobbyService.closeLobby(lobbyCode);
    }

    //GameController below, could be moved to own class at some point (lobby instance!)







}
