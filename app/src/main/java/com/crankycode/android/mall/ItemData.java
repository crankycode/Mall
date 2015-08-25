package com.crankycode.android.mall;

/**
 * Created by zuyi on 7/14/2015.
 */
public class ItemData {
    String text;
    Integer imageId;
    public ItemData(String text, Integer imageId) {
        this.text = text;
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }


    public Integer getImageId() {
        return imageId;
    }

}
