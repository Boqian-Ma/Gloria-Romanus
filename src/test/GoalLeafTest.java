package test;


import unsw.gloriaromanus.classes.*;
import unsw.gloriaromanus.classes.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import unsw.gloriaromanus.*;

public class GoalLeafTest {
    @Test
    public void test1() {
        Player player = new Player();
        Faction faction = new Faction("faction", player);
        GoalLeaf tester = new GoalLeaf("treasury", faction);

    
        assertEquals("treasury",tester.getGoal());
        assertEquals(false, tester.isSatisfiedCheck());
        faction.setGold(100000);
        assertEquals(true, tester.isSatisfiedCheck());
        assertEquals(100000, tester.getCondition());

        tester.setGoal("wealth");
        assertEquals("wealth",tester.getGoal());
        assertEquals(false, tester.isSatisfiedCheck());
        faction.setTotalWealth(4000000);
        assertEquals(4000000, tester.getCondition());

        tester.setGoal("province");
        assertEquals("province",tester.getGoal());
        assertEquals(56, tester.getCondition());
        assertEquals(false, tester.isSatisfiedCheck());
    }
    
}