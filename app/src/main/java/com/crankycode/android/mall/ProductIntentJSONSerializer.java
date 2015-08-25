package com.crankycode.android.mall;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by zuyi on 7/16/2015.
 */
public class ProductIntentJSONSerializer {
    private Context mContext;
    private String mFilename;

    public ProductIntentJSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    public ArrayList<Product> loadProducts() throws IOException, JSONException {
        ArrayList<Product> products = new ArrayList<Product>();
        BufferedReader reader = null;
        try {
            // Open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                // Line breaks are omitter and irrelevant
                jsonString.append(line);
            }
            // Parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            // Build the array of crimes from JSONObjects
            for (int i = 0; i < array.length(); i++) {
                products.add(new Product(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            // Ihnore this one; it happens when starting fresh
        } finally {
            if (reader != null)
                    reader.close();
        }
        return products;
    }

    public void saveProducts(ArrayList<Product> products) throws JSONException, IOException {
        JSONArray array = new JSONArray();
        for (Product p : products)
            array.put(p.toJSON());

        // Write the file to disk
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }

    }
}
