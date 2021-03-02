package unsw.gloriaromanus.classes;

import java.util.ArrayList;

public interface MoveBehaviour {
    /**
     * Move one unit to another 
     * @param from, province the troops are moving from
     * @param to, province the troops are moving to 
     * @param units, a list of troops moving
     * @param mvPoints, the number of movement points required to move 
     * @pre there must be a valid path between "from" and "to", and that the lowest movement points <= mvPoints
     */
    public void move(Province from, Province to, ArrayList<Unit> units, int mvPoints);
}
