package ch.uzh.ifi.hase.soprafs23.Game;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private List<Player> Order = new ArrayList<Player>();
    private static Table instance;

    private Table(){}

    public static synchronized Table getInstance() {
        if (instance == null) {
            instance = new Table();
        }
        return instance;
    }

    public List<Player> getOrder() {
        return Order;
    }

    public void addPlayer(Player p){
        Order.add(p);
    }
}
