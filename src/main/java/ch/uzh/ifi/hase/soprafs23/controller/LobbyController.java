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
    public void removeUser(@RequestBody PlayerPostDTO playerPostDTO) {
        Player leavingPlayer = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);
        lobbyService.removeUser(leavingPlayer);
    }

    @PutMapping("/lobbies/{lobbyCode}/closeHandler")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void closeLobby(@PathVariable Long lobbyCode) {
        lobbyService.closeLobby(lobbyCode);
    }

    //GameController below, could be moved to own class at some point (lobby instance!)

    @PostMapping("/games/{lobbyCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void startGame(@PathVariable Long lobbyCode){
      lobbyService.startGame(lobbyCode);
    }

    @GetMapping("/games/{lobbyCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void /*LobbyGetDTO*/ getGame(@PathVariable Long lobbyCode){

        //return
    }


    @GetMapping("/games/{lobbyCode}/rounds")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public int getRound(@PathVariable Long lobbyCode){
        return lobbyService.getRound(lobbyCode);
    }

    @PutMapping("/games/{lobbyCode}/bidHandler")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerGetDTO makeBid(@RequestBody PlayerPostDTO playerPostDTO, @PathVariable Long lobbyCode) {
        // convert user
        Player playerInput = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);

        Player bidPlayer = lobbyService.recordBid(playerInput, lobbyCode);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(bidPlayer);
    }

}
