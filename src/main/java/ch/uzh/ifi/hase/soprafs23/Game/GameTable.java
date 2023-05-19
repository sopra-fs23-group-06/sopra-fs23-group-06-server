package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameTable implements Serializable {
    private List<Player> Order = new ArrayList<Player>();
    private Player roundStarter;
    private Player trickStarter;

    public List<Player> getOrder() {
        return Order;
    }

    public void addPlayer(Player p){
        Order.add(p);
    }

    public Player getRoundStarter() {return roundStarter;}

    public void setRoundStarter(Player roundStarter) {this.roundStarter = roundStarter;}

    public Player getTrickStarter() {return trickStarter;}

    public void setTrickStarter(Player trickStarter) {this.trickStarter = trickStarter;}

    public void deleteOrder() {
        this.Order = null;
    }
}
