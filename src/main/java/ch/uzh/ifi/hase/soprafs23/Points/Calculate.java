package ch.uzh.ifi.hase.soprafs23.Points;

import ch.uzh.ifi.hase.soprafs23.entity.Player;

public class Calculate {
    private static int trickDifference;
    private static int grantedPoints;

    public static int calculatePoints(Player p, int round){
        trickDifference = Math.abs(p.getTricks() - p.getBid());
        if(p.getBid() == 0 && p.getTricks() == 0)
            grantedPoints = round * 10;
        else if(p.getBid() == 0 && p.getTricks() != 0)
            grantedPoints = - (round * 10);
        else if(trickDifference == 0)
            grantedPoints = p.getBid() * 20;
        else grantedPoints = - (trickDifference * 10);
        return grantedPoints;
    }

}
