package ch.uzh.ifi.hase.soprafs23.points;

import ch.uzh.ifi.hase.soprafs23.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class Scoreboard implements Serializable {
    private ArrayList<ArrayList<Score>> scoreboardTable;

    private ArrayList<String> usernames;

    public Scoreboard(ArrayList<Player> players, int roundsToEndGame) {
        scoreboardTable = new ArrayList<>();
        usernames = new ArrayList<>();
        for (Player player : players) {
            usernames.add(player.getUsername());
        }
        for (int i = 0; i < players.size(); i++) {
            ArrayList<Score> row = new ArrayList<>();
            Player player = players.get(i);
            for (int j = 0; j < roundsToEndGame; j++) {
                Score score = new Score();
                score.setCurPlayer(player);
                score.setCurRound(j+1);
                row.add(score);
            }
            scoreboardTable.add(row);
        }
    }
    public void setScoreboard(Score score) {
        int index = usernames.indexOf(score.getCurPlayer());
        int round = score.getCurRound() - 1;
        Score scoreAtIndex = scoreboardTable.get(index).get(round);
        scoreAtIndex.setCurPoints(score.getCurPoints());
        scoreAtIndex.setCurBid(score.getCurBid());
        scoreAtIndex.setCurTricks(score.getCurTricks());
    }

    public ArrayList<ArrayList<Score>> getScoreboard() {
        return scoreboardTable;
    }
}
