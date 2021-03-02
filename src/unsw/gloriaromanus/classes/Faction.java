package unsw.gloriaromanus.classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

public class Faction implements RoundObserver {
    private static int COST_OF_MOVING = 4;
    private Player player;
    private String factionString;
    private ArrayList<Province> provinces;
    private int gold;
    private int totalWealth;
    private int currentRound;
    private GoalComponent goalComponent;

    private MoveBehaviour moveBehaviour;

    public Faction(String factionString, Player player) {
        this.factionString = factionString;
        this.provinces = new ArrayList<>();
        this.gold = 0;
        this.player = player;
        this.totalWealth = 0;
        this.goalComponent = null;
        player.setFaction(this);
        this.currentRound = 1;
        this.moveBehaviour = new Move();
    }

    /**
     * Collect everything about this faction into a hashmap
     * @param folderName
     * @return HashMap<String, Object> containing faction information
     */
    public JSONObject saveGame() {
        // create map
        JSONObject factionData = new JSONObject();
        // add faction data
        factionData.put("faction", this.factionString);
        factionData.put("gold", this.gold);
        factionData.put("total_wealth", this.totalWealth);
        factionData.put("provinces", this.getProvincesData());
        return factionData;
    }
    /**
     * Summarise province data
     * @return
     */
    private JSONObject getProvincesData() {
        JSONObject provinceData = new JSONObject();
        for (Province p : provinces) {
            provinceData.put(p.getProvinceName(), p.saveGameProvince());
        }
        return provinceData;
    }


    /**
     * Set goals given a HashMap of goals
     * @param goalComponent
     * @param goals
     */
    public void setGoalComponent(GoalComponent goalComponent, HashMap<String, String> goals) {
        this.goalComponent = goalComponent;
        GoalLeaf andGoal = new GoalLeaf(goals.get("and"), this);
        GoalComposite orGoal = new GoalComposite("or", this);
        goalComponent.add(andGoal);
        goalComponent.add(orGoal);
        GoalLeaf orGoal1 = new GoalLeaf(goals.get("or1"), this);
        GoalLeaf orGoal2 = new GoalLeaf(goals.get("or2"), this);
        orGoal.add(orGoal1);
        orGoal.add(orGoal2);
    }

    /**
     * Move a list of troops from one province to another
     * @param from
     * @param to
     * @param units
     * @param mvPoints
     */
    public void moveUnitsToAnotherProvince(Province from, Province to, ArrayList<Unit> units, int mvPoints) {
        this.moveBehaviour.move(from, to, units, mvPoints);
    }

    /**
     * Collect tax from all provinces
     */
    public void collectTaxFromAllProvinces() {
        for (Province p : this.provinces) {
            this.gold += p.upDateWealthTax();
        }
    }

    /**
     * Update total wealth earned by all provinces
     */
    public void getTotalWealth() {
        int total = 0;
        for (Province p : this.provinces) {
            total += p.getWealth();
        }
        this.totalWealth = total;
    }

    

    /**
     * When ever a round ends, we collect taxes and update total wealth
     * Afterwards, we check if there is a winner
     */
    @Override
    public void updateRound(int currentRound) {
        this.setCurrentRound(currentRound);
        // increase wealth and collect tax 
        //update round in provinces
        for (Province p : provinces) {
            p.updateRound();
        }
        // collect tax
        collectTaxFromAllProvinces();
        // update total wealth
        getTotalWealth();
        // check winning 
        checkWinning();
        // reset all units movement points
        resetMovementPoints();
    }

    /**
     * Reset movement points
     */
    private void resetMovementPoints() {
        for (Province p : provinces) {
            // reset all movementpoints of all units
            p.resetMovementPointsOfAllUnits();
        }
    }

    /**
     * Check if a winner has been generated 
     */
    private void checkWinning() {
        if (this.player.getWinner() == null) {
            if (this.goalComponent.isSatisfiedCheck()){
                //System.out.print("yeet");
                this.player.setWinner();
            }
        } else {
            System.out.print("The game is finished already, " + this.player.getWinner().getPlayerFactionName() + " has won!");
        }
    }

    public void setCurrentRound(int Round) {
        this.currentRound = Round;
    }

    /**
     * Add a conquered province to the existing province list
     * 
     * @param province
     */
    public void addProvince(Province province) {
        this.provinces.add(province);
        // check winning 
        checkWinning();
    }

    /**
     * Remove a province from the existing list due to lost battles
     * 
     * @param province
     */
    public void removeProvince(Province province) {
        this.provinces.remove(province);
        if (this.provinces.size() == 0){
            System.out.println("\nOh No " + this.factionString + " has lost the game\n");
        }
    }



    /**
     * Check if the faction owns a province given the provinces' game string.
     * @param province
     * @return
     */
    public boolean ownsProvince(String province) {
        for (Province p : provinces) {
            if (p.getProvinceName().equals(province)){
                return true;
            }
        }
        return false;   
    }

    /**
     * Perform move
     * @param from
     * @param to
     * @param units
     * @param mvPoints
     */
    private void performMove(Province from, Province to, ArrayList<Unit> units, int mvPoints) {
        this.moveBehaviour.move(from, to, units, mvPoints);
    }
    
    /**
     * Move some units from one province to another
     * @param units
     * @param from
     * @param to
     * @return true if moved, false if couldn't (two error sources: 1. Path doesnt exist. 2. Not enough movement point to reach destination)
     * @throws IOException
     */
    public Boolean moveUnits(ArrayList<Unit> units, Province from, Province to) throws IOException {

        if (units.isEmpty()){
            return false;
        }
        // find minimum movement point
        int mvPointsRequired = minimumMovementPoint(units);
        String fromString = from.getProvinceName();
        String toString = to.getProvinceName();

        Graph map = player.getMap();
        int distance = map.getDistanceBetweenTwoProvinces(fromString, toString, this);

        // when there isnt a path
        if (distance == -1){
            System.out.println("Not enough movement points");
            return false;
        }
        // check if distance is smaller than minimum movement points
        if ((distance * COST_OF_MOVING) > mvPointsRequired) {
            System.out.println("Not enough movement points");
            return false;
        } 
        // now transfer units to 
        this.performMove(from, to, units, mvPointsRequired);
        return true;
    }

    
    /**
     * Find the minimum movement point in a list of units
     * @param units
     * @return
     */
    private static int minimumMovementPoint(ArrayList<Unit> units) {
        int min = Integer.MAX_VALUE;
        for (Unit u : units) {
            if (u.getCurrentMovementPoint() < min) {
                min = u.getCurrentMovementPoint();
            }
        }
        if (min == Integer.MAX_VALUE) {
            return 0;
        }
        return min;
    }



    /**
     * Create an army from a province
     * 
     * @param units An array list of units in the army
     * @param attackProvince
     * @param defendProvince
     * @return
     * @throws IOException
     */
    public static String createArmyBattle(ArrayList<Unit> attackUnits, Province attackProvince, Province defendProvince) throws IOException {
        //Faction attackFaction = attackProvince.getFaction();
        // check if there is a path

        int unitMvPoint = minimumMovementPoint(attackUnits);

        String attackProvinceString = attackProvince.getProvinceName();

        String defendProvinceString = defendProvince.getProvinceName();

        Graph map = attackProvince.getFaction().getMap();

        // find distance

        int distance = map.getDistanceBetweenTwoProvinces(attackProvinceString, defendProvinceString, attackProvince.getFaction());

        // if no path
        if (distance == -1) {
            return "no path";
        }

        // calculate total cost of moving
        distance = COST_OF_MOVING * distance;

        System.out.println("Unit max mv point "+ unitMvPoint);
        System.out.println("distance "+ distance);

        // if not enough movement point from the unit
        if (unitMvPoint < distance) {
            return "not Enough Movement points";
        }

        // able to attack 
        // create two armies: attacking and defending

        Army attackArmy = Army.createArmy(attackUnits, attackProvince);
        // update movement points of the army
        for (Unit u : attackArmy.getUnits()) {
            u.updateMovementPoints(unitMvPoint);
            u.setAttackedTrue();
        }

        System.out.println("Set up attack army");


        ArrayList<Unit> defendUnits = new ArrayList<>(defendProvince.getUnits());
        Army defendArmy = Army.createArmy(defendUnits, defendProvince);
        // create a battle

        System.out.println("\n**In faction before battle**");
        System.out.println("Attack army units = " + attackArmy.getUnits());
        System.out.println("Defend army units = " + defendArmy.getUnits());

        System.out.println("Attack province units should be empty = " + attackProvince.getUnits());
        System.out.println("defend province units should be empty = " + defendProvince.getUnits());

        Province winner = BattleV2.createBattle(attackArmy, defendArmy);
        System.out.println("\n**In faction after battle**");
        System.out.println("Attack province units should be empty = " + attackProvince.getUnits());
        System.out.println("defend province units should be empty = " + defendProvince.getUnits());

        // tied
        if (winner == null) {
            return "tied";
        }

        // return winning province
        if (winner.getProvinceName().equals(attackProvince.getProvinceName())){
            // if attack province wins
            // change province owner
            System.out.println("Before switching: " + defendProvince.getFaction().getFactionName());
            defendProvince.changeOwner(attackProvince.getFaction());
            System.out.println("After switching: " + defendProvince.getFaction().getFactionName());
            // send winning units to new province
            defendProvince.setUnits(attackUnits);
            // these units have been removed from attack province
            return attackProvince.getFaction().getFactionName();
        } else {
            // else defending province wins (both die or successfully defend)
            if (defendArmy.getUnits().size() == 0) {
                defendArmy.returnUnitsToProvince(new ArrayList<Unit>());
            }
            else {
                defendArmy.returnUnitsToProvince(defendArmy.getUnits());
            }
            return defendProvince.getFaction().getFactionName();
        }
    }

     // getters and setters

    public Graph getMap() {
        return player.getMap();
    }

    public int getCurrentRound(){
        return this.currentRound;
    }

     public Player getPlayer() {
        return this.player;
    }

    public int getGold() {
        return this.gold;
    }
    public void setGold(int amount) {
        this.gold = amount;
    }

    public void reduceGold(int amount) {
        this.gold -= amount;
    }

    public int getWealth() {
        return this.totalWealth;
    }
    public void setTotalWealth(int amount) {
        this.totalWealth = amount;
    }

    public void setProvinces(ArrayList<Province> provinces) {
        this.provinces = provinces;
    }

    public ArrayList<Province> getProvinces() {
        return this.provinces;
    }

    public ArrayList<String> getProvincesString() {
        ArrayList<String> list = new ArrayList<>();
        for (Province p : this.provinces){
            list.add(p.getProvinceName());
        }
        return list;
    }

    public String getFactionName() {
        return this.factionString;
    }
    
    public void removeAllProvinces() {
        this.provinces = new ArrayList<>();
    }

    public int getNumProvinces() {
        return provinces.size();
    }

    public Province getProvinceFromString(String name) {

        for (Province p : this.provinces) {
            if (name.equals(p.getProvinceName())) {
                return p;
            }
        }
        return null;
    }
}
