package test;

import unsw.gloriaromanus.classes.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;


import unsw.gloriaromanus.*;

public class FactionTest {

    public static String folderName = "testGame1";
    public static Player currentPlayer;
    public static Game game;
    public static String firstFaction = "Rome";
    public static String secondFaction = "Gaul";

    @Test
    public void wealthTest() throws IOException {
        HashMap<String, ArrayList<String>> province_allocation = GameScreenController.generateNewProvince(folderName, firstFaction, secondFaction);
        HashMap<String, String> goals = GameScreenController.createGoals(folderName);
        game =  new Game(folderName, firstFaction, secondFaction, province_allocation, goals);

        Faction f1 = game.getPlayer1Faction();
        int length = province_allocation.get(firstFaction).size();
        // getNumProvinces
        assertEquals(length, f1.getNumProvinces());
        f1.removeAllProvinces();
        assertEquals(0, f1.getNumProvinces());
        Province p = new Province(f1.getPlayer(), "Yeet");
        f1.addProvince(p);
        assertEquals(1, f1.getNumProvinces());
        // getwealth
        assertEquals(0, f1.getWealth());
        game.endRound();
        assertEquals(9, f1.getWealth());
    }

    @Test
    public void getterTest() throws IOException {
        HashMap<String, ArrayList<String>> province_allocation = GameScreenController.generateNewProvince(folderName, firstFaction, secondFaction);
        HashMap<String, String> goals = GameScreenController.createGoals(folderName);
        game =  new Game(folderName, firstFaction, secondFaction, province_allocation, goals);
        Faction f1 = game.getPlayer1Faction();
        // get faction name
        assertEquals(firstFaction, f1.getFactionName());
        // get currentround
        f1.setCurrentRound(1);
        assertEquals(1, f1.getCurrentRound());
        // get player
        Player p1 = game.getPlayer1();
        assertEquals(p1, f1.getPlayer());
        //get gold
        assertEquals(0, f1.getGold());
        f1.setGold(10);
        assertEquals(10, f1.getGold());
    }

    @Test
    public void getProvincesTest() throws IOException {
        HashMap<String, ArrayList<String>> province_allocation = GameScreenController.generateNewProvince(folderName, firstFaction, secondFaction);
        HashMap<String, String> goals = GameScreenController.createGoals(folderName);
        game =  new Game(folderName, firstFaction, secondFaction, province_allocation, goals);
        Faction f1 = game.getPlayer1Faction();
        
        assertEquals(province_allocation.get(firstFaction), f1.getProvincesString());

        //remove province
        // locate a province to remove
        ArrayList<Province> a = new ArrayList<>();
        Province p = new Province(f1.getPlayer(), "yeet");
        a.add(p);

        f1.removeAllProvinces();
        f1.addProvince(p);
        assertEquals(a, f1.getProvinces());
        a.remove(p);
        f1.removeProvince(p);
        assertEquals(a , f1.getProvinces());
    }




    
}