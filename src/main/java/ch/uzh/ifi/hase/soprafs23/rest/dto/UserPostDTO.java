package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class UserPostDTO {

  private Long id;
  private Long lobby;
  private String username;


  public Long getId() {
        return id;
    }

  public void setId(Long id) {
        this.id = id;
    }

  public Long getLobby() {
    return lobby;
  }

  public void setLobby(Long lobby) {
    this.lobby = lobby;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
