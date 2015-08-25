package com.crankycode.android.mall;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.UUID;


import java.text.ParseException;
import java.util.Date;


/**
 * Created by zuyi on 7/5/2015.
 */
public class ProductLab {

    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "products.json";

    private ArrayList<Product> mProducts;
    private ProductIntentJSONSerializer mSerializer;

    private static ProductLab sProductLab;
    private Context mAppContext;

    private Random rand = new Random(2000);

    private Date dateGenerator(int start, int end) {

// Make a String that has a date in it, with MEDIUM date format
        // and SHORT time format.
        String dateString = "Nov 4, 2003 8:14 PM";

        // Get the default MEDIUM/SHORT DateFormat
        java.text.DateFormat format =
                java.text.DateFormat.getDateTimeInstance(
                        java.text.DateFormat.MEDIUM, java.text.DateFormat.SHORT);

        // Parse the date
        try {
            Date date = format.parse(dateString);
            return date;
        } catch (ParseException pe) {
            System.out.println("ERROR: could not parse date in string \"" +
                    dateString + "\"");
        }
        return null;
    }

    public static int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    private ProductLab(Context appContext) {
        mAppContext = appContext;
        mSerializer = new ProductIntentJSONSerializer(appContext, FILENAME);
        try {
            mProducts = mSerializer.loadProducts();
        } catch (Exception e) {
            mProducts = new ArrayList<Product>();
            Log.e(TAG, "Error loading crimes: ", e);
        }

    }

    public static ProductLab get(Context c) {
        if (sProductLab == null) {
            sProductLab = new ProductLab(c.getApplicationContext());
        }
        return sProductLab;
    }

    public ArrayList<Product> getProducts() {
        return mProducts;
    }


    public void addProduct(Product c) {
        mProducts.add(c);
    }

    public Product getProduct(UUID id) {
        for (Product p : mProducts) {
            if (p.getId().equals(id))
                return p;
        }
        return null;
    }

    public boolean saveProducts() {
        try {
            mSerializer.saveProducts(mProducts);
            Log.d(TAG, "products saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving Product: " + e);
            return false;
        }
    }

}
