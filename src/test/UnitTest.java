package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.gloriaromanus.classes.Unit;


import java.io.IOException;

public class UnitTest {
    @Test
    public void elephantInit() throws IOException {
        Unit tester = new Unit("elephant");
        assertEquals(10, tester.getNumTroops());
        assertEquals("infantry", tester.getType().get(0));
        assertEquals(1, tester.getRange());
        assertEquals(3, tester.getArmour());
        assertEquals(6, tester.getMorale());
        assertEquals(4, tester.getSpeed());
        assertEquals(85, tester.getAttack());
        assertEquals(15, tester.getDefenseSkill());
        assertEquals(5, tester.getShieldDefense());
        assertEquals(10, tester.getMaxMovementPoint());
        assertEquals(5, tester.getCost());
        assertEquals(2, tester.getTrainingTime());
    }

}
