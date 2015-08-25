package com.crankycode.android.mall;

import java.util.Date;
import java.util.UUID;

/**
 * Created by zuyi on 7/18/2015.
 */
public class InsertMallProduct {
    private String ean;
    private Date date;
    private boolean sold;
    private String productname;
    private String userid;
    private double price;
    private int stock;
    private String brand;
    private String category;
    private String condition;

    public InsertMallProduct() {
    }

    public InsertMallProduct(
            String ean,
            Date date,
            boolean sold,
            String productname,
            String userid,
            double price,
            int stock,
            String brand,
            String category,
            String condition) {

        this.ean = ean;
        this.date = date;
        this.sold = sold;
        this.productname = productname;
        this.userid = userid;
        this.price = price;
        this.stock = stock;
        this.brand = brand;
        this.category = category;
        this.condition = condition;
    }

}
