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

public class GoalCompositeTest {
    @Test
    public void addRemovetest() {
        Player player = new Player();
        Faction faction = new Faction("faction", player);
        GoalComponent leaf1 = new GoalLeaf("treasury", faction);
        GoalComponent leaf2 = new GoalLeaf("wealth", faction);

        GoalComposite composite = new GoalComposite("and", faction);
        composite.add(leaf1);
        composite.add(leaf2);
        assertEquals(2, composite.getChildren().size());

        composite.remove(leaf1);
        assertEquals(1, composite.getChildren().size());

    }

    @Test
    public void satisfyANDtest() {
        Player player = new Player();
        Faction faction = new Faction("faction", player);
        GoalLeaf leaf1 = new GoalLeaf("treasury", faction);
        GoalLeaf leaf2 = new GoalLeaf("wealth", faction);

        GoalComposite composite = new GoalComposite("and", faction);
        composite.add(leaf1);
        composite.add(leaf2);
        assertEquals(2, composite.getChildren().size());
        assertEquals(false, composite.isSatisfiedCheck());
        faction.setGold(100000);
        assertEquals(false, composite.isSatisfiedCheck());
        faction.setTotalWealth(4000000);
        assertEquals(true, composite.isSatisfiedCheck());

    }
    
    @Test
    public void satisfyORtest() {
        Player player = new Player();
        Faction faction = new Faction("faction", player);
        GoalLeaf leaf1 = new GoalLeaf("treasury", faction);
        GoalLeaf leaf2 = new GoalLeaf("wealth", faction);

        GoalComposite composite = new GoalComposite("or", faction);
        composite.add(leaf1);
        composite.add(leaf2);
        assertEquals(2, composite.getChildren().size());
        assertEquals(false, composite.isSatisfiedCheck());
        faction.setGold(100000);
        assertEquals(true, composite.isSatisfiedCheck());
        faction.setTotalWealth(4000000);
        assertEquals(true, composite.isSatisfiedCheck());

    }
    
}