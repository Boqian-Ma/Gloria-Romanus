package unsw.gloriaromanus.classes;

import org.json.JSONObject;

public class TrainSlot implements TrainBehaviour {
    
    private Boolean occupancy;
    private Province province;
    private int currentTurn;
    private int endTurn;
    private Unit currentTraining;
    // need to observe owners 
    // if owner changes, 
    
    public TrainSlot(Province province) {
        this.currentTurn = 0;
        this.province = province;
        this.endTurn = 0;
        this.currentTraining = null;
        this.occupancy = false;
    }
    // trainig a unit


    /**
     * Train a unit
     * @param u
     * @return true if avaliable, false if not
     */
    @Override
    public Boolean train(Unit u) {
        if (this.occupancy == true) {
            // if no spot
            return false;
        } else {
            // available to train
            // if faction doesnt have enough money
            if ((this.province.getFaction().getGold() - u.getCost()) < 0) {

                return false;
            }
            this.occupancy = true;
            this.currentTraining = u;
            this.endTurn = u.getTrainingTime() + this.currentTurn;
            // reduce gold
            this.province.getFaction().reduceGold(u.getCost());
        }
        return true;
    }

    // Observing and update current turn whenever
    public void updateRound() {
        this.currentTurn += 1;
        // add to unit if finished and occupied
        if (this.currentTurn == this.endTurn && this.occupancy == true) {
            System.out.print("training "+this.currentTurn);
            this.currentTraining.setProvince(this.province);
            addUnit(this.currentTraining);
            this.currentTraining = null;
            this.occupancy = false;
        }
    }
    /**
     * Add a newly trained unit to the province unit list
     * @param u
     */
    private void addUnit(Unit u) {
        province.getUnits().add(u);
    }


    // getters and setters
    @Override
    public Boolean getOccupancy() {
        return occupancy;
    }

    /**
     * Stop traing and free up space
     */
    public void stopTraining() {
        this.occupancy = false;
        this.currentTraining = null;
        this.endTurn = this.currentTurn;
    }
    
    @Override
    public Unit getCurrentTraining() {
        return this.currentTraining;
    }

    @Override 
    public JSONObject saveTrainSlotData() {
        JSONObject slotData = new JSONObject();
        slotData.put("occupancy", this.occupancy);
        slotData.put("end_turn", this.endTurn);
        if (this.currentTraining != null) {
            slotData.put("current_training", this.currentTraining.getUnitName());
        } else {
            slotData.put("current_training", "null");
        }
        return slotData;
    }


}
