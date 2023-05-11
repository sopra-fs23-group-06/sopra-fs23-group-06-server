package ch.uzh.ifi.hase.soprafs23.Points;


import ch.uzh.ifi.hase.soprafs23.Game.GameTable;
import ch.uzh.ifi.hase.soprafs23.constant.CardColor;
import ch.uzh.ifi.hase.soprafs23.constant.CardOption;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class EvaluateTest {
    static ArrayList<Card> cards = Card.getCards();
    static Card pirate;
    static Card mermaid;
    static Card skullking;
    static Card oneRed;
    static Card oneBlue;
    static Card oneBlack;
    static Card twoRed;
    static Card scarymary;
    static Card escape;

    @BeforeAll
    static void setup(){
        pirate = cards.get(60);
        mermaid = cards.get(58);
        skullking = cards.get(65);
        oneBlue = cards.get(13);
        oneBlack = cards.get(39);
        oneRed = cards.get(0);
        twoRed = cards.get(1);
        scarymary = cards.get(64);
        escape = cards.get(52);
    }

    @Test
    public void noBonus(){
        Trick trick = new Trick();
        Player trickWinner = new Player();
        trick.addPlayedCards(oneRed);
        trick.addPlayedCards(oneBlue);
        Evaluate.calcBonus(trick, trickWinner);
        assertEquals(0,trickWinner.getBonus());
    }

    @Test
    public void mermaidBonus(){
        Trick trick = new Trick();
        Player trickWinner = new Player();
        trick.addPlayedCards(mermaid);
        trick.addPlayedCards(skullking);
        Evaluate.calcBonus(trick, trickWinner);
        assertEquals(50,trickWinner.getBonus());
    }

    @Test
    public void mermaidBonusWithPirate(){
        Trick trick = new Trick();
        Player trickWinner = new Player();
        trick.addPlayedCards(skullking);
        trick.addPlayedCards(mermaid);
        trick.addPlayedCards(pirate);
        Evaluate.calcBonus(trick, trickWinner);
        assertEquals(50,trickWinner.getBonus());
    }

    @Test
    public void BonusWithPirateScaryMary(){
        Trick trick = new Trick();
        Player trickWinner = new Player();
        trick.addPlayedCards(skullking);
        trick.addPlayedCards(scarymary);
        trick.addPlayedCards(pirate);
        trick.addPlayedCards(oneRed);
        Evaluate.calcBonus(trick, trickWinner);
        assertEquals(60,trickWinner.getBonus());
    }

    @Test
    public void skullkingBonus(){
        Trick trick = new Trick();
        Player trickWinner = new Player();
        trick.addPlayedCards(pirate);
        trick.addPlayedCards(pirate);
        trick.addPlayedCards(skullking);
        Evaluate.calcBonus(trick, trickWinner);
        assertEquals(60,trickWinner.getBonus());
    }
    @Test
    public void evaluateTrick() {

        Trick trick = new Trick();
        GameTable gameTable = new GameTable();

        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();

        trick.addPlayedCards(oneRed);
        trick.addPlayedCards(pirate);
        trick.addPlayedCards(skullking);
        trick.addPlayedCards(mermaid);
        trick.setTrumpColour(oneRed);
        trick.setIsTrumpSet(true);

        gameTable.addPlayer(player1);
        gameTable.addPlayer(player2);
        gameTable.addPlayer(player3);
        gameTable.addPlayer(player4);
        gameTable.setTrickStarter(player2);

        assertNotNull(Evaluate.evaluate(gameTable, trick));
        assertEquals(player1, Evaluate.evaluate(gameTable, trick));
        assertEquals(50, player1.getBonus());
    }
    @Test
    public void checkOneRedTwoRed() {
        assertNotNull(Evaluate.check(oneRed, twoRed, twoRed.getColor()));
        assertEquals(twoRed, Evaluate.check(oneRed, twoRed, twoRed.getColor()));
    }
    @Test
    public void checkTwoRedOneRed() {
        assertNotNull(Evaluate.check(twoRed, oneRed, twoRed.getColor()));
        assertEquals(twoRed, Evaluate.check(twoRed, oneRed, twoRed.getColor()));
    }
    @Test
    public void checkOneRedEscape() {
        assertNotNull(Evaluate.check(oneRed, escape, oneRed.getColor()));
        assertEquals(oneRed, Evaluate.check(oneRed, escape, oneRed.getColor()));
    }
    @Test
    public void checkOneRedSkullKing() {
        assertNotNull(Evaluate.check(oneRed, skullking, oneRed.getColor()));
        assertEquals(skullking, Evaluate.check(oneRed, skullking, oneRed.getColor()));
    }
    @Test
    public void checkOneRedOneBlack() {
        assertNotNull(Evaluate.check(oneRed, oneBlack, oneRed.getColor()));
        assertEquals(oneBlack, Evaluate.check(oneRed, oneBlack, oneRed.getColor()));
    }
    @Test
    public void checkOneRedOneBlue() {
        assertNotNull(Evaluate.check(oneRed, oneBlue, oneRed.getColor()));
        assertEquals(oneRed, Evaluate.check(oneRed, oneBlue, oneRed.getColor()));
    }
    @Test
    public void checkEscapeOneRed() {
        assertNotNull(Evaluate.check(escape, oneRed, oneRed.getColor()));
        assertEquals(oneRed, Evaluate.check(escape, oneRed, oneRed.getColor()));
    }
    @Test
    public void checkSkullKingOneRed() {
        assertNotNull(Evaluate.check(skullking, oneRed, oneRed.getColor()));
        assertEquals(skullking, Evaluate.check(skullking, oneRed, oneRed.getColor()));
    }
    @Test
    public void checkOneBlackOneRed() {
        assertNotNull(Evaluate.check(oneBlack, oneRed, oneRed.getColor()));
        assertEquals(oneBlack, Evaluate.check(oneBlack, oneRed, oneRed.getColor()));
    }
    @Test
    public void checkOneBlueOneRed() {
        assertNotNull(Evaluate.check(oneBlue, oneRed, oneRed.getColor()));
        assertEquals(oneRed, Evaluate.check(oneBlue, oneRed, oneRed.getColor()));
    }
    @Test
    public void checkOneBlackEscape() {
        assertNotNull(Evaluate.check(oneBlack, escape, oneRed.getColor()));
        assertEquals(oneBlack, Evaluate.check(oneBlack, escape, oneRed.getColor()));
    }
    @Test
    public void checkOneBlackPirate() {
        assertNotNull(Evaluate.check(oneBlack, pirate, oneRed.getColor()));
        assertEquals(pirate, Evaluate.check(oneBlack, pirate, oneRed.getColor()));
    }
    @Test
    public void checkOneBlackOneRedTrumpBlue() {
        assertNotNull(Evaluate.check(oneBlack, oneRed, oneBlue.getColor()));
        assertEquals(oneBlack, Evaluate.check(oneBlack, oneRed, oneBlue.getColor()));
    }
    @Test
    public void checkEscapeOneBlack() {
        assertNotNull(Evaluate.check(escape, oneBlack, oneRed.getColor()));
        assertEquals(oneBlack, Evaluate.check(escape, oneBlack, oneRed.getColor()));
    }
    @Test
    public void checkPirateOneBlack() {
        assertNotNull(Evaluate.check(pirate, oneBlack, oneRed.getColor()));
        assertEquals(pirate, Evaluate.check(pirate, oneBlack, oneRed.getColor()));
    }
    @Test
    public void checkOneRedOneBlackTrumpBlue() {
        assertNotNull(Evaluate.check(oneRed, oneBlack, oneBlue.getColor()));
        assertEquals(oneBlack, Evaluate.check(oneRed, oneBlack, oneBlue.getColor()));
    }
    @Test
    public void checkOneRedOneBlueTrumpBlack() {
        assertNotNull(Evaluate.check(oneRed, oneBlue, oneBlack.getColor()));
        assertEquals(oneRed, Evaluate.check(oneRed, oneBlue, oneBlack.getColor()));
    }
    @Test
    public void checkOneBlueTwoRedTrumpBlack() {
        assertNotNull(Evaluate.check(oneBlue, twoRed, oneBlack.getColor()));
        assertEquals(twoRed, Evaluate.check(oneBlue, twoRed, oneBlack.getColor()));
    }
    @Test
    public void scaryMaryEscape() {
        scarymary.setaOption(CardOption.ESCAPE);
        assertNotNull(Evaluate.checkScary(scarymary, escape));
        assertEquals(scarymary, Evaluate.checkScary(scarymary, escape));
    }
    @Test
    public void scaryMaryOneRed() {
        scarymary.setaOption(CardOption.ESCAPE);
        assertNotNull(Evaluate.checkScary(scarymary, oneRed));
        assertEquals(oneRed, Evaluate.checkScary(scarymary, oneRed));
    }
    @Test
    public void scaryMarySkullKing() {
        scarymary.setaOption(CardOption.PIRATE);
        assertNotNull(Evaluate.checkScary(scarymary, skullking));
        assertEquals(skullking, Evaluate.checkScary(scarymary, skullking));
    }
    @Test
    public void scaryMaryOneBlue() {
        scarymary.setaOption(CardOption.PIRATE);
        assertNotNull(Evaluate.checkScary(scarymary, oneBlue));
        assertEquals(scarymary, Evaluate.checkScary(scarymary, oneBlue));
    }
    @Test
    public void OneBlueScaryMary() {
        scarymary.setaOption(CardOption.ESCAPE);
        assertNotNull(Evaluate.checkScary(oneBlue, scarymary));
        assertEquals(oneBlue, Evaluate.checkScary(oneBlue, scarymary));
    }
    @Test
    public void SkullKingScaryMary() {
        scarymary.setaOption(CardOption.PIRATE);
        assertNotNull(Evaluate.checkScary(skullking, scarymary));
        assertEquals(skullking, Evaluate.checkScary(skullking, scarymary));
    }
    @Test
    public void OneRedScaryMary() {
        scarymary.setaOption(CardOption.PIRATE);
        assertNotNull(Evaluate.checkScary(oneRed, scarymary));
        assertEquals(scarymary, Evaluate.checkScary(oneRed, scarymary));
    }
    @Test
    public void compareCards() {
        Trick trick = new Trick();

        trick.addPlayedCards(oneRed);
        trick.addPlayedCards(pirate);
        trick.addPlayedCards(pirate);
        trick.addPlayedCards(mermaid);
        trick.setTrumpColour(oneRed);
        trick.setIsTrumpSet(true);

        assertNotNull(Evaluate.compareCards(trick));
        assertEquals(pirate, Evaluate.compareCards(trick));
    }
}