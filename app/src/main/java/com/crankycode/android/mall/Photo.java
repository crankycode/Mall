package com.crankycode.android.mall;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by crankyfish on 28/8/15.
 */
public class Photo {
    private static final String JSON_FILENAME = "filename";

    private String mFilename;

    /** Create a representation of the file on photo **/
    public Photo(String filename) {
        mFilename = filename;
    }

    // For json serialization when saving and loading the property type of Photo
    public Photo(JSONObject json) throws JSONException {
        mFilename = json.getString(JSON_FILENAME);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFilename);
        return json;
    }

    public String getFilename() {
        return mFilename;
    }

}
