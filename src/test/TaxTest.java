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

public class TaxTest {
    @Test
    public void constructorTest() {
        Tax tax = new Tax(1.5, 10);
        assertEquals(1.5, tax.getTaxRate());
        assertEquals(10, tax.getWealthRate());
    }
    @Test
    public void calculatrTaxTest() {
        Tax tax = new Tax(0.1, 10);
        assertEquals(10, tax.CalculateTax(100));
    }




    
}