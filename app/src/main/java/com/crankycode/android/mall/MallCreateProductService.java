package com.crankycode.android.mall;

import org.json.JSONObject;

import java.util.UUID;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by zuyi on 7/17/2015.
 */
public interface MallCreateProductService {
    @GET("/manualform/createform")
    void response(Callback<Response> cb);

    @PUT("/products/{ean}")
    void createProduct(@Path("ean") String ean,@Body InsertProduct insertProduct, Callback<InsertProduct> cb);

    @PUT("/products/{ean}")
    void createMallProduct(@Path("ean")String ean, @Body InsertMallProduct insertMallProduct, Callback<InsertMallProduct> cb);
}
