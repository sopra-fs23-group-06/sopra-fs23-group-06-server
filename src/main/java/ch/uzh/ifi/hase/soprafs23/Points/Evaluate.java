package ch.uzh.ifi.hase.soprafs23.Points;

import ch.uzh.ifi.hase.soprafs23.Game.Player;

public class Evaluate {
    private Player trickWinner;

    public void setTrickWinner(Player trickWinner) {
        this.trickWinner = trickWinner;
    }

    public void setBonus() {
        int bonus = 0;
        trickWinner.setBonus(bonus);
    }
}
