package unsw.gloriaromanus.classes;

import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONObject;

public class Province {

    private String provinceName;
    private ArrayList<Unit> units;
    private Faction faction;
    private TaxBehaviour taxBehaviour;
    private double wealth;
    private int currentRound;
    private ArrayList<TrainSlot> trainSlots;

    /**
     * Create a province object
     * @param owner
     * @param provinceName
     */
    public Province(Player owner, String provinceName) {
        this.units = new ArrayList<>();
        this.trainSlots = new ArrayList<>();
        // initialise two training slots
        addSlots(2, this.trainSlots, this);
        this.provinceName = provinceName;
        this.taxBehaviour = new Tax(0.10, 10);

        this.faction = owner.getFaction();
    }

    /**
     * Returns the data of this province
     * 1. owner faction
     * 2. tax rate
     * 3. wealth rate
     * 4. training slot availibility
     * 4. units
     * @return
     */
    public JSONObject saveGameProvince() {
        
        JSONObject provinceData = new JSONObject();

        provinceData.put("faction", this.faction.getFactionName());
        provinceData.put("tax_rate", this.taxBehaviour.getTaxRate());
        provinceData.put("wealth_rate", this.taxBehaviour.getWealth());

        // slots availibility 
        provinceData.put("train_slot_1", this.trainSlots.get(0).saveTrainSlotData());
        provinceData.put("train_slot_2", this.trainSlots.get(1).saveTrainSlotData());
        provinceData.put("units", this.getUnitsData());

        return provinceData;
    }

    /**
     * Given a list unit indicies, return a list of units
     * Prepare to attack 
     * @param unitIndicies
     * @return
     */
    public ArrayList<Unit> unitsFromIndecies(ArrayList<Integer> unitIndicies) {
        ArrayList<Unit> units = new ArrayList<>();
        for (int i = 0; i < unitIndicies.size(); i++) {
          int unitIndex = unitIndicies.get(i);
          if (this.units.get(unitIndex).getAttacked() == false) {
            units.add(this.units.get(unitIndex));
          }
          
        }
        return units;
    }

    /**
     * Combine all units data
     * @return
     */
    private JSONObject getUnitsData() {
        JSONObject unitsData = new JSONObject();
        for (Unit u : this.units) {
            unitsData.put(u.getUnitName(), u.saveUnitData());
        }
        return unitsData;
    }
    


    // Tax and wealth management
    public void setTaxBehaviour(double taxRate, double wealthRate) {
        this.taxBehaviour = new Tax(taxRate, wealthRate);
    }

    public void updateRound() {
        this.currentRound += 1;
    }

    /**
     * Update training slots after a round is completed
     */
    private void updateTrainingTime() {
        for (TrainSlot s : this.trainSlots) {
            s.updateRound();
        }
    }

    /**
     * Update Wealth after each round. Wealth is increased first then decreased
     * after taxing
     */
    public double upDateWealthTax() {
        // update training rounds
        updateTrainingTime();
        // Increase wealth first
        this.increaseWealth();
        // Tax and update wealth
        double tax = this.taxBehaviour.CalculateTax(this.wealth);
        if (this.wealth - tax < 0) {
            this.wealth = 0;
        } else {
            this.wealth -= tax;
        }
        return tax;
    }

    /**
     * Increase wealth after each turn
     */
    public void increaseWealth() {
        if (this.wealth + this.taxBehaviour.getWealth() >= 0) {
            this.wealth += this.taxBehaviour.getWealth();
        } else {
            this.wealth = 0;
        }
    }

    /**
     * Add training slots to a province
     * 
     * @param num
     * @param slots
     * @param p
     */
    public static void addSlots(int num, ArrayList<TrainSlot> slots, Province p) {
        for (int i = 0; i < num; i++) {
            slots.add(new TrainSlot(p));
        }
    }

    public ArrayList<TrainSlot> getTrainingSlots() {
        return this.trainSlots;
    }

    /**
     * Find the first available training slot
     * 
     * @param unit
     * @return True if there is one, false if not
     * @throws IOException
     */
    public Boolean performTrain(String unitString) {
        // loop through slots
        for (TrainSlot s : this.trainSlots) {
            // System.out.println(s.getOccupancy());
            if (!s.getOccupancy()) {
                // create unit object
                Unit unit = new Unit(unitString);
                return s.train(unit);
            }
        }
        return false;
    }
    
    /**
     * Check if there is a slot for training
     * @return true if there is a slot, otherwise false
     */
    public Boolean checkTrain() {
        for (TrainSlot s : this.trainSlots) {
            if (!s.getOccupancy()) {
                // create unit object
                return true;
            }
        }
        return false; 
    }

    /**
     * Reset movement points for all unit in this province
     * and attacked check
     */
    public void resetMovementPointsOfAllUnits() {
        for (Unit u : units) {
            u.resetMovementPoint();
            u.setAttackFalse();
        }
    }



    // getters and setters
    public void setOwnerFaction(Faction faction) {
        this.faction = null;
        this.faction = faction;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = new ArrayList<>(units);
        for (Unit unit : units) {
            unit.setProvince(this);
        }
    }

    // issue

    public void removeUnitList(ArrayList<Unit> units) {
        //ArrayList<Unit> newUnits = new ArrayList<Unit>(units);
        //System.out.println(units.equals(this.units));
        for (Unit u : units) {
            this.removeUnit(u);
        }
    }

    public String getProvinceName() {
        return this.provinceName;
    }

    public TaxBehaviour getTaxBehaviour() {
        return this.taxBehaviour;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public double getWealth() {
        return wealth;
    }

    /**
     * Change the province's owner Update the ownerFaction's list
     * 
     * @param newOwner
     */
    public void changeOwner(Faction newOwnerFaction) {
        this.stopTraining();
        // add this province to new owner
        newOwnerFaction.addProvince(this);
        // remove this province from old province
        this.faction.removeProvince(this);
        this.faction = newOwnerFaction;
        // cease all training
        // create a new arraylist for new owner
        this.units = new ArrayList<>();
    }

    /**
     * Stop all trianing in all training slots
     */
    public void stopTraining() {
        for (TrainSlot t : this.trainSlots) {
            // System.out.println("in stop trianing");
            t.stopTraining();
        }
    }

    
    public void addUnit(Unit unit){
        this.units.add(unit);
        unit.setProvince(this);
    }

    public void routedUnit(Unit unit) {
        this.units.add(unit);
    }

    public void removeUnit(Unit unit) {
        this.units.remove(unit);
    }
    public Faction getFaction() {
        return this.faction;
    }
}
