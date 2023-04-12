package ch.uzh.ifi.hase.soprafs23.Game;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private List<Player> Order = new ArrayList<Player>();

    public List<Player> getOrder() {
        return Order;
    }

    public void addPlayer(Player p){
        Order.add(p);
    }
}
