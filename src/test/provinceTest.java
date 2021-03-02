package test;

import unsw.gloriaromanus.classes.*;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;


import unsw.gloriaromanus.*;

public class provinceTest {
    public static Player currentPlayer;
    public static Game game;
    public static String firstFaction = "Rome";
    public static String secondFaction = "Gaul";
    public static String folderName = "testGame1";

    @Test
    public void trainSlotTest() throws IOException {
        Player player = new Player();
        Faction faction = new Faction("faction", player);
        player.setFaction(faction);
        Province province = new Province(player, "yeetopia");
        assertEquals("yeetopia", province.getProvinceName());
        assertEquals("faction", faction.getFactionName());
        assertEquals(faction, player.getFaction());
        assertEquals(null, player.getGame());
        Faction faction2 = new Faction("faction2", player);
        player.setFaction(faction2);
        assertEquals(faction2, player.getFaction());
    }
    
    @Test
    public void provinceWealthTest() throws IOException {
        Player player = new Player();
        Faction faction = new Faction("faction", player);
        player.setFaction(faction);
        Province province = new Province(player, "yeetopia");
        assertEquals(0, province.getWealth());
        province.increaseWealth();
        assertEquals(10, province.getWealth());
    }

    @Test
    public void provinceTrainingSlotsTest() throws IOException {
        Player player = new Player();
        Faction faction = new Faction("faction", player);
        player.setFaction(faction);
        Province province = new Province(player, "yeetopia");
        assertEquals(2, province.getTrainingSlots().size());
        Province.addSlots(1, province.getTrainingSlots(), province);
        assertEquals(3, province.getTrainingSlots().size());
    }

    @Test
    public void provinceSizeTest() throws IOException {
        Player player = new Player();
        Faction faction = new Faction("faction", player);
        player.setFaction(faction);
        Province province = new Province(player, "yeetopia");
        assertEquals(0, province.getUnits().size());
    }

    @Test
    public void performTrainTest() throws IOException {
        HashMap<String, ArrayList<String>> province_allocation = GameScreenController.generateNewProvince(folderName, firstFaction, secondFaction);
        HashMap<String, String> goals = GameScreenController.createGoals(folderName);
        game =  new Game(folderName, firstFaction, secondFaction, province_allocation, goals);

        Faction firstFaction = game.getPlayer1Faction();
        firstFaction.setGold(5);

        Faction secondFaction = game.getPlayer2Faction();
        secondFaction.setGold(5);
        assertEquals(5, firstFaction.getGold()); 

        Province p1 = firstFaction.getProvinces().get(0);

        TrainSlot p1s1 = p1.getTrainingSlots().get(0);
        TrainSlot p1s2 = p1.getTrainingSlots().get(1);

        Province p2 = secondFaction.getProvinces().get(0);
        TrainSlot p2s1 = p2.getTrainingSlots().get(0);
        TrainSlot p2s2 = p2.getTrainingSlots().get(1);

        p1.performTrain("elephant");
        assertEquals(0, firstFaction.getGold()); 
        p2.performTrain("elephant");
        assertEquals(0, secondFaction.getGold()); 
        
        // attampt to train when slots no money
        assertEquals(false, p1.performTrain("elephant")); 
        assertEquals(false, p2.performTrain("elephant")); 
        // check occupancy
        assertEquals(true, p1s1.getOccupancy());
        game.endRound();
        game.endRound();
        assertEquals(false, p1s1.getOccupancy());

        p1.performTrain("elephant");
        p2.performTrain("elephant");
        p1.performTrain("elephant");
        p2.performTrain("elephant");

        // switch owner
        p1.changeOwner(secondFaction);
        p2.changeOwner(firstFaction);
        assertEquals(false, p1s1.getOccupancy());
        assertEquals(new ArrayList<>(), p1.getUnits());

    }

    @Test
    public void taxBehaviourTest() throws IOException {
        HashMap<String, ArrayList<String>> province_allocation = GameScreenController.generateNewProvince(folderName, firstFaction, secondFaction);
        HashMap<String, String> goals = GameScreenController.createGoals(folderName);
        game =  new Game(folderName, firstFaction, secondFaction, province_allocation, goals);
        Faction f = game.getPlayer1Faction();
        Province p1 = f.getProvinces().get(0);

        p1.setTaxBehaviour(0.5, 100);
        Tax t = (Tax) p1.getTaxBehaviour();
        assertEquals(0.5, t.getTaxRate());
        assertEquals(100, t.getWealthRate());
    }

    @Test
    public void addRemoveTest() throws IOException {
        HashMap<String, ArrayList<String>> province_allocation = GameScreenController.generateNewProvince(folderName, firstFaction, secondFaction);
        HashMap<String, String> goals = GameScreenController.createGoals(folderName);
        game =  new Game(folderName, firstFaction, secondFaction, province_allocation, goals);
        Faction f = game.getPlayer1Faction();
        Province p1 = f.getProvinces().get(0);
        ArrayList<Unit> u = new ArrayList<>();
        p1.setUnits(u);
        Unit unit = new Unit("elephant");
        p1.addUnit(unit);
        assertEquals(1, p1.getUnits().size());
        p1.removeUnit(unit);
        assertEquals(0, p1.getUnits().size());
    }
    
    
}
