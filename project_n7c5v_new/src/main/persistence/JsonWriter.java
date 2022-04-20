package persistence;

import model.BinomialCalculator;
import model.NormalCalculator;
import model.RawData;
import org.json.JSONObject;


import java.io.*;

// Represents a writer that writes JSON representation of each calculator to separate file
// Reference: JsonSerialization from CPSC 210 GitHub repository
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of binomial calculator to file
    public void writeBC(BinomialCalculator bc) {
        JSONObject json = bc.toJson();
        saveToFile(json.toString(TAB));
    }


    // MODIFIES: this
    // EFFECTS: writes JSON representation of normal calculator to file
    public void writeNC(NormalCalculator nc) {
        JSONObject json = nc.toJson();
        saveToFile(json.toString(TAB));
    }


    // MODIFIES: this
    // EFFECTS: writes JSON representation of raw data to file
    public void writeRD(RawData rd) {
        JSONObject json = rd.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
