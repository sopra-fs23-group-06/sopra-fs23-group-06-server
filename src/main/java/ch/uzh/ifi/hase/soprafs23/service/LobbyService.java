package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LobbyService {

  private final Logger log = LoggerFactory.getLogger(LobbyService.class);

  private final LobbyRepository lobbyRepository;


  @Autowired
  public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository)
                         {
    this.lobbyRepository = lobbyRepository;

  }



  public List<Lobby> getLobbies() {
    return this.lobbyRepository.findAll();
  }

  public Lobby createLobby() {
    Lobby newLobby =new Lobby();
    double code =  (Math.floor(100000 + Math.random() * 900000));
    newLobby.setLobbyCode((long) code);
    ArrayList<User> players = new ArrayList<>();
    newLobby.setPlayers(players);
    newLobby = lobbyRepository.save(newLobby);
    lobbyRepository.flush();

    log.debug("Created new Lobby: {}", newLobby);
    return newLobby;
  }

    public Lobby joinLobby(Long lobbyCode) {
      if(checkIfLobbyExists(lobbyCode)){
          return lobbyRepository.findByLobbyCode(lobbyCode);
      }
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist.");
    }

    public User addToLobby(Long lobbyCode, User userToAdd) {
      Lobby lobbyByLobbyCode = lobbyRepository.findByLobbyCode(lobbyCode);
      if (!checkIfUserNotTaken(lobbyCode,userToAdd.getUsername())){
          throw new ResponseStatusException(HttpStatus.CONFLICT, "username already taken");
      }
      userToAdd.setLobby(lobbyCode);
      userToAdd.setId((long) lobbyByLobbyCode.getPlayers().size()+1);
      lobbyByLobbyCode.addPlayers(userToAdd);
      return userToAdd;
    }


    private boolean checkIfUserNotTaken(Long lobbyCode, String username) {
        Lobby lobbyByLobbyCode = lobbyRepository.findByLobbyCode(lobbyCode);
        ArrayList<User> playerList = lobbyByLobbyCode.getPlayers();
        if (playerList == null){return true;}
        else {
            for (User user : playerList) {
            return !user.getUsername().equals(username);
            }
        }
        return true;
    }

    private boolean checkIfLobbyExists(Long lobbyCode) {
      Lobby lobbyByLobbyCode = lobbyRepository.findByLobbyCode(lobbyCode);
              return lobbyByLobbyCode != null;
    }

    public List<User> getUsers(Long lobbyCode) {
        Lobby lobbyByLobbyCode = lobbyRepository.findByLobbyCode(lobbyCode);
        return lobbyByLobbyCode.getPlayers();
    }


    /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws ResponseStatusException
   * @see User
   */

}
