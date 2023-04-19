package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.Game.GameLogic;
import ch.uzh.ifi.hase.soprafs23.Game.Round;
import ch.uzh.ifi.hase.soprafs23.Game.GameTable;


import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;


  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private Long lobbyCode;

  @Lob
  private ArrayList<User> players;
  @Transient
  private Round round = new Round();
  @Transient
  private GameTable gameTable = new GameTable();
  @Transient
  private Deck deck = new Deck();

  public void CreateGameLogic(){
      GameLogic gameLogic = new GameLogic(deck, gameTable,round);
  }
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

  public ArrayList<User> getPlayers() {return players;}

  public void setPlayers(ArrayList<User> players) {this.players = players;}

   /* public void addPlayers(User userToAdd) {
        ArrayList<User> players = this.players;
        players.add(userToAdd);
        this.setPlayers(players);
            }
*/
   public void addPlayers(User userToAdd){
       this.players.add(userToAdd);
   }

}
