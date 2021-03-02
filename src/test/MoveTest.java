package test;

import unsw.gloriaromanus.classes.*;
import unsw.gloriaromanus.classes.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;


public class MoveTest {
    @Test
    public void moveTest1() throws IOException {
        // create game
        // create two provinces
        Player player = new Player();
        Faction f = new Faction("Yeet", player);
        Province from = new Province(player, "Province 1");
        Province to = new Province(player, "Province 2");
        // create units 
        from.addUnit(new Unit("elephant"));

        assertEquals(1, from.getUnits().size());
        assertEquals(0, to.getUnits().size());
        ArrayList<Unit> units = new ArrayList<>(from.getUnits());
        
        // test movement points
        assertEquals(10, units.get(0).getCurrentMovementPoint());
        f.moveUnitsToAnotherProvince(from, to, units, 1);
        assertEquals(0, from.getUnits().size());
        assertEquals(1, to.getUnits().size());
        assertEquals(9, units.get(0).getCurrentMovementPoint());
    }
    
}