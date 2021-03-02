package unsw.gloriaromanus.classes;

import java.util.ArrayList;
import java.util.Random;

public class SkirmishV2 {
    
    private ArrayList<Unit> attackUnits;
    private ArrayList<Unit> defendUnits;
    private BattleV2 battle;
   // private Province winner;

    public SkirmishV2(BattleV2 battle) {
        this.attackUnits = battle.getAttackingUnits();
        this.defendUnits = battle.getDefendingUnits();
        this.battle = battle;
    }

    /**
     * 
     * @pre units sizes > 0, number of engagement < 200
     */
    public void initiateSkirmish() {
        // select two random units from each army
        Unit attackUnit = getRandomUnit(this.attackUnits);
        Unit defendUnit = getRandomUnit(this.defendUnits);

        // send units to engage, some might come back later
        this.attackUnits.remove(attackUnit);
        this.defendUnits.remove(defendUnit);
        
        // create an engagement
        while (attackUnit.getNumTroops() > 0 && defendUnit.getNumTroops() > 0) {
            // select two random units 
            if (this.battle.getNumEngagement() == 200) {
                return;
            }
            // create an engagement with two units
            EngagementV2 newEngagement = new EngagementV2(attackUnit, defendUnit, this.battle, this);
            newEngagement.engage();

        }
    }   


    /**
     * Randomly select a unit from a list of units
     * 
     * @param units
     * @return a randomly selected unit;
     */
    private Unit getRandomUnit(ArrayList<Unit> units) {
        if (units.size() == 0) {
            return null;
        }
        int seed = 1;
        Random rand = new Random(seed);
        int attack_random_int = rand.nextInt(units.size());
        Unit chosenUnit = units.get(attack_random_int);
        // remove chosen one and send it to engagement
        units.remove(attack_random_int);
        return chosenUnit;
    }

}
