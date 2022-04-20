package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NormalTest {
    NormalCalculator nc;

    @BeforeEach
    void runBefore() {
        nc = new NormalCalculator(3,1);
    }

    @Test
    void testNormalCalculator() {
        assertEquals(3, nc.getMean());
        assertEquals(1,nc.getSd());
    }

    @Test
    void testGetMean() {
        assertEquals(3, nc.getMean());
    }

    @Test
    void testPoint1() {
        assertEquals(0,nc.getPoint1());
    }

    @Test
    void testPoint2() {
        assertEquals(0,nc.getPoint2());
    }

    @Test
    void testNormalProbability() {
        assertEquals(0.0214, nc.normalProbability(0,1));
        assertEquals(0,nc.normalProbability(0,0));
    }
}
