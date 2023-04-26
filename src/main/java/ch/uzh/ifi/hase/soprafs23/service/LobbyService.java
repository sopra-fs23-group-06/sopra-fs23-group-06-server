package ch.uzh.ifi.hase.soprafs23.service;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
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
    Lobby newLobby = new Lobby();
    double code =  (Math.floor(100000 + Math.random() * 900000));
    newLobby.setLobbyCode((long) code);
    ArrayList<Player> players = new ArrayList<>();
    newLobby.setPlayers(players);
    newLobby = lobbyRepository.save(newLobby);
    lobbyRepository.flush();

    log.debug("Created new Lobby: {}", newLobby);
    return newLobby;
  }

    public Lobby checkLobby(Long lobbyCode) {
      if(checkIfLobbyExists(lobbyCode)){
          return lobbyRepository.findByLobbyCode(lobbyCode);
      }
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist.");
    }

    public Lobby joinLobby(Long lobbyCode){
        if(checkIfLobbyExists(lobbyCode)){
            if(checkIfLobbyFull(lobbyCode)){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Lobby is Full");
            }
            return lobbyRepository.findByLobbyCode(lobbyCode);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist.");
    }



    private boolean checkIfLobbyFull(Long lobbyCode) {
      Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode);
        return lobby.getPlayers().size() >= 6;
    }

    public Player addToLobby(Player playerToAdd) {
      Long lobbyCode = playerToAdd.getLobby();
      Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode);
      ArrayList<Player> playerList = lobby.getPlayers();
      if (!checkIfUserNotTaken(lobbyCode, playerToAdd.getUsername())){
          throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
      }
      if(checkIfLobbyFull(lobbyCode)){
          throw new ResponseStatusException(HttpStatus.CONFLICT, "Lobby is Full");
      }
      if (lobby.getPlayers().size() == 0){
          playerToAdd.setId((long)1);}
      else {
          playerToAdd.setId(playerList.get(playerList.size()-1).getId()+1);};
      lobby.addPlayers(playerToAdd);
      return playerToAdd;
    }


    private boolean checkIfUserNotTaken(Long lobbyCode, String username) {
        Lobby lobbyByLobbyCode = lobbyRepository.findByLobbyCode(lobbyCode);
        ArrayList<Player> playerList = lobbyByLobbyCode.getPlayers();
        if (playerList == null){return true;}
        else {
            for (Player player : playerList) {
            if(player.getUsername().equals(username)){
                return false;
            }
            }
        }
        return true;
    }

    private boolean checkIfLobbyExists(Long lobbyCode) {
      Lobby lobbyByLobbyCode = lobbyRepository.findByLobbyCode(lobbyCode);
              return lobbyByLobbyCode != null;
    }

    public List<Player> getUsers(Long lobbyCode) {
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode);
        return lobby.getPlayers();
    }

    public void removeUser(Player leavingPlayer) {
      Long lobbyCode = leavingPlayer.getLobby();
      Long userID = leavingPlayer.getId();
      if (checkIfLobbyExists(lobbyCode)){
      Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode);
      ArrayList<Player> playerList = lobby.getPlayers();
      for (int i = 0; i < playerList.size(); i++){
          if (playerList.get(i).getId().equals(userID)){
              playerList.remove(playerList.get(i));
          }
      }
      }
    }

    public void closeLobby(Long lobbyCode) {
        if (checkIfLobbyExists(lobbyCode)){
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode);
        ArrayList<Player> playerList = lobby.getPlayers();
        playerList.clear();
        lobbyRepository.delete(lobby);}
  }


  //game functions, could be moved to a GameService (lobbyRepository instance!)


    public Player recordBid(Player playerInput, Long lobbyCode) {
        if (!checkIfLobbyExists(lobbyCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist.");
        }
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode);
        if (playerInput.getBid() < 0 || playerInput.getBid() > lobby.getRound()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bid does not match round.");
        }
        ArrayList<Player> playerList = lobby.getPlayers();
        Player player = null;
        for (Player value : playerList) {
            if (value.getId().equals(playerInput.getId())) {
                player = value;
                player.setBid(playerInput.getBid());
            }
        }
        return player;
    }

    private boolean checkAllBidsMade(Lobby lobby) {
    boolean allBidsMade = true;
        ArrayList<Player> playerList = lobby.getPlayers();
        for (Player player : playerList) {
            if(player.getBid() == null){
                allBidsMade = false;
                break;
            }
        }
        return allBidsMade;
    }


    public void startGame(Long lobbyCode) {
        if (!checkIfLobbyExists(lobbyCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist.");
        }
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode);
        for (int i = 0; i<lobby.getPlayers().size(); i++){
            lobby.getGameTable().addPlayer(lobby.getPlayers().get(i));
        }
        lobby.getGameTable().setStartingPlayer(lobby.getPlayers().get(0));
        nextRound(lobby);
  }

    private void nextRound(Lobby lobby) {
        lobby.setRound(lobby.getRound()+1);
        lobby.getGameLogic().distributeCards();
    }


    public int getRound (Long lobbyCode){
      if (!checkIfLobbyExists(lobbyCode)) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist.");
      }
      Lobby lobby =lobbyRepository.findByLobbyCode(lobbyCode);
      return lobby.getRound();
  }
}
