package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameTable implements Serializable {
    private List<Player> Order = new ArrayList<Player>();
    private Player startingPlayer;

    public List<Player> getOrder() {
        return Order;
    }

    public void addPlayer(Player p){
        //test purpose
        Player test = new Player();
        test.setUsername("max");
        test.setId(2L);
        Order.add(test);
        // right call
        Order.add(p);
        //test purpose
        Player test2 = new Player();
        test2.setUsername("fritzz");
        test2.setId(3L);
        Order.add(test2);
    }

    public void setStartingPlayer(Player startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    public Player getStartingPlayer(){
        return startingPlayer;
    }
}
