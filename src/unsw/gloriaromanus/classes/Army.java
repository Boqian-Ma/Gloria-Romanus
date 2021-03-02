package unsw.gloriaromanus.classes;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import unsw.gloriaromanus.GameScreenController;

public class Army {
    private ArrayList<Unit> armyUnitList;
    //private int maxMovementPoint;
    private Province startProvince;
    // private boolean attack; // true is attack, false is defend

    public Army() {
        this.armyUnitList = new ArrayList<>();
        this.startProvince = null;
    }


    /**
     * Create an army
     * @param units
     * @param province
     * @return
     */
    public static Army createArmy(ArrayList<Unit> units, Province province) {
        Army newArmy = new Army();
        newArmy.setProvince(province);
        newArmy.setUnits(units);
        return newArmy;
    }
    /**
     * Add a list of units into the army
     * @param mergeList
     */
    public void addUnits(ArrayList<Unit> mergeList) {
        for (Unit m : mergeList) {
            boolean found = false;
            String mergeName = m.getUnitName();
            for (Unit u : this.armyUnitList) {
                if (u.getUnitName().equals(mergeName)) {
                    u.setNumTroops(u.getNumTroops() + m.getNumTroops());
                    u.setAttackedTrue();
                    found = true;
                }
            }
            if (!found) {
                Unit unitNew = new Unit(mergeName);
                unitNew.setNumTroops(m.getNumTroops());
                unitNew.setAttackedTrue();
                this.armyUnitList.add(unitNew);
            }
        }
    }

    

    // getters and setters

    public void setProvince(Province province) {
        this.startProvince = province;
    }


    public void setUnits(ArrayList<Unit> units) {

        // make a copy of the units into army
        this.armyUnitList = new ArrayList<Unit>(units);
        // remove these units from their province
        this.startProvince.removeUnitList(units);
        // set attacked to true

    }


    public ArrayList<Unit> getUnits() {
        return this.armyUnitList;
    }

    public int getTotalUnits() {
        int answer = 0;
        for (Unit u : armyUnitList) {
            answer = answer + u.getNumTroops();
        }
        return answer;
    }

    public int getTotalAttack() {
        int attack = 0;
        for (Unit u : getUnits()) {
            attack = u.getAttack();
        }
        return attack;
    }

    public int getTotalDefence() {
        int defence = 0;
        for (Unit u : getUnits()) {
            defence = defence + u.getShieldDefense();
        }
        return defence;
    }

    public void loseUnits(int numTroopsLost) {
        Unit u;
        Random r = new Random();
        while (numTroopsLost > 0) {
            u = armyUnitList.remove(r.nextInt(armyUnitList.size()));
            u.lostUnits(numTroopsLost);
            numTroopsLost--;
        }
    }


    public boolean unitsIsEmpty() {
        return (armyUnitList.size() == 0);
    }

    public Province getStartProvince() {
        return this.startProvince;
    }

    public void addUnit(Unit unit) {
        this.armyUnitList.add(unit);
    }

    public void returnUnitsToProvince(ArrayList<Unit> units) {
        for (Unit u : units) {
            this.startProvince.addUnit(u);
        }
    }

    public static void main(String[] args) throws IOException {
        

        GameScreenController.folderName = "testGame1";
        // Path path = Paths.get("src/unsw/gloriaromanus/gamedata/"+ folderName);
        String gameFolder = "testGame1";
        // reset folderName for next selection
        String folderName = null;
        String firstFactionString = "Rome";
        String secondFactionString = "Gaul";

        // hashmap mapping current province allocation

        HashMap<String, ArrayList<String>> province_allocation = GameScreenController.generateNewProvince(gameFolder,
                firstFactionString, secondFactionString);

        HashMap<String, String> goals = GameScreenController.createGoals(gameFolder);
        System.out.println(goals);

        // set goals
        Game game = new Game(folderName, firstFactionString, secondFactionString, province_allocation, goals);
        //Player currentPlayer = game.getCurrentPlayer();

        Faction firstFaction = game.getPlayer1Faction();
        Faction secondFaction = game.getPlayer2Faction();

        Province province1 = firstFaction.getProvinces().get(0);
        Province province2 = secondFaction.getProvinces().get(0);


        // create some units and add into province
        Unit army1Unit1 = new Unit("chariots");
        province1.addUnit(army1Unit1);
        Unit army1Unit2 = new Unit("spearmen");
        province1.addUnit(army1Unit2);
        Unit army1Unit3 = new Unit("roman-legionary");
        province1.addUnit(army1Unit3);

        System.out.println("Province 1 Owner = " + province1.getFaction().getFactionName() + "\nProvince 1 Unit list before attack = " + province1.getUnits());

        Unit army2Unit1 = new Unit("elephant");
        province2.addUnit(army2Unit1);
        Unit army2Unit2 = new Unit("beserker");
        province2.addUnit(army2Unit2);
        Unit army2Unit3 = new Unit("druid");
        province2.addUnit(army2Unit3);
        System.out.println("Province 2 Owner = " + province2.getFaction().getFactionName() + "\nProvince 2 Unit list before attack = " + province2.getUnits());



        ArrayList<Unit> attackUnits = new ArrayList<Unit>();

        //attackUnits.add(province1.getUnits().get(0));


        // create army
        String winner = Faction.createArmyBattle(attackUnits, province1, province2);
        System.out.println("\n After attack \n");
        System.out.println("Winner = " + winner);
        System.out.println("Province 1 = " + province1.getProvinceName());
        System.out.println("Province 2 = " + province2.getProvinceName());
        System.out.println("Province 1 Owner = " + province1.getFaction().getFactionName() + "\nProvince 1 Unit list after attack = " + province1.getUnits());
        System.out.println("Province 2 Owner = " + province2.getFaction().getFactionName() + "\nProvince 2 Unit list after attack = " + province2.getUnits());

    }
}
