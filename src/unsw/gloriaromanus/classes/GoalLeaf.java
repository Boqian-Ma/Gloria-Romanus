package unsw.gloriaromanus.classes;


public class GoalLeaf extends GoalComponent {
    private String goal;
    private int condition;
    private Faction faction;
    public GoalLeaf(String goal, Faction faction){
        super();
        this.goal = goal;
        this.faction = faction;
        setCondition();
    }

    public String getGoal() {
        return this.goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
        setCondition();
    }

    private void setCondition() {
        if (this.goal.equals("treasury")){
            // find total gold
            this.condition = 100000;
        } else if (this.goal.equals("wealth")){
            // get total wealth
            this.condition = 4000000;
        } else {
            // get total number of conqurered prpvinces
            this.condition = 56;
        }
    }

    public int getCondition() {
        return this.condition;
    }

    @Override
    public boolean isSatisfiedCheck() {
        // get current check from faction
        int currentProgress = 0;
        if (this.goal.equals("treasury")){
            // find total gold
            currentProgress = this.faction.getGold();
        } else if (this.goal.equals("wealth")){
            // get total wealth
            currentProgress = this.faction.getWealth();
        } else {
            // get total number of conqurered prpvinces
            currentProgress = this.faction.getNumProvinces();
        }
        if (currentProgress >= this.condition) {
            return true;
        }
        return false;
    }


}
