package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

  LobbyController(LobbyService lobbyService) {
    this.lobbyService = lobbyService;
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

    @PutMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO joinLobby() {
        // create user
        Lobby createdLobby = lobbyService.createLobby();
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }


    @PutMapping("/lobbies/{lobbyCode}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO joinLobby(@PathVariable Long lobbyCode) {
        // create user
        Lobby updatedLobby = lobbyService.joinLobby(lobbyCode);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(updatedLobby);
    }

    @PostMapping("users/{lobbyCode}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO addUserToLobby(@PathVariable Long lobbyCode,@RequestBody UserPostDTO userPostDTO) {
        // create user
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        User addedUser = lobbyService.addToLobby(lobbyCode, userInput);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(addedUser);
    }

    @GetMapping("users/{lobbyCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers(@PathVariable Long lobbyCode) {
        // fetch all users in the internal representation
        List<User> users = lobbyService.getUsers(lobbyCode);
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

}
