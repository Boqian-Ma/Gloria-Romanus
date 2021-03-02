package unsw.gloriaromanus.classes;

import java.util.ArrayList;

public abstract class RoundSubject {
    private ArrayList<RoundObserver> roundObservers;

    public RoundSubject(){
        this.roundObservers = new ArrayList<>();
    }

    public void attach(RoundObserver o) {
        this.roundObservers.add(o);
    }
    public void dettach(RoundObserver o) {
        this.roundObservers.remove(o);
    }
    abstract public void notifyRoundObservers();
    // getters and setters
    public ArrayList<RoundObserver> getRoundObservers() {
        return roundObservers;
    }
}
