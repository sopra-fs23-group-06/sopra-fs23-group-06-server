package ch.uzh.ifi.hase.soprafs23.Game;

public class Round {

    private static Round instance;
    private int round = 0;
    private Round(){}

    public static synchronized Round getInstance() {
        if (instance == null) {
            instance = new Round();
        }
        return instance;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }

}
