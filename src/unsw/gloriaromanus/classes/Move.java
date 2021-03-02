package unsw.gloriaromanus.classes;

import java.util.ArrayList;

/**
 * Move some units from one province to another province
 * 
 */
public class Move implements MoveBehaviour {
    @Override
    public void move(Province from, Province to, ArrayList<Unit> units, int mvPointsDeduction) {
        // remove the units from province from 
        for (Unit u : units) {
            // remove unit from From province
            from.removeUnit(u);
            // add unit to To province
            to.addUnit(u);
            // deduct movement points
            u.updateMovementPoints(mvPointsDeduction);
        }
    }
}
