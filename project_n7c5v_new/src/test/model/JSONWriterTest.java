package model;

import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JSONWriterTest {

    @Test
    void testWriterInvalidFileBC() {
        try {
            BinomialCalculator bc = new BinomialCalculator(0.5,10);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterInvalidFileNC() {
        try {
            NormalCalculator nc = new NormalCalculator(0.5,10);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterInvalidFileRD() {
        try {
            RawData rd = new RawData("1,2","2,4");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }


    @Test
    void testWriterGeneralBinomialCalculator() {
        try {
            BinomialCalculator bc = new BinomialCalculator(0.5,10);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralBinomialCalculator.json");
            writer.open();
            writer.writeBC(bc);
            writer.close();

            JsonReader jsonReaderBC = new JsonReader("./data/testWriterGeneralBinomialCalculator.json");
            bc = jsonReaderBC.readBC();
            assertEquals(0.5, bc.getProbabilityOfSuccess());



        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralNormalCalculator() {
        try {
            NormalCalculator nc = new NormalCalculator(5,10);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralNormalCalculator.json");
            writer.open();
            writer.writeNC(nc);
            writer.close();

            JsonReader jsonReaderNC = new JsonReader("./data/testWriterGeneralNormalCalculator.json");
            nc = jsonReaderNC.readNC();
            assertEquals(5, nc.getMean());



        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralRawData() {
        try {
            RawData rd = new RawData("1,2,3,4,5","5,10,15,20,25");
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralRawData.json");
            writer.open();
            writer.writeRD(rd);
            writer.close();

            JsonReader jsonReaderRD = new JsonReader("./data/testWriterGeneralRawData.json");
            rd = jsonReaderRD.readRD();
            assertEquals(15, rd.getMeanY());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}
