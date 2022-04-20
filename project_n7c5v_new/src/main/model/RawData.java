package model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javafx.beans.binding.ListExpression;
import org.json.JSONObject;

// Represents raw data calculator having 2 lists of doubles
public class RawData {

    private String strY;
    private String strX;
    private ArrayList<Double> listX;
    private ArrayList<Double> listY;
    private Double meanY;
    private Double sdY;

    // Effects: Creates 2 lists of doubles
    public RawData(String strX, String strY) {
        this.strX = strX;
        this.strY = strY;
        listX = new ArrayList<>();
        listY = new ArrayList<>();
        stringToList(strX, listX);
        stringToList(strY, listY);
        meanY();
        standardDevY();

    }

    public ArrayList<Double> getListX() {
        return listX;
    }

    public ArrayList<Double> getListY() {
        return listY;
    }

    // Requires: String cannot be empty
    // Modifies: This
    // Effects: Converts a string of numbers into a list of doubles
    public void stringToList(String string, ArrayList<Double> al) {
        String[] str = string.split(",");
        List<String> ls = new ArrayList<String>();
        ls = Arrays.asList(str);
        for (String s : ls) {
            al.add(Double.parseDouble(s));
        }
    }

    public String listToString(ArrayList<Double> listXY) {

        StringBuilder stringbuild = new StringBuilder();
        List<Double> list = listXY;
        Iterator<Double> iterator = list.iterator();
        while (iterator.hasNext()) {
            stringbuild.append(iterator.next());
            if (iterator.hasNext()) {
                stringbuild.append(",");
            }
        }
        return stringbuild.toString();

    }



    // Modifies: this
    // Effects: Produces the mean of the Y list
    private void meanY() {
        double sum = 0;
        for (Double d: listY) {
            sum = sum + d;
        }

        meanY = sum / listY.size();
    }

    public double getMeanY() {
        return meanY;
    }

    // Modifies: sdY
    // Effects: Produces the standard deviation of the Y list
    public void standardDevY() {
        EventLog.getInstance().logEvent(new Event("Raw Data Calculated"));
        sdY = 0.0;
        for (Double d: listY) {
            sdY = sdY + Math.pow((meanY - d),2);
        }
        sdY = sdY / listY.size();
        double tempsdY = Math.sqrt(sdY);
        BigDecimal bd = new BigDecimal(tempsdY);
        bd = bd.round(new MathContext(5));
        sdY = bd.doubleValue();
    }

    public double getStandardDevY() {
        return sdY;
    }

    // Effects: Puts all aspects of Raw Data into a JSON object
    // Reference: JsonSerialization from CPSC 210 GitHub repository
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("listX", listToString(listX));
        json.put("listY", listToString(listY));
        json.put("mean Y", meanY);
        json.put("standard deviation Y", sdY);
        EventLog.getInstance().logEvent(new Event("Raw Data Saved"));
        return json;
    }

    public void logLoadEventRD() {
        EventLog.getInstance().logEvent(new Event("Raw Data Loaded"));
    }

}
