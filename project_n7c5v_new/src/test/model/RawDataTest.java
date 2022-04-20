package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RawDataTest {
    RawData rd;

    @BeforeEach
    void runBefore() {
        rd = new RawData("1.0,2.0,3.0","1.0,4.0,9.0");
    }

    @Test
    void testConstructor() {
        ArrayList<Double> yl = new ArrayList<Double>();
        yl.add(1.0);
        yl.add(4.0);
        yl.add(9.0);
        assertEquals(yl,rd.getListY());
        ArrayList<Double> xl = new ArrayList<Double>();
        xl.add(1.0);
        xl.add(2.0);
        xl.add(3.0);
        assertEquals(xl,rd.getListX());
    }

    @Test
    void testMeanY() {
        assertEquals(14.0/3.0,rd.getMeanY());
    }

    @Test
    void testsdY() {
        assertEquals(3.2998,rd.getStandardDevY());
    }

}
