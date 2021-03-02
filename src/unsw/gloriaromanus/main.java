package unsw.gloriaromanus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import unsw.gloriaromanus.classes.*;

import unsw.gloriaromanus.*;

public class main {

    public static String fileName = null;
    public static String folderName = null;
    public static Player currentPlayer;
    public Game game;
    public static String firstFaction;
    public static String secondFaction;

    public static void main(String[] args) throws IOException {

        // call create game
        GameScreenController.folderName = "testGame1";
        // Path path = Paths.get("src/unsw/gloriaromanus/gamedata/"+ folderName);
        String gameFolder = folderName;
        // reset folderName for next selection
        folderName = null;
        firstFaction = "Rome";
        secondFaction = "Gaul";
        // hashmap mapping current province allocation
        HashMap<String, ArrayList<String>> province_allocation = GameScreenController.generateNewProvince(gameFolder,
                firstFaction, secondFaction);

        HashMap<String, String> goals = GameScreenController.createGoals(gameFolder);
        System.out.println(goals);

        // set goals
        Game game = new Game(folderName, firstFaction, secondFaction, province_allocation, goals);
        Player currentPlayer = game.getCurrentPlayer();

        Faction firstFaction = game.getPlayer1Faction();
        Faction secondFaction = game.getPlayer2Faction();

        Province province1 = firstFaction.getProvinces().get(0);
        Province province2 = secondFaction.getProvinces().get(0);


        System.out.println("Current round " + game.getCurrentRound());
        System.out.println("Player 1 faction: " + firstFaction.getFactionName());
        System.out.println("Player 1 provinces: " + firstFaction.getProvincesString());
        System.out.println("Player 2 faction: " + secondFaction.getFactionName());
        System.out.println("Player 2 provinces: " + secondFaction.getProvincesString());

        System.out.println(firstFaction.getWealth());
        game.endRound();
        System.out.println(firstFaction.getWealth());
       
        
        province1.performTrain("elephant");
        province2.performTrain("elephant");
        province1.performTrain("elephant");
        province2.performTrain("elephant");

        System.out.println("Player 1 province units : " + province1.getUnits());
        System.out.println("Player 2 province units: " + province2.getUnits());
        game.endRound();
        System.out.println("Current round " + game.getCurrentRound());

        System.out.println("Attempting training when spots are filled");
        System.out.println("result " + province1.performTrain("elephant"));
        System.out.println("result " + province2.performTrain("elephant"));

        System.out.println("Player 1 province units : " + province1.getUnits());
        System.out.println("Player 2 province units: " + province2.getUnits());
        game.endRound();
        System.out.println("Current round " + game.getCurrentRound());
        System.out.println("Player 1 province units : " + province1.getUnits());
        System.out.println("Player 2 province units: " + province2.getUnits());
        
        /*
        //province1.move(province1, province2, units, mvPoints);
        
        game.endRound();
        System.out.println("Current round " + game.getCurrentRound());
        System.out.println("Player 1 province units : " + province1.getUnits());
        System.out.println("Player 2 province units: " + province2.getUnits());

        game.endRound();
        System.out.println("Current round " + game.getCurrentRound());
        System.out.println("Removing a unit from player 1 province ");
        province1.removeUnit(province1.getUnits().get(0));
        System.out.println("Player 1 province units : " + province1.getUnits());
        System.out.println("Player 2 province units: " + province2.getUnits());

        game.endRound();
        System.out.println("Current round " + game.getCurrentRound());
        System.out.println("Training some more elephents, will take two turns to finish....see you in two rounds");
        province1.performTrain("elephant");
        province2.performTrain("elephant");
        province1.performTrain("elephant");
        province2.performTrain("elephant");

        game.endRound();
        System.out.println("Current round " + game.getCurrentRound());
        System.out.println("Swap faction owners to if training stops");
        province1.changeOwner(secondFaction);
        province2.changeOwner(firstFaction);
        System.out.println("result " + province1.performTrain("elephant"));
        System.out.println("result " + province2.performTrain("elephant"));
        System.out.println("Player 1 province units : " + province1.getUnits());
        System.out.println("Player 2 province units: " + province2.getUnits());
        */
    }
}
