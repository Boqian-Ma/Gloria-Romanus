package unsw.gloriaromanus.classes;

import org.json.JSONObject;

public interface TrainBehaviour {
    // find the most suitable training slot
    public Boolean train(Unit unit);
    // update turn when a turn ends
    public void updateRound();
    public Boolean getOccupancy();
    public Unit getCurrentTraining();
    public JSONObject saveTrainSlotData();
}


