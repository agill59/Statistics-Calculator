package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BinomialTest {
    private BinomialCalculator binomialCalc;

    @BeforeEach
    void runBefore() {
        binomialCalc = new BinomialCalculator(0.5,10);
    }

    @Test
    void testConstructor() {
        assertEquals(0.5, binomialCalc.getProbabilityOfSuccess());
        assertEquals(10,binomialCalc.getTrials());
        assertFalse(0 == binomialCalc.getTrials());
        assertEquals(0,binomialCalc.getSuccesses());

    }


    @Test
    void testProbability() {
        assertEquals(0.117,binomialCalc.probability(3));
        assertEquals(0.000977,binomialCalc.probability(0));
        assertEquals(0,binomialCalc.probability(11));
    }


}