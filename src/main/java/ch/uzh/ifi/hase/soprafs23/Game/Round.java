package ch.uzh.ifi.hase.soprafs23.Game;

import java.io.Serializable;

public class Round implements Serializable {

    private int round = 0;

    public void setRound(int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }

}
