package unsw.gloriaromanus.classes;

import java.util.ArrayList;

public class BattleV2 {
    
    private int numEngagement;

    private Army attackArmy;
    private Army defendArmy;

    //private ArrayList<SkirmishV2> skirmishes;

    public BattleV2(Army attackArmy, Army defendArmy) {
        this.attackArmy = attackArmy;
        this.defendArmy = defendArmy; 
        this.numEngagement = 0;
        //this.skirmishes = new ArrayList<>();
    }

    /**
     * 
     * @param attackArmy
     * @param defendArmy
     * @pre     there is a path between attackArmy and defend army
     * @return winning province
     */
    public static Province createBattle(Army attackArmy, Army defendArmy) {

        // create a battle between two army
        BattleV2 battle = new BattleV2(attackArmy, defendArmy);
        // create skirmishes until one unit is empty
        while (!attackArmy.unitsIsEmpty() && !defendArmy.unitsIsEmpty() && battle.getNumEngagement() < 200) {
            // continue creating skirmishes
            System.out.println("in skirmish *****");
            System.out.println("attack army  = " + attackArmy.getUnits());
            System.out.println("defend army = " + defendArmy.getUnits());
            System.out.println("attack province army  = " + attackArmy.getStartProvince().getUnits());
            System.out.println("defend province army = " + defendArmy.getStartProvince().getUnits());
            System.out.println("out skirmish *****");
            SkirmishV2 newSkirmish = new SkirmishV2(battle);
            // start a skirmish
            newSkirmish.initiateSkirmish();
        }

        if (battle.getNumEngagement() == 200) {
            return null;
        }

        // add units back to provinces
        if (attackArmy.unitsIsEmpty() && !defendArmy.unitsIsEmpty()){
            // if attack army is empty and attacking province isnt
            return defendArmy.getStartProvince();
        } else if (!attackArmy.unitsIsEmpty() && defendArmy.unitsIsEmpty()) {
            // if attack army is still alive and defend army is gone, i.e. province been over taken
            // move units to 
            return attackArmy.getStartProvince();
        } else {
            // both are empty
            // empy army

            return defendArmy.getStartProvince();
        }
    }

    public ArrayList<Unit> getAttackingUnits() {
        return this.attackArmy.getUnits();
    }

    public ArrayList<Unit> getDefendingUnits() {
        return this.defendArmy.getUnits();
    }
    
    public int getNumEngagement() {
        return this.numEngagement;
    }

    public void addNumEngagement() {
        this.numEngagement++;
    }

    public Province getAttackArmyProvince() {
        return this.attackArmy.getStartProvince();
    }

    public void addAttackUnit(Unit unit) {
        this.attackArmy.addUnit(unit);
    }

    public void addDefendUnit(Unit unit) {
        this.defendArmy.addUnit(unit);
    }
} 
