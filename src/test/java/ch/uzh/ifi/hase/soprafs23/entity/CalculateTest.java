package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.Game.Round;
import ch.uzh.ifi.hase.soprafs23.Points.Calculate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculateTest {

    @Test
    public void noTricks_and_noBids() {
        Round round = new Round();
        round.setRound(3);
        Player p = new Player();
        p.setTricks(0);
        p.setBid(0);
        int grantedpoints = Calculate.calculatePoints(p, round);
        assertEquals(30, grantedpoints);
    }

    @Test
    public void moreTricksthanBids() {
        Round round = new Round();
        round.setRound(3);
        Player p = new Player();
        p.setTricks(3);
        p.setBid(2);
        int grantedpoints = Calculate.calculatePoints(p, round);
        assertEquals(-10, grantedpoints);
    }

    @Test
    public void moreBidsthanTricks() {
        Round round = new Round();
        round.setRound(9);
        Player p = new Player();
        p.setTricks(4);
        p.setBid(6);
        int grantedpoints = Calculate.calculatePoints(p, round);
        assertEquals(-20, grantedpoints);
    }

    @Test
    public void sameTricksandBids() {
        Round round = new Round();
        round.setRound(9);
        Player p = new Player();
        p.setTricks(4);
        p.setBid(4);
        int grantedpoints = Calculate.calculatePoints(p, round);
        assertEquals(80, grantedpoints);
    }

    @Test
    public void noBidsandsomeTricks() {
        Round round = new Round();
        round.setRound(5);
        Player p = new Player();
        p.setTricks(2);
        p.setBid(0);
        int grantedpoints = Calculate.calculatePoints(p, round);
        assertEquals(-50, grantedpoints);
    }


}