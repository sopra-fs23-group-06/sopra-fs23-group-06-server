package ch.uzh.ifi.hase.soprafs23.Points;

import ch.uzh.ifi.hase.soprafs23.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class Scoreboard implements Serializable {
    private ArrayList<ArrayList<Score>> scoreboard;

    private ArrayList<String> usernames;

    public Scoreboard(ArrayList<Player> players) {
        scoreboard = new ArrayList<ArrayList<Score>>();
        usernames = new ArrayList<String>();
        for (Player player : players) {
            usernames.add(player.getUsername());
        }
        for (int i = 0; i < players.size(); i++) {
            ArrayList<Score> row = new ArrayList<Score>();
            Player player = players.get(i);
            for (int j = 0; j < 10; j++) {
                Score score = new Score();
                score.setCurPlayer(player);
                score.setCurRound(j+1);
                row.add(score);
            }
            scoreboard.add(row);
        }
    }
    public void setScoreboard(Score score) {
        int index = usernames.indexOf(score.getCurPlayer());
        int round = score.getCurRound() - 1;
        Score scoreAtIndex = scoreboard.get(index).get(round);
        scoreAtIndex.setCurPoints(score.getCurPoints());
        scoreAtIndex.setCurBid(score.getCurBid());
    }

    public ArrayList<ArrayList<Score>> getScoreboard() {
        return scoreboard;
    }
}
