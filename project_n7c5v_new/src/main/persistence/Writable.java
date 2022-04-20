package persistence;

import org.json.JSONObject;

// Reference: JsonSerialization from CPSC 210 GitHub repository
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
