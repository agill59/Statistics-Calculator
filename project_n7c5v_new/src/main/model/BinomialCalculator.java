package model;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.json.JSONObject;
import persistence.Writable;

import java.math.BigDecimal;
import java.math.MathContext;



// Represents a Binomial Distribution having a probability and a number of trials
public class BinomialCalculator implements Writable {

    private final double probabilityOfSuccess;
    private final int trials;
    private int successes;
    private double probability;



    public BinomialCalculator(double probabilityOfOneEvent, int numberOfTrials) {
        trials = numberOfTrials;
        probabilityOfSuccess = probabilityOfOneEvent;

    }

    public int getTrials() {
        return trials;
    }

    public double getProbabilityOfSuccess() {
        return probabilityOfSuccess;
    }

    public int getSuccesses() {
        return successes;
    }

    public void setSuccesses(int successes) {
        this.successes = successes;
    }

    // Requires: successes to be greater than or equal to 0
    // Modifies: This
    // Effects: Returns probability of successes with given trials and probability
    public Double probability(int successes) {
        EventLog.getInstance().logEvent(new Event("Binomial Probability Calculated"));
        this.successes = successes;
        BinomialDistribution biDist;
        double tempDouble;
        BigDecimal bd;
        double tempDouble2;

        biDist = new BinomialDistribution(trials,probabilityOfSuccess);
        tempDouble = (biDist.probability(successes));
        bd = new BigDecimal(tempDouble);
        bd = bd.round(new MathContext(3));
        tempDouble2 = bd.doubleValue();
        return tempDouble2;
    }

    public void setProbability(int successes) {
        this.probability = probability(successes);
    }

    public double getProbability() {
        return probability;
    }

    @Override
    // Effects: Puts all aspects of Binomial Calculator into a JSON object
    // Reference: JsonSerialization from CPSC 210 GitHub repository
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        EventLog.getInstance().logEvent(new Event("Binomial Probability Saved"));
        json.put("probabilityOfSuccess", probabilityOfSuccess);
        json.put("trials", trials);
        json.put("successes", successes);
        json.put("probability", getProbability());
        return json;
    }

    public void logLoadEventBC() {
        EventLog.getInstance().logEvent(new Event("Binomial Probability Loaded"));
    }

}
