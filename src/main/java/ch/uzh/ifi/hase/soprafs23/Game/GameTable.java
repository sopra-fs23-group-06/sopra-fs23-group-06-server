package ch.uzh.ifi.hase.soprafs23.Game;

import java.util.ArrayList;
import java.util.List;

public class GameTable {
    private List<Player> Order = new ArrayList<Player>();
    private Player startingPlayer;

    public List<Player> getOrder() {
        return Order;
    }

    public void addPlayer(Player p){
        Order.add(p);
    }

    public void setStartingPlayer(Player startingPlayer) {
        this.startingPlayer = startingPlayer;
    }
    public Player getStartingPlayer(){
        return startingPlayer;
    }
}
