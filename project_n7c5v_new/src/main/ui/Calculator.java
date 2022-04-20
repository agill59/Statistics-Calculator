package ui;

import model.BinomialCalculator;
import model.NormalCalculator;
import model.RawData;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// Calculator Application, Reference: TellerApp and JsonSerialization project from CPSC 210 GitHub repository
public class Calculator {
    private BinomialCalculator bc;
    private NormalCalculator nc;
    private RawData rd;
    private Scanner input;
    private JsonWriter jsonWriterBC;
    private JsonReader jsonReaderBC;
    private JsonReader jsonReaderBaseBC;
    private JsonWriter jsonWriterNC;
    private JsonReader jsonReaderNC;
    private JsonReader jsonReaderBaseNC;
    private JsonWriter jsonWriterRD;
    private JsonReader jsonReaderRD;
    private JsonReader jsonReaderBaseRD;
    private static final String JSON_STOREBC = "./data/binomialCalculator.json";
    private static final String JSON_STORENC = "./data/normalCalculator.json";
    private static final String JSON_STORERD = "./data/rawData.json";
    private static final String JSON_STOREBaseBC = "./data/binomialBase.json";
    private static final String JSON_STOREBaseNC = "./data/normalBase.json";
    private static final String JSON_STOREBaseRD = "./data/rawBase.json";


    // Effects: runs the calculator application
    public Calculator() {
        jsonWriterBC = new JsonWriter(JSON_STOREBC);
        jsonWriterNC = new JsonWriter(JSON_STORENC);
        jsonWriterRD = new JsonWriter(JSON_STORERD);
        jsonReaderBC = new JsonReader(JSON_STOREBC);
        jsonReaderNC = new JsonReader(JSON_STORENC);
        jsonReaderRD = new JsonReader(JSON_STORERD);
        jsonReaderBaseBC = new JsonReader(JSON_STOREBaseBC);
        jsonReaderBaseNC = new JsonReader(JSON_STOREBaseNC);
        jsonReaderBaseRD = new JsonReader(JSON_STOREBaseRD);
        runCalculator();
    }

    public void runCalculator() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("b")) {
            binomialCalculator();
        } else if (command.equals("n")) {
            normalCalculator();
        } else if (command.equals("r")) {
            rawData();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes calculators
    private void init() {
        bc = new BinomialCalculator(0,0);
        nc = new NormalCalculator(0,1);
        rd = new RawData("0,1","1,2");
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tb -> binomial");
        System.out.println("\tn -> normal");
        System.out.println("\tr -> raw data");
        System.out.println("\tq -> quit");
    }

    // Modifies: this
    // Effects: starts the binomial calculator
    private void binomialCalculator() {
        BinomialCalculator selected = selectBinomialCalculator();

        System.out.println("Enter lb to load saved data or any key to start a new set");

        binomialCalculatorStartState(input.next());

        System.out.print("Enter probability of event occurring once,number of trials and number of successes");
        double probabilityOfSuccess = input.nextDouble();
        int trials = input.nextInt();
        int successes = input.nextInt();

        if (probabilityOfSuccess < 0.0) {
            System.out.println("invalid probability...\n");
        } else if (trials < 0.0) {
            System.out.println("Need more trials...\n");
        }

        selected = new BinomialCalculator(probabilityOfSuccess,trials);



        System.out.println(selected.probability(successes));

        System.out.println("\tsave -> save binomial calculator to file");

        if (input.next().equals("save")) {
            saveBinomialCalculator(selected);
        }
    }

    // MODIFIES: this
    // EFFECTS: starts the normal distribution calculator
    private void normalCalculator() {
        NormalCalculator selected = selectNormalCalculator();

        System.out.println("Enter ln to load saved data or any key to start a new set");

        normalCalculatorStartState(input.next());

        System.out.print("Enter mean and standard deviation");
        double mean = input.nextDouble();
        double standardDeviation = input.nextDouble();

        if (standardDeviation < 0.0) {
            System.out.println("Invalid standard deviation...\n");
        }

        selected = new NormalCalculator(mean,standardDeviation);


        System.out.println("Enter the 2 points for the area you which to calculate");
        double x1 = input.nextDouble();
        double x2 = input.nextDouble();

        System.out.println(selected.normalProbability(x1,x2));

        System.out.println("\tsave -> save normal calculator to file");

        if (input.next().equals("save")) {
            saveNormalCalculator(selected);
        }

    }

    // Modifies: This
    // Effects: starts the raw data calculator
    private void rawData() {
        RawData selected = selectRawData();

        System.out.println("Enter lr to load saved data or any key to start a new set");

        rawDataStartState(input.next());


        System.out.println("Enter your first list of values");
        String listX = input.next();
        System.out.println("Enter your second list of values");
        String listY = input.next();

        if (listX == "") {
            System.out.println("Invalid list...\n");
        } else if (listY == "") {
            System.out.println("Invalid list...\n");
        }

        selected = new RawData(listX,listY);

        System.out.println("type 1 to calculate mean or 0 for standard deviation?");

        if (input.nextInt() == 1) {
            System.out.println(selected.getMeanY());
        } else {
            System.out.println(selected.getStandardDevY());
        }

        System.out.println("\tsave -> save raw data to file");

        if (input.next().equals("save")) {
            saveRawData(selected);
        }


    }

    // EFFECTS: returns binomial calculator
    private BinomialCalculator selectBinomialCalculator() {
        return bc;
    }

    // EFFECTS: returns normal calculator
    private NormalCalculator selectNormalCalculator() {
        return nc;
    }

    // EFFECTS: returns raw data calculator
    private RawData selectRawData() {
        return rd;
    }

    // EFFECTS: saves the binomial calculator to file
    private void saveBinomialCalculator(BinomialCalculator bcTemp) {
        try {
            jsonWriterBC.open();
            jsonWriterBC.writeBC(bcTemp);
            jsonWriterBC.close();
            System.out.println("Saved " + bcTemp.getProbabilityOfSuccess() + " to " + JSON_STOREBC);
            System.out.println("Saved " + bcTemp.getTrials() + " to " + JSON_STOREBC);
            System.out.println("Saved " + bcTemp.getSuccesses() + " to " + JSON_STOREBC);
            System.out.println("Saved " + bcTemp.probability(bcTemp.getSuccesses()) + " to " + JSON_STOREBC);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STOREBC);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads binomial calculator from file
    private void loadBinomialCalculator() {
        try {
            bc = jsonReaderBC.readBC();
            System.out.println("Loaded " + bc.getProbabilityOfSuccess() + " from " + JSON_STOREBC);
            System.out.println("Loaded " + bc.getTrials() + " from " + JSON_STOREBC);
            System.out.println("Loaded " + bc.getSuccesses() + " from " + JSON_STOREBC);
            System.out.println("Loaded " + bc.getProbability() + " from " + JSON_STOREBC);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STOREBC);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads binomial calculator's base state from file
    private void loadBinomialCalculatorBase() {
        try {
            bc = jsonReaderBaseBC.readBC();
            System.out.println("Loaded " + bc.getProbabilityOfSuccess() + " from " + JSON_STOREBaseBC);
            System.out.println("Loaded " + bc.getTrials() + " from " + JSON_STOREBaseBC);
            System.out.println("Loaded " + bc.getSuccesses() + " from " + JSON_STOREBaseBC);
            System.out.println("Loaded " + bc.getProbability() + " from " + JSON_STOREBaseBC);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STOREBaseBC);
        }
    }

    // EFFECTS: saves the normal calculator to file
    private void saveNormalCalculator(NormalCalculator ncTemp) {
        try {
            jsonWriterNC.open();
            jsonWriterNC.writeNC(ncTemp);
            jsonWriterNC.close();
            System.out.println("Saved " + ncTemp.getMean() + " to " + JSON_STORENC);
            System.out.println("Saved " + ncTemp.getSd() + " to " + JSON_STORENC);
            System.out.println("Saved " + ncTemp.getPoint1() + " to " + JSON_STORENC);
            System.out.println("Saved " + ncTemp.getPoint2() + " to " + JSON_STORENC);
            System.out.println("Saved " + ncTemp.normalProbability(ncTemp.getPoint1(),ncTemp.getPoint2())
                    + " to " + JSON_STORENC);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORENC);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads normal calculator from file
    private void loadNormalCalculator() {
        try {
            nc = jsonReaderNC.readNC();
            System.out.println("Loaded " + nc.getMean() + " from " + JSON_STORENC);
            System.out.println("Loaded " + nc.getSd() + " from " + JSON_STORENC);
            System.out.println("Loaded " + nc.getPoint1() + " from " + JSON_STORENC);
            System.out.println("Loaded " + nc.getPoint2() + " from " + JSON_STORENC);
            System.out.println("Loaded " + nc.getProbability() + " to " + JSON_STORENC);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORENC);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads normal calculator's base state from file
    private void loadNormalCalculatorBase() {
        try {
            nc = jsonReaderBaseNC.readNC();
            System.out.println("Loaded " + nc.getMean() + " from " + JSON_STOREBaseNC);
            System.out.println("Loaded " + nc.getSd() + " from " + JSON_STOREBaseNC);
            System.out.println("Loaded " + nc.getPoint1() + " from " + JSON_STOREBaseNC);
            System.out.println("Loaded " + nc.getPoint2() + " from " + JSON_STOREBaseNC);
            System.out.println("Loaded " + nc.getProbability() + " to " + JSON_STOREBaseNC);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STOREBaseNC);
        }
    }

    // EFFECTS: saves the raw data to file
    private void saveRawData(RawData rdTemp) {
        try {
            jsonWriterRD.open();
            jsonWriterRD.writeRD(rdTemp);
            jsonWriterRD.close();
            System.out.println("Saved" + "listX to " + JSON_STORERD);
            System.out.println("Saved " + "rdTemp.getListY()" + " to " + JSON_STORERD);
            System.out.println("Saved " + rdTemp.getMeanY() + " to " + JSON_STORERD);
            System.out.println("Saved " + rdTemp.getStandardDevY() + " to " + JSON_STORERD);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORERD);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads raw data from file
    private void loadRawData() {
        try {
            rd = jsonReaderRD.readRD();
            System.out.println("Loaded " + rd.getListX() + " from " + JSON_STORERD);
            System.out.println("Loaded " + rd.getListY() + " from " + JSON_STORERD);
            System.out.println("Loaded " + rd.getMeanY() + " from " + JSON_STORERD);
            System.out.println("Loaded " + rd.getStandardDevY() + " from " + JSON_STORERD);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORERD);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads raw data's base state from file
    private void loadRawDataBase() {
        try {
            rd = jsonReaderBaseRD.readRD();
            System.out.println("Loaded " + rd.getListX() + " from " + JSON_STOREBaseRD);
            System.out.println("Loaded " + rd.getListY() + " from " + JSON_STOREBaseRD);
            System.out.println("Loaded " + rd.getMeanY() + " from " + JSON_STOREBaseRD);
            System.out.println("Loaded " + rd.getStandardDevY() + " from " + JSON_STOREBaseRD);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STOREBaseRD);
        }
    }

    // Effects: Determines whether to load a saved state or a base state
    private void binomialCalculatorStartState(String str) {
        if (str.equals("lb")) {
            loadBinomialCalculator();
        } else {
            loadBinomialCalculatorBase();
        }
    }

    // Effects: Determines whether to load a saved state or a base state
    private void normalCalculatorStartState(String str) {
        if (str.equals("ln")) {
            loadNormalCalculator();
        } else {
            loadNormalCalculatorBase();
        }
    }

    // Effects: Determines whether to load a saved state or a base state
    private void rawDataStartState(String str) {
        if (str.equals("lr")) {
            loadRawData();
        } else {
            loadRawDataBase();
        }
    }


}
