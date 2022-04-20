package model;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.json.JSONObject;
import persistence.Writable;

import java.math.BigDecimal;
import java.math.MathContext;

// Represents a Normal Distribution having a mean and a standard deviation
public class NormalCalculator implements Writable {

    private double mean;
    private double sd;
    private NormalDistribution nd;
    private double point1;
    private double point2;
    private double probability;

    // Requires: sd > 0
    // Effects: Creates a Normal Distribution with given mean and sd
    public NormalCalculator(double mean,double sd) {
        normalDistribution(mean,sd);

    }

    public double getMean() {
        return mean;
    }

    public double getSd() {
        return sd;
    }

    public double getPoint1() {
        return point1;
    }

    public double getPoint2() {
        return point2;
    }

    // Modifies: This
    // Effects: Makes a new Normal Distribution using given mean and sd
    public void normalDistribution(double meanNorm, double sdNorm) {
        mean = meanNorm;
        sd = sdNorm;
        new NormalDistribution(mean,sd);
    }

    // Requires: pt1 < pt2
    // Modifies: this
    // Effects: Returns the area between pt1 and pt2
    public double normalProbability(double pt1, double pt2) {
        EventLog.getInstance().logEvent(new Event("Normal Probability Calculated"));
        point1 = pt1;
        point2 = pt2;
        nd = new NormalDistribution(mean,sd);
        Double d = nd.probability(pt1,pt2);
        BigDecimal bd = new BigDecimal(d);
        bd = bd.round(new MathContext(3));
        double dd = bd.doubleValue();
        return dd;
    }

    public void setProbability(double point1, double point2) {
        this.probability = normalProbability(point1,point2);
    }

    public double getProbability() {
        return probability;
    }

    @Override
    // Effects: Puts all aspects of Normal Calculator into a JSON object
    // Reference: JsonSerialization from CPSC 210 GitHub repository
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("mean", mean);
        json.put("standardDeviation", sd);
        json.put("point1", point1);
        json.put("point2", point2);
        json.put("probability", normalProbability(point1,point2));
        EventLog.getInstance().logEvent(new Event("Normal Probability Saved"));
        return json;
    }

    public void logLoadEventNC() {
        EventLog.getInstance().logEvent(new Event("Normal Probability Loaded"));
    }
}
