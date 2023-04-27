package ch.uzh.ifi.hase.soprafs23.Points;

import ch.uzh.ifi.hase.soprafs23.entity.Player;

public class Calculate {

    public static int calculatePoints(Player p, int round){
        int trickDifference = Math.abs(p.getTricks() - p.getBid());
        int grantedPoints;
        if(p.getBid() == 0 && p.getTricks() == 0)
            grantedPoints = round * 10;
        else if(p.getBid() == 0 && p.getTricks() != 0)
            grantedPoints = - (round * 10);
        else if(trickDifference == 0)
            grantedPoints = (p.getBid() * 20)+ p.getBonus();
        else grantedPoints = - (trickDifference * 10);
        p.setBonus(0);
        return grantedPoints;
    }

}
