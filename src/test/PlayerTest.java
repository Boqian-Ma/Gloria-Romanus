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

public class PlayerTest {
    @Test
    public void playerTest1() {
        Player player = new Player();
        assertEquals(null, player.getGame());
        assertEquals(null, player.getFaction());
    }
    
}