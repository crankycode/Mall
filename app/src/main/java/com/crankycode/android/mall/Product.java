package com.crankycode.android.mall;

import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by zuyi on 7/3/2015.
 */
public class Product {
    private static final String JSON_ID = "id";
    private static final String JSON_PRODUCT = "product";
    private static final String JSON_SOLD = "sold";
    private static final String JSON_CREATED = "created";
    private static final String JSON_PHOTO = "photo";

    private UUID mId;
    private Date mDate;
    private boolean mSold;
    private String mProductName;
    private String mUserId;
    private double mPrice;
    private int mStock;
    private int mShipOutIn;
    private String mBrand;
    private String[] mCondition = {"0", "0"};
    private String[] mCategory = {"0", "0"};
//    private Photo mPhoto;
    private ArrayList<Photo> mPhoto = new ArrayList<Photo>();

    public Date getDate() {
        return mDate;
    }

    public UUID getId() {
        return mId;
    }

    public String getUserId() {
        return mUserId;
    }

    public String getProductName() {
        return mProductName;
    }

    public double getPrice() {
        return mPrice;
    }

    public int getStock() {
        return mStock;
    }

    public int getShipOutIn() {
        return mShipOutIn;
    }

    public String[] getCondition() {
        return mCondition;
    }

    public String[] getCategory() {
        return mCategory;
    }

    public String getBrand() {
        return mBrand;
    }

//    public Photo getPhoto() {
//        return mPhoto;
//    }

    public Photo getPhoto(int id) {
        return mPhoto.get(id);
    }


    public void setDate(Date mDate) {

        this.mDate = mDate;
    }

    public void setPrice(double price) {
        this.mPrice = price;
    }

    public void setStock(int stock) {
        this.mStock = stock;
    }

    public void setShipOutIn(int shipOutIn) {
        this.mShipOutIn = shipOutIn;
    }

    public void setCondition(String val, int selection) {

        if (mCondition != null) {
            mCondition[0] = val;
            mCondition[1] = Integer.toString(selection);
        }
    }

    public void setCategory(String val, int selection) {
        if (mCategory != null) {
            mCategory[0] = val;
            mCategory[1] = Integer.toString(selection);
        }
    }

    public void setBrand(String brand) {
        this.mBrand = brand;
    }

    public boolean isSold() {
        return mSold;
    }

    public void setSold(boolean mSold) {
        this.mSold = mSold;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public void setProductName(String productName) {
        this.mProductName = productName;
    }
    public void setPhoto(Photo p) {
        mPhoto.add(p);
    }


    public Product() {
        // Generate unique identifier
        mCondition = new String[]{"0", "0"};
        mId = UUID.randomUUID();
    }

    public Product(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_PRODUCT)) {
            mProductName = json.getString(JSON_PRODUCT);
        }

        mSold = json.getBoolean(JSON_SOLD);
        mDate = new Date(json.getLong(JSON_CREATED));

        if (json.has(JSON_PHOTO)) {
            mPhoto.add(new Photo(json.getJSONObject(JSON_PHOTO)));
        }
    }

    @Override
    public String toString() {
        return mProductName;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_PRODUCT, mProductName);
        json.put(JSON_SOLD, mSold);
        json.put(JSON_CREATED, mDate.getTime());
        if(mPhoto != null) {
            json.put(JSON_PHOTO,mPhoto);
        }

        return json;
    }
}
