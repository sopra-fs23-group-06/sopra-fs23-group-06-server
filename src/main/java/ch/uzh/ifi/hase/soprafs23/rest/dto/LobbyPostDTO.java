package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.game.GameLogic;
import ch.uzh.ifi.hase.soprafs23.game.GameTable;
import ch.uzh.ifi.hase.soprafs23.entity.Deck;
import ch.uzh.ifi.hase.soprafs23.entity.Player;

import java.util.ArrayList;

public class LobbyPostDTO {

    private Long id;
    private Long lobbyCode;
    private ArrayList<Player> players;
    private int round;
    private GameTable gameTable;
    private GameLogic gameLogic;
    private Deck deck;

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

    public ArrayList<Player> getPlayers() { return players; }

    public void setPlayers(ArrayList<Player> players) { this.players = players; }

    public int getRound() {return round;}

    public void setRound(int round) {this.round = round;}

    public GameTable getGameTable() {return gameTable;}

    public void setGameTable(GameTable gameTable) {this.gameTable = gameTable;}

    public Deck getDeck() {return deck;}

    public void setDeck(Deck deck) {this.deck = deck;}

    public GameLogic getGameLogic() {return gameLogic;}

    public void setGameLogic(GameLogic gameLogic) {this.gameLogic = gameLogic;}
}
