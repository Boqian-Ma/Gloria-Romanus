package unsw.gloriaromanus.classes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

public class Game extends RoundSubject {

    private String folderName;
    private LocalDateTime creationTime;
    private int currentRound;
    private GoalComponent goals;
    private Player currentPlayer;
    private Player player1;
    private Player player2;
    private Player winner;

    private Graph map;

    /**
     * Create a game
     * @param folderName
     * @param firstFaction
     * @param secondFaction
     * @param province_allocation
     * @param goals
     * @throws IOException
     */
    public Game(String folderName, String firstFaction, String secondFaction,
            HashMap<String, ArrayList<String>> province_allocation, HashMap<String, String> goals) throws IOException {
        // create RoundSubject 
        super();
        // create a map
        this.map = new Graph();

        // create first player and his/her faction 
        this.player1 = new Player();

        this.player1.setGame(this);
        Faction player1Faction = new Faction(firstFaction, this.player1);
        this.player1.setFaction(player1Faction);

        // create second player and his/her faction
        this.player2 = new Player();
        this.player2.setGame(this);
        Faction player2Faction = new Faction(secondFaction, this.player2);
        this.player2.setFaction(player2Faction);

        // create province object list
        ArrayList<Province> firstProvinceList = createProvinceList(this.player1, province_allocation.get(firstFaction));
        ArrayList<Province> secondProvinceList = createProvinceList(this.player2, province_allocation.get(secondFaction));

        // set provinces to players
        player1Faction.setProvinces(firstProvinceList);
        player2Faction.setProvinces(secondProvinceList);            
        this.currentRound = 1;
        // set current players
        this.currentPlayer = this.player1;
        // attach factions as observers
        super.attach(player1Faction);
        super.attach(player2Faction);
        this.creationTime = LocalDateTime.now();

        // setup goals
        setUpGoals(player1Faction, player2Faction, goals);
        // Create goals
        // set folder name
        this.folderName = folderName;
        this.winner = null;
    }

    /**
     * Set goals given a hashmap
     * @param faction1
     * @param faction2
     * @param goals
     */
    private void setUpGoals(Faction faction1, Faction faction2, HashMap<String, String> goals) {
        GoalComponent faction1goals = new GoalComposite("and", faction1);
        GoalComponent faction2goals = new GoalComposite("and", faction2);
        faction1.setGoalComponent(faction1goals, goals);
        faction2.setGoalComponent(faction2goals, goals);
    }



    /**
     * Change the current player.
     */
    public void changeCurrentPlayer() {
        if (this.currentPlayer.getPlayerFactionName().equals(this.player1.getPlayerFactionName())){
            System.out.print(this.currentPlayer.getPlayerFactionName());
            this.currentPlayer = this.player2;
        } else {
            this.currentPlayer = this.player1;
            this.endRound();
        }   
    }
     /**
     * Create a list of province object from a province's name string
     * @param player
     * @param stringList
     * @return
     */
    public static ArrayList<Province> createProvinceList(Player player, ArrayList<String> stringList) {
        ArrayList<Province> list = new ArrayList<>();
        for (String province : stringList) {
            list.add(new Province(player, province));
        }
        return list;
    }


    /**
     * Save a game to json
     * 
     * @throws IOException
     */
    public void saveGame(String folderName) throws IOException {
        // create a json object 
        JSONObject gameData = new JSONObject();
        // get p1 faction information
        

        gameData.put("saved_time", LocalDateTime.now().toString());
        gameData.put("current_player", this.currentPlayer.getFaction().getFactionName());
        gameData.put("current_round", this.currentRound);
        

        // if there is a winner
        if (this.winner != null) {
            gameData.put("winner", this.winner.getFaction().getFactionName());
        } else {
            gameData.put("winner", "null");
        }

        // player data
        JSONObject player1Data = this.player1.getFaction().saveGame();
        JSONObject player2Data = this.player2.getFaction().saveGame();
        gameData.put("player1", player1Data);
        gameData.put("player2", player2Data);

        // write to file
        String gameDataString = gameData.toString(2);
        Writer output = null;
        File file = new File("src/unsw/gloriaromanus/gamedata/" + folderName + "/saved_data.json");
        output = new BufferedWriter(new FileWriter(file));
        output.write(gameDataString);
        output.close();
        
    }

    // Load game constructor
    public Game(String folderName, int currentRound, Player player1, Player player2, Player currentPlayer, GoalComponent goals) {
        this.folderName = folderName;
        this.currentRound = currentRound;
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = currentPlayer;
        this.goals = goals;
        this.winner = null;

    }

    /**
     * Load a game
     * @param folderName
     * @return 
     */
    public static Game loadGame(String folderName) {


        //Game game = new Game();

        // open saved file
        
        // create players

        
        // get game info
            // 1 current round

        // create player
            /*
            provinces": {
                "Africa Proconsularis": {
                  "wealth_rate": 10,
                  "faction": "Rome",
                  "train_slot_1": {
                    "occupancy": false,
                    "end_turn": 0
                  },
                  "train_slot_2": {
                    "occupancy": false,
                    "end_turn": 0
                  },
                  "units": {},
                  "tax_rate": 0.1
                },
            */
        
        // create player 1
            // create faction
                // faction info
                    // total wealth 
                    // gold
            // create province  
                // province into
                    // wealth 
                    // gold
                    // tax rate
                    // wealth rate
                // training slots
                    //occupancy
                    // end_turn
                    // current_unit
                // units
                    // currentMvPoint
                    // attacked

        
        // load rest game info
            // 1. current player
            // 2. winner
            // 3. set goals

    
        return null;
    }

    // getters and setters
    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }
    public Player getWinner() {

        return this.winner;
    }
    public void setWinner(Player player) {
        this.winner = player;
    }
    public String getCurrentPlayerFactionString(){
        return this.currentPlayer.getPlayerFactionName();
    }
    public int getCurrentRound() {
        return this.currentRound;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void endRound() {
        this.currentRound += 1;
        notifyRoundObservers();
    }

    public void notifyRoundObservers() {
        for (RoundObserver o : super.getRoundObservers()) {
            o.updateRound(this.currentRound);
        }
    }

    public Player getPlayer1() {
        return this.player1;
    }

    public Player getPlayer2() {
        return this.player2;
    }
    
    public Graph getMap() {
        return this.map;
    }

    public Faction getPlayer1Faction() {
        return this.player1.getFaction();
    }

    public Faction getPlayer2Faction() {
        return this.player2.getFaction();
    }
}
