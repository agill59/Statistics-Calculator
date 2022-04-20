package model;


import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JSONReaderTest {

    @Test
    void testReaderNonExistentFileBC() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            BinomialCalculator bc = reader.readBC();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderNonExistentFileNC() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            NormalCalculator nc = reader.readNC();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderNonExistentFileRD() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            RawData rd = reader.readRD();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderGeneralBinomialCalculator() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralBinomialCalculator.json");
        try {
            BinomialCalculator bc = reader.readBC();
            assertEquals(0.205, bc.getProbability());

        } catch (IOException e) {
            fail("exception caught");
        }
    }

    @Test
    void testReaderGeneralNormalCalculator() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralNormalCalculator.json");
        try {
            NormalCalculator nc = reader.readNC();
            assertEquals(0.683, nc.getProbability());

        } catch (IOException e) {
            fail("exception caught");
        }
    }

    @Test
    void testReaderGeneralRawData() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralRawData.json");
        try {
            RawData rd = reader.readRD();
            assertEquals(7.0711, rd.getStandardDevY());

        } catch (IOException e) {
            fail("exception caught");
        }
    }



}
