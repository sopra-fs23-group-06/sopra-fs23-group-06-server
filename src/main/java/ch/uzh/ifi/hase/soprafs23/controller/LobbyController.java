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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
  private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
  LobbyController(LobbyService lobbyService) {this.lobbyService = lobbyService;}

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO addUserToLobby(@RequestBody UserPostDTO userPostDTO) {
        // create user
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        User addedUser = lobbyService.addToLobby(userInput);
        // convert internal representation of user back to API
        update(addedUser.getLobby());
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(addedUser);
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
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }


    @PutMapping("/lobbies/{lobbyCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO joinLobby(@PathVariable Long lobbyCode) {
        // create user
        Lobby updatedLobby = lobbyService.joinLobby(lobbyCode);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(updatedLobby);
    }


    @GetMapping("/lobbies/{lobbyCode}/users")
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
    }// maybe change to GET lobbies/lobbyId


    @PutMapping("/lobbies/{lobbyCode}/leaveHandler")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void leaveUser(@RequestBody UserPostDTO userPostDTO) {
        User leavingUser = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        lobbyService.removeUser(leavingUser);
        update(leavingUser.getLobby());
    }


    @PutMapping("/lobbies/{lobbyCode}/kickHandler")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void removeUser(@RequestBody UserPostDTO userPostDTO) {
        User leavingUser = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        lobbyService.removeUser(leavingUser);
        update(leavingUser.getLobby());
    }

    @PutMapping("/lobbies/{lobbyCode}/closeHandler")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void closeLobby(@PathVariable Long lobbyCode) {
        lobbyService.closeLobby(lobbyCode);
        close(lobbyCode);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/updates")
    public SseEmitter sendUpdate() {
        SseEmitter emitter = new SseEmitter(-1L);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> {emitters.remove(emitter);});
        return emitter;
    }


    public void update(Long lobbyCode) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().data("update:"+ lobbyCode));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }

    public void close(Long lobbyCode) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().data("close:"+ lobbyCode));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }



}
