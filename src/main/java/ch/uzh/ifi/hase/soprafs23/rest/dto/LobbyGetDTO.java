package ch.uzh.ifi.hase.soprafs23.rest.dto;
import ch.uzh.ifi.hase.soprafs23.entity.User;

import java.util.ArrayList;

public class LobbyGetDTO {

  private Long id;
  private Long lobbyCode;
  private ArrayList<User> players;

  public Long getId() {
      return id;
  }

  public void setId(Long id) {
        this.id = id;
    }


  public Long getLobbyCode() {
    return lobbyCode;
  }

  public void setLobbyCode(Long lobbyCode) {
    this.lobbyCode = lobbyCode;
  }


}
