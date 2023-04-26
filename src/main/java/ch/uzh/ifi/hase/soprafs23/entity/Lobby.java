package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.Game.GameLogic;
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
  private ArrayList<Player> players;

  @Lob
  private final GameLogic gameLogic;


  public Lobby(){
      GameTable gameTable = new GameTable();
      Deck deck = new Deck();
      this.gameLogic = new GameLogic(deck, gameTable);

  }

  //public void CreateGameLogic(){GameLogic gameLogic = new GameLogic(deck, gameTable, round);}

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

  public ArrayList<Player> getPlayers() {return players;}

  public void setPlayers(ArrayList<Player> players) {this.players = players;}

  public void addPlayers(Player playerToAdd){this.players.add(playerToAdd);getGameTable().addPlayer(playerToAdd);}

  public int getRound(){return this.gameLogic.getRound();}

  public Deck getDeck(){return this.gameLogic.getDeck();}

  public GameTable getGameTable(){return this.gameLogic.getGameTable();}

  public GameLogic getGameLogic(){return this.gameLogic;}


  public void setRound(int r) { this.gameLogic.setRound(r);
    }


    //public void setStartingPlayer(){}
}
