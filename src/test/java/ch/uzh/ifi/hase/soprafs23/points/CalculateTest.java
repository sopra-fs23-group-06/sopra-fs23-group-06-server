package ch.uzh.ifi.hase.soprafs23.points;

import ch.uzh.ifi.hase.soprafs23.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculateTest {

    @Test
    void noTricks_and_noBids() {
        int round = 3;
        Player p = new Player();
        p.setTricks(0);
        p.setBid(0);
        int grantedpoints = Calculate.calculatePoints(p, round);
        assertEquals(30, grantedpoints);
    }

    @Test
    void moreTricksthanBids() {
        int round = 3;
        Player p = new Player();
        p.setTricks(3);
        p.setBid(2);
        int grantedpoints = Calculate.calculatePoints(p, round);
        assertEquals(-10, grantedpoints);
    }

    @Test
    void moreBidsthanTricks() {
        int round = 9;
        Player p = new Player();
        p.setTricks(4);
        p.setBid(6);
        int grantedpoints = Calculate.calculatePoints(p, round);
        assertEquals(-20, grantedpoints);
    }

    @Test
    void sameTricksandBids() {
        int round = 9;
        Player p = new Player();
        p.setTricks(4);
        p.setBid(4);
        int grantedpoints = Calculate.calculatePoints(p, round);
        assertEquals(80, grantedpoints);
    }

    @Test
    void noBidsandsomeTricks() {
        int round = 5;
        Player p = new Player();
        p.setTricks(2);
        p.setBid(0);
        int grantedpoints = Calculate.calculatePoints(p, round);
        assertEquals(-50, grantedpoints);
    }


}