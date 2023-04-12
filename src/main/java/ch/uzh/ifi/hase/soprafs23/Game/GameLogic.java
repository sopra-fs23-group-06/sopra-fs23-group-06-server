package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Points.Score;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameLogic {
    private List<Player> Players = new ArrayList<Player>();
    private ArrayList<Score> Scoreboard = new ArrayList<Score>();

    public void setPlayers(Player p) {
        Players.add(p);
    }

    public List<Player> getPlayers() {
        return Players;
    }

    public void setScoreboard(Score s, Player p) {
    }

    public ArrayList<Score> getScoreboard() {
        return Scoreboard;
    }
}
