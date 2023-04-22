package ch.uzh.ifi.hase.soprafs23.Points;

import ch.uzh.ifi.hase.soprafs23.entity.Player;

public class Score {
    private Player curPlayer;
    private int curRound;
    private int curBid;
    private int curPoints;

    public void setCurPlayer(Player curPlayer) {
        this.curPlayer = curPlayer;
    }

    public void setCurRound(int curRound) {
        this.curRound = curRound;
    }

    public void setCurBid(int curBid) {
        this.curBid = curBid;
    }

    public void setCurPoints(int curPoints) {
        this.curPoints = curPoints;
    }

    public Player getCurPlayer() {
        return curPlayer;
    }

    public int getCurRound() {
        return curRound;
    }

    public int getCurBid() {
        return curBid;
    }

    public int getCurPoints() {
        return curPoints;
    }
}
