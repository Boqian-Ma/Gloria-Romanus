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

public class ArmyTest {
    @Test
    public void ArmyTest1() {
        Tax tax = new Tax(1.5, 10);
        assertEquals(1.5, tax.getTaxRate());
        assertEquals(10, tax.getWealthRate());
    }
    
}