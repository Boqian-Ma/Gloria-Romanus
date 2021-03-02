package unsw.gloriaromanus.classes;

public abstract class GoalComponent {
    public void add(GoalComponent specificGoalComponent) {
        throw new UnsupportedOperationException();
    }

    public void remove(GoalComponent specificGoalComponent) {
        throw new UnsupportedOperationException();
    }

    public boolean isSatisfiedCheck() {
        throw new UnsupportedOperationException();
    }
}
