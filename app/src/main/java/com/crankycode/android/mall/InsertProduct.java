package com.crankycode.android.mall;

/**
 * Created by zuyi on 7/17/2015.
 */

public class InsertProduct {
    Long ean;
    String name;
    String description;

    public InsertProduct() {}

    public InsertProduct(long ean, String name, String description) {
        this.ean = ean;
        this.name = name;
        this.description = description;
    }
}
