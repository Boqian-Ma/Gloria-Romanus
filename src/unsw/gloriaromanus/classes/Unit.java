package unsw.gloriaromanus.classes;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONObject;

public class Unit {
    private int numTroops; // the number of troops in this unit (should reduce based on depletion)
    private int range; // range of the unit
    private int armour; // armour defense
    private int morale; // resistance to fleeing
    private int speed; // ability to disengage from disadvantageous battle
    private int attack; // can be either missile or melee attack to simplify. Could improve
                        // implementation by differentiating!
    private int defenseSkill; // skill to defend in battle. Does not protect from arrows!
    private int shieldDefense; // a shield
    private int cost;
    private int trainingTime;

    private int maxMovementPoint; // how many times they can move each turn
    private int currentMovementPoint; // How many mv points left in this turn
    private ArrayList<String> type; // type of troop
    private String unitName;
    private Province province;

    private boolean attacked; // flag to check if this unit has been moved of not this turn
    
    /**
     * Create a unit object
     * @param unitName
     */
    public Unit(String unitName) {
        this.attacked = false;
        JSONObject jsonContent = new JSONObject(
                "{\r\n    \"elephant\": {\r\n        \"cost\": 5,\r\n        \"trainingTime\": 2,\r\n        \"numTroops\": 10,\r\n        \"type\": [\"infantry\"],\r\n        \"abilities\": {\r\n                \"range\": 1,\r\n                \"armour\": 3,\r\n                \"morale\": 6,\r\n                \"speed\": 4,\r\n                \"attack\": 85,\r\n                \"defenseSkill\": 15,\r\n                \"shieldDefense\": 5\r\n        },\r\n        \"movementPoint\": 10\r\n    },\r\n    \"roman-legionary\": {\r\n        \"cost\": 5,\r\n        \"trainingTime\": 2,\r\n        \"numTroops\": 10,\r\n        \"type\": [\"infantry\"],\r\n        \"abilities\": {\r\n                \"range\": 1,\r\n                \"armour\": 3,\r\n                \"morale\": 6,\r\n                \"speed\": 8,\r\n                \"attack\": 100,\r\n                \"defenseSkill\": 10,\r\n                \"shieldDefense\": 5\r\n        },\r\n        \"movementPoint\": 10\r\n    },\r\n    \"beserker\" : {\r\n        \"cost\": 5,\r\n        \"trainingTime\": 2,\r\n        \"numTroops\": 10,\r\n        \"type\": [\"infantry\"],\r\n        \"abilities\": {\r\n            \"range\": 2,\r\n            \"armour\": 3,\r\n            \"morale\": 6,\r\n            \"speed\": 12,\r\n            \"attack\": 80,\r\n            \"defenseSkill\": 8,\r\n            \"shieldDefense\": 6\r\n        },\r\n        \"movementPoint\": 10\r\n    },\r\n    \"heavy-infantry\": {\r\n        \"numTroops\": 10,\r\n        \"cost\": 5,\r\n        \"trainingTime\": 2,\r\n        \"type\": [\"infantry\"],\r\n        \"abilities\": {\r\n            \"range\": 1,\r\n            \"armour\": 12,\r\n            \"morale\": 6,\r\n            \"speed\": 4,\r\n            \"attack\": 70,\r\n            \"defenseSkill\": 10,\r\n            \"shieldDefense\": 8\r\n        },\r\n        \"movementPoint\": 10\r\n\r\n    },\r\n    \"spearmen\" : {\r\n        \"numTroops\": 10,\r\n        \"cost\": 5,\r\n        \"trainingTime\": 2,\r\n        \"type\": [\"infantry\", \"ranged\"],\r\n        \"abilities\": {\r\n            \"range\": 5,\r\n            \"armour\": 1,\r\n            \"morale\": 6,\r\n            \"speed\": 5,\r\n            \"attack\": 70,\r\n            \"defenseSkill\": 2,\r\n            \"shieldDefense\": 3\r\n        },\r\n        \"movementPoint\": 10\r\n    },\r\n    \"missile-infantry\": {\r\n        \"cost\": 5,\r\n        \"trainingTime\": 2,\r\n        \"numTroops\": 10,\r\n        \"type\": [\"infantry\", \"ranged\"],\r\n        \"abilities\": {\r\n            \"range\": 5,\r\n            \"armour\": 1,\r\n            \"morale\": 6,\r\n            \"speed\": 5,\r\n            \"attack\": 70,\r\n            \"defenseSkill\": 2,\r\n            \"shieldDefense\": 3\r\n        },\r\n        \"movementPoint\": 10\r\n    },\r\n    \"melee-cavalry\": {\r\n        \"cost\": 5,\r\n        \"trainingTime\": 2,\r\n        \"numTroops\": 10,\r\n        \"type\": [\"cavalry\"],\r\n        \"abilities\": {\r\n            \"range\": 1,\r\n            \"armour\": 4,\r\n            \"morale\": 6,\r\n            \"speed\": 7,\r\n            \"attack\": 85,\r\n            \"defenseSkill\": 2,\r\n            \"shieldDefense\": 3\r\n        },\r\n        \"movementPoint\": 15\r\n    },\r\n    \"horse-archers\": {\r\n        \"cost\": 5,\r\n        \"trainingTime\": 2,\r\n        \"numTroops\": 10,\r\n        \"type\": [\"cavalry\", \"ranged\"],\r\n        \"abilities\": {\r\n            \"range\": 5,\r\n            \"armour\": 3,\r\n            \"morale\": 6,\r\n            \"speed\": 7,\r\n            \"attack\": 75,\r\n            \"defenseSkill\": 3,\r\n            \"shieldDefense\": 4\r\n        },\r\n        \"movementPoint\": 15\r\n    },\r\n    \"chariots\": {\r\n        \"cost\": 5,\r\n        \"trainingTime\": 2,\r\n        \"numTroops\": 10,\r\n        \"type\": [\"cavalry\"],\r\n        \"abilities\": {\r\n            \"range\": 1,\r\n            \"armour\": 8,\r\n            \"morale\": 6,\r\n            \"speed\": 3,\r\n            \"attack\": 60,\r\n            \"defenseSkill\": 7,\r\n            \"shieldDefense\": 6\r\n        },\r\n        \"movementPoint\": 15\r\n    },\r\n    \"artillery\": {\r\n        \"cost\": 5,\r\n        \"trainingTime\": 2,\r\n        \"numTroops\": 10,\r\n        \"type\": [\"ranged\"],\r\n        \"abilities\": {\r\n            \"range\": 1,\r\n            \"armour\": 4,\r\n            \"morale\": 6,\r\n            \"speed\": 7,\r\n            \"attack\": 85,\r\n            \"defenseSkill\": 2,\r\n            \"shieldDefense\": 3\r\n        },\r\n        \"movementPoint\": 4\r\n    },\r\n    \"javelin-skirmisher\": {\r\n        \"cost\": 5,\r\n        \"trainingTime\": 2,\r\n        \"numTroops\": 10,\r\n        \"type\": [\"ranged\"],\r\n        \"abilities\": {\r\n            \"range\": 5,\r\n            \"armour\": 3,\r\n            \"morale\": 6,\r\n            \"speed\": 4,\r\n            \"attack\": 70,\r\n            \"defenseSkill\": 2,\r\n            \"shieldDefense\": 3\r\n        },\r\n        \"movementPoint\": 4\r\n    },\r\n    \"druid\": {\r\n        \"cost\": 5,\r\n        \"trainingTime\": 2,\r\n        \"numTroops\": 10,\r\n        \"type\": [\"infantry\"],\r\n        \"abilities\": {\r\n            \"range\": 1,\r\n            \"armour\": 5,\r\n            \"morale\": 6,\r\n            \"speed\": 3,\r\n            \"attack\": 90,\r\n            \"defenseSkill\": 2,\r\n            \"shieldDefense\": 4\r\n        },\r\n        \"movementPoint\": 4\r\n    },\r\n    \"melee-infantry\": {\r\n        \"cost\": 5,\r\n        \"trainingTime\": 2,\r\n        \"numTroops\": 10,\r\n        \"type\": [\"infantry\"],\r\n        \"abilities\": {\r\n            \"range\": 1,\r\n            \"armour\": 5,\r\n            \"morale\": 6,\r\n            \"speed\": 3,\r\n            \"attack\": 80,\r\n            \"defenseSkill\": 3,\r\n            \"shieldDefense\": 3\r\n        },\r\n        \"movementPoint\": 10\r\n    },\r\n    \"pikemen\": {\r\n        \"cost\": 5,\r\n        \"trainingTime\": 2,\r\n        \"numTroops\": 10,\r\n        \"type\": [\"infantry\"],\r\n        \"abilities\": {\r\n            \"range\": 1,\r\n            \"armour\": 5,\r\n            \"morale\": 6,\r\n            \"speed\": 3,\r\n            \"attack\": 80,\r\n            \"defenseSkill\": 3,\r\n            \"shieldDefense\": 3\r\n        },\r\n        \"movementPoint\": 10\r\n    }\r\n}\r\n");
        jsonContent.keySet().forEach(keyStr -> {
            Object keyvalue = jsonContent.get(keyStr);
            if (keyStr.equals(unitName)) {
                this.unitName = unitName;
                JSONObject details = (JSONObject) keyvalue;

                // get number of troops per unit
                this.numTroops = (int) details.get("numTroops");
                // get types
                ArrayList<String> type = new ArrayList<String>();
                JSONArray jsonType = (JSONArray) details.get("type");
                for (int i = 0; i < jsonType.length(); i++) {
                    type.add(jsonType.getString(i));
                }
                this.type = type;

                this.maxMovementPoint = (int) details.get("movementPoint");
                this.currentMovementPoint = this.maxMovementPoint;
                JSONObject abilities = (JSONObject) details.get("abilities");
                this.range = (int) abilities.get("range");
                this.armour = (int) abilities.get("armour");
                this.morale = (int) abilities.get("morale");
                this.speed = (int) abilities.get("speed");
                this.attack = (int) abilities.get("attack");
                this.defenseSkill = (int) abilities.get("defenseSkill");
                this.shieldDefense = (int) abilities.get("shieldDefense");

                this.cost = (int) details.get("cost");
                this.trainingTime = (int) details.get("trainingTime");
            }
        });
    }

    /**
     * Save unit data
     * 1. num troops
     * 2. currentMvPoints
     * @return
     */
    public JSONObject saveUnitData() {
        JSONObject unitData = new JSONObject();
        unitData.put("province", this.province.getProvinceName());
        unitData.put("num_troops", this.numTroops);
        unitData.put("current_mv_points", this.currentMovementPoint);
        unitData.put("attacked", this.attacked);
        return unitData;
    }

    // getters and setters
    public void setAttackFalse() {
        this.attacked = false;
    }

    public boolean getAttacked() {
        return this.attacked;
    }

    public int getTrainingTime() {
        return this.trainingTime;
    }

    public int getCost() {
        return this.cost;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getAttack() {
        return this.attack;
    }

    public int getDefenseSkill() {
        return this.defenseSkill;
    }

    public int getShieldDefense() {
        return this.shieldDefense;
    }

    public int getNumTroops() {
        return this.numTroops;
    }

    public ArrayList<String> getType() {
        return this.type;
    }

    public int getMaxMovementPoint() {
        return this.maxMovementPoint;
    }

    public int getCurrentMovementPoint() {
        return this.currentMovementPoint;
    }

    public int getRange() {
        return this.range;
    }

    public int getMorale() {
        return this.morale;
    }

    public int getArmour() {
        return this.armour;
    }

    public String getUnitName() {
        return this.unitName;
    }

    public Province getProvince() {
        return this.province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public void setAttackedTrue() {
        this.attacked = true;
    }

    public void setNumTroops(int num) {
        this.numTroops = num;
    }

    /**
     * Reset attacked after the turn has finished.
     */
    public void resetAttacked() {
        this.attacked = false;
    }

    /**
     * Reset movement point after the turn has finished
     */
    public void resetMovementPoint() {
        this.currentMovementPoint = this.maxMovementPoint;
    }


    // /**
    //  * Once a unit is moved to a new province, we update its current province and
    //  * the available movement points it has
    //  * 
    //  * @param targetProvince
    //  * @param movementPointsDeductions
    //  */
    // public void moveUnittoNewProvince(Province targetProvince, int movementPointsDeductions) {
    //     // remove unit from previous unit list
    //     this.province.removeUnit(this);
    //     // add unit to new list
    //     addToCurrentProvinceUnits(targetProvince);
    //     // update movement points
    //     updateMovementPoints(movementPointsDeductions);
    // }

    /**
     * Deduct unit from total
     * 
     * @param num
     */
    public void lostUnits(int num) {
        this.numTroops -= num;
        if (this.numTroops < 0) {
            this.numTroops = 0;
        }
    }



    /**
     * Update movement points of this unit after it's been moved.
     * 
     * @param deductions
     */
    public void updateMovementPoints(int deductions) {
        this.currentMovementPoint -= deductions;
    }


    

    public static void main(String[] args) throws IOException {
        String filename = "UnitAbilities.json";
        Path pathToFile = Paths.get(filename);
        System.out.println(pathToFile);
        // String content =
        // Files.readString(Paths.get("src/unsw/gloriaromanus/classes/UnitAbilities.json"));
        // JSONObject jsonContent = new JSONObject(content);
        Unit tester = new Unit("elephant");
        System.out.println(tester.getNumTroops());
    }


}
