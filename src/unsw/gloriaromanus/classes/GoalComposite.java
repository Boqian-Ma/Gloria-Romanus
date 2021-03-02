package unsw.gloriaromanus.classes;

import java.util.ArrayList;

public class GoalComposite extends GoalComponent {
    private ArrayList<GoalComponent> children;
    private String type;

    private Faction faction;
    private Boolean isSatisfied;

    /**
     * Goal Composite
     * @param type
     * @param faction
     */
    public GoalComposite(String type, Faction faction) {
        super();
        this.children = new ArrayList<GoalComponent>();
        this.type = type;
        this.faction = faction;
        this.isSatisfied = false;
    }
    public ArrayList<GoalComponent> getChildren() {
        return this.children;
    }
    /**
     * Adds a new goal component
     * 
     * @param g
     */
    @Override
    public void add(GoalComponent g) {
        this.children.add(g);
    }

    /**
     * Removes a goal component
     * 
     * @param g
     */
    @Override
    public void remove(GoalComponent g) {
        this.children.remove(g);
    }
    /**
     * Check if the goals have been satisfied based
     */
    @Override
    public boolean isSatisfiedCheck() {
        if (this.type.equals("or")) {
            for (GoalComponent g : children) {
                if (g.isSatisfiedCheck()){
                    this.isSatisfied = true;
                    return true;
                }
            }
        } else {
            ArrayList<Integer> check = new ArrayList<>();
            for (GoalComponent g : children) {
                if (g.isSatisfiedCheck()){
                    check.add(1);
                }
            }
            // if all children are satisfied
            if (check.size() == children.size()) {
                this.isSatisfied = true;
                return true;
            }
        }
        return false;
    }
}
