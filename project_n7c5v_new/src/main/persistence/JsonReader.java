package persistence;

import model.BinomialCalculator;
import model.NormalCalculator;
import model.RawData;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

// Represents a reader that reads workroom from JSON data stored in file
// Reference: JsonSerialization from CPSC 210 GitHub repository
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads binomial calculator from file and returns it;
    // throws IOException if an error occurs reading data from file
    public BinomialCalculator readBC() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseBinomialCalculator(jsonObject);
    }

    // EFFECTS: reads normal calculator from file and returns it;
    // throws IOException if an error occurs reading data from file
    public NormalCalculator readNC() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseNormalCalculator(jsonObject);
    }

    // EFFECTS: reads raw data from file and returns it;
    // throws IOException if an error occurs reading data from file
    public RawData readRD() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseRawData(jsonObject);
    }

    // EFFECTS: parses raw data from JSON object and returns it
    private RawData parseRawData(JSONObject jsonObject) {
        RawData rd;
        String listX = jsonObject.getString("listX");
        String listY = jsonObject.getString("listY");
        rd = new RawData(listX,listY);
        rd.logLoadEventRD();
        Double meanY = jsonObject.getDouble("mean Y");
        Double standardDeviationY = jsonObject.getDouble("standard deviation Y");
        return rd;
    }

    // EFFECTS: parses normal calculator from JSON object and returns it
    private NormalCalculator parseNormalCalculator(JSONObject jsonObject) {
        NormalCalculator nc;
        Double mean = jsonObject.getDouble("mean");
        Double standardDeviation = jsonObject.getDouble("standardDeviation");
        nc = new NormalCalculator(mean, standardDeviation);
        nc.logLoadEventNC();
        Double point1 = jsonObject.getDouble("point1");
        Double point2 = jsonObject.getDouble("point2");
        nc.setProbability(point1,point2);
        Double probability = jsonObject.getDouble("probability");

        return nc;
    }


    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses binomial calculator from JSON object and returns it
    private BinomialCalculator parseBinomialCalculator(JSONObject jsonObject) {
        BinomialCalculator bc;
        Double probabilityofsuccess = jsonObject.getDouble("probabilityOfSuccess");
        int trials = jsonObject.getInt("trials");
        bc = new BinomialCalculator(probabilityofsuccess, trials);
        bc.logLoadEventBC();
        int successes = jsonObject.getInt("successes");
        double probability = jsonObject.getDouble("probability");
        bc.setSuccesses(successes);
        bc.setProbability(successes);
        return bc;
    }




}
