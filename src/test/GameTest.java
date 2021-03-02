package test;

import unsw.gloriaromanus.classes.*;
import unsw.gloriaromanus.classes.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import unsw.gloriaromanus.*;

public class GameTest {

    public static String folderName = "testGame1";
    public static Player currentPlayer;
    public static Game game;
    public static String firstFaction = "Rome";
    public static String secondFaction = "Gaul";

    @Test

    public void factionNameTest() throws IOException {
        HashMap<String, ArrayList<String>> province_allocation = GameScreenController.generateNewProvince(folderName, firstFaction, secondFaction);
        HashMap<String, String> goals = GameScreenController.createGoals(folderName);
        game =  new Game(folderName, firstFaction, secondFaction, province_allocation, goals);
        Player player1 = new Player();
        player1.setGame(game);
        //ArrayList<Province> firstProvinceList = createProvinceList(firstFaction, province_allocation.get(firstFaction));
        // test faction names
        assertEquals(firstFaction, game.getPlayer1Faction().getFactionName());
        assertEquals(secondFaction, game.getPlayer2Faction().getFactionName());
        assertEquals(null, game.getWinner());
    }
    
    @Test
    public void factionGettersTest() throws IOException {
        HashMap<String, ArrayList<String>> province_allocation = GameScreenController.generateNewProvince(folderName, firstFaction, secondFaction);
        HashMap<String, String> goals = GameScreenController.createGoals(folderName);
        game =  new Game(folderName, firstFaction, secondFaction, province_allocation, goals);
        
        // getCurrentPlayer
        assertEquals(firstFaction, game.getCurrentPlayer().getPlayerFactionName());
        // get currentwinner
        assertEquals(null, game.getWinner());
        // setWinner
        Player p = new Player();
        //Faction f = new Faction("Faction", p);
        game.setWinner(p);
        assertEquals(p, game.getWinner());
        // get currentplayerfactionstring
        assertEquals(firstFaction, game.getCurrentPlayerFactionString());
        // getCurrentRound
        assertEquals(1, game.getCurrentRound());

    }

    @Test
    public void endroundTest() throws IOException {
        HashMap<String, ArrayList<String>> province_allocation = GameScreenController.generateNewProvince(folderName, firstFaction, secondFaction);
        HashMap<String, String> goals = GameScreenController.createGoals(folderName);
        game =  new Game(folderName, firstFaction, secondFaction, province_allocation, goals);
        assertEquals(1, game.getCurrentRound());
        game.endRound();
        assertEquals(2, game.getCurrentRound());
    }

    @Test
    public void notifyObserverTest() throws IOException {
        HashMap<String, ArrayList<String>> province_allocation = GameScreenController.generateNewProvince(folderName, firstFaction, secondFaction);
        HashMap<String, String> goals = GameScreenController.createGoals(folderName);
        game =  new Game(folderName, firstFaction, secondFaction, province_allocation, goals);
        Faction f1 = game.getPlayer1().getFaction();
        Faction f2 = game.getPlayer2().getFaction();
        assertEquals(1,f1.getCurrentRound());
        game.endRound();
        assertEquals(2,f1.getCurrentRound());
        assertEquals(2,f2.getCurrentRound());
    }
}