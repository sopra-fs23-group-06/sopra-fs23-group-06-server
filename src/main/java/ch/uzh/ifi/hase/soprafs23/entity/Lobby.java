package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.game.GameLogic;
import ch.uzh.ifi.hase.soprafs23.game.GameTable;


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
  private final GameLogic gameLogic;

  public Lobby(){
      this.gameLogic = new GameLogic();
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

  public ArrayList<Player> getPlayers() {return gameLogic.getPlayers();}

  public void setPlayers(ArrayList<Player> players) {this.gameLogic.setPlayers(players);}

  public void addPlayers(Player playerToAdd){this.gameLogic.addPlayers(playerToAdd);}

  public int getRound(){return this.gameLogic.getRound();}

  public Deck getDeck(){return this.gameLogic.getDeck();}

  public GameTable getGameTable(){return this.gameLogic.getGameTable();}

  public GameLogic getGameLogic(){return this.gameLogic;}


  public void setRound(int r) { this.gameLogic.setRound(r);
    }

}
