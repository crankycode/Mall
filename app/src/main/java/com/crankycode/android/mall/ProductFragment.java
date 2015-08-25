package com.crankycode.android.mall;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;


/**
 * Created by zuyi on 7/3/2015.
 */
public class ProductFragment extends Fragment {
    public static String EXTRA_PRODUCT_ID = "com.crankycode.android.mall.product_id";
    public static final String IP_ADDRESS = "com.crankycode.android.mall.IP_ADDRESS";

    final int MAX_COL = 9;
    final int MAX_NO_OF_IMG = 3;
    final int LAST_COL = 6;
    final int MAX_SPAN = 6;
    final float INITIAL_SPACE_WEIGHT = 12f;
    float spaceWeight = 8f;
    int col = 0;
    int row = 0;
    int curNumImg = 0;

    OkHttpClient client;
    Gson jsonConvertor;
    RestAdapter restAdapter;

    private Product mProduct;
    private EditText mProductName;
    private EditText mUserName;
    private CheckBox mSoldCheckBox;
    private GridLayout mImageGridLayout;
    private ImageButton mAddImageButton;
    private EditText mStock;
    private EditText mPrice;
    private EditText mBrand;
    private Space mSpaceWeight;

    //    AerospikeClient aeroClient = new AerospikeClient("192.168.1.33", 3000);
    private Button mDoneButton;
    private Spinner mConditionSpinner;
    private Spinner mConditionSpinner2;
    private Spinner mSpinnerCategory;
    private ArrayAdapter<String> mAdapterCategorySpinner;
    private String[] categoryList = {"Women's Apparel", "Men's Apparel", "Accessories", "Watches", "Bags", "Cosmetics & Fragrances"};

    public static ProductFragment newInstance(UUID productId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PRODUCT_ID, productId);
        ProductFragment fragment = new ProductFragment();

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID productId = (UUID) getArguments().getSerializable(EXTRA_PRODUCT_ID);
        mProduct = ProductLab.get(getActivity()).getProduct(productId);
        ApplicationInfo info = null;

        // Get the ipaddress from the menifest file
        try {
            info = getActivity().getPackageManager().getApplicationInfo(getActivity().getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("ProductFragment", "Error: IP ADDRESS NOT FOUND" + e + " Packetname: " + getActivity().getPackageName());
        }

        // init OkHttpClient
        client = new OkHttpClient();
        // Set the Date format for Retrofit
        jsonConvertor = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        // init retrofit
        restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint((String) info.metaData.get(IP_ADDRESS))
                .setClient(new OkClient(new OkHttpClient())).setConverter(new GsonConverter(jsonConvertor)).build();
        MallCreateProductService service = restAdapter.create(MallCreateProductService.class);
        retrofit.Callback callback = new retrofit.Callback() {


            @Override
            public void success(Object o, retrofit.client.Response response) {
                // Try to get response body
                BufferedReader reader = null;
                StringBuilder sb = new StringBuilder();
                String result = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String line;

                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    result = sb.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                List<retrofit.client.Header> responseHeaders = response.getHeaders();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.d("ProductFragment", (responseHeaders.get(i).getName() + ": " + responseHeaders.get(i).getValue()));
                }
                Log.d("ProductFragment: Body", result);
            }

            @Override
            public void failure(RetrofitError e) {
                e.printStackTrace();
            }

        };


    }

    @Override
    public void onPause() {
        super.onPause();
        // Save the data when its onPause
        ProductLab.get(getActivity()).saveProducts();
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        final int marginTopBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        final int uiHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65, getResources().getDisplayMetrics());
        final int spaceWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        final int category_dropdown_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 290, getResources().getDisplayMetrics());
        int imageWidthHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, getResources().getDisplayMetrics());

        View v = inflater.inflate(R.layout.fragment_product, parent, false);


        mProductName = (EditText) v.findViewById(R.id.product_name);
        mProductName.setText(mProduct.getProductName());
        mProductName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mProduct.setProductName(c.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mUserName = (EditText) v.findViewById(R.id.product_user_id);
        mUserName.setText(mProduct.getUserId());
        mUserName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mProduct.setUserId(c.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        // Set mStock
        mStock = (EditText) v.findViewById(R.id.product_stock);
        mStock.setText(Integer.toString(mProduct.getStock()));
        mStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if (c.length() != 0)
                    mProduct.setStock(Integer.parseInt(c.toString()));
                else
                    mProduct.setStock(0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Set mPrice
        mPrice = (EditText) v.findViewById(R.id.product_price);
        mPrice.setText(Double.toString(mProduct.getPrice()));
        mPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if (c.length() != 0)
                    mProduct.setPrice(Double.parseDouble(c.toString()));
                else
                    mProduct.setPrice(0.0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // set mBrand
        mBrand = (EditText) v.findViewById(R.id.product_brand);
        mBrand.setText(mProduct.getBrand());
        mBrand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mProduct.setBrand(c.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // Done button press
        mDoneButton = (Button) v.findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("productFragment", "button press:");
                MallCreateProductService service = restAdapter.create(MallCreateProductService.class);

                // sending real mall product data over to server
                InsertMallProduct insertMallProduct = new InsertMallProduct(mProduct.getId().toString(),
                        mProduct.getDate(), mProduct.isSold(), mProduct.getProductName(),
                        mProduct.getUserId(), mProduct.getPrice(), mProduct.getStock(), mProduct.getBrand(), mProduct.getCategory()[0],
                        mProduct.getCondition()[0]);
                service.createMallProduct(mProduct.getId().toString(), insertMallProduct, new retrofit.Callback<InsertMallProduct>() {
                    @Override
                    public void success(InsertMallProduct insertMallProduct, Response response) {
                        Log.d("ProductFragment", "send mall product data success: " + mProduct.getId().toString());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("ProductFragment", "failure sending product mall data" + error.getResponse());
                    }
                });

                getActivity().finish();
            }
        });

        // Set Date of product creation
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        mProduct.setDate(new Date(currentDateTimeString));

        mSoldCheckBox = (CheckBox) v.findViewById(R.id.product_sold_checkbox);
        mSoldCheckBox.setChecked(mProduct.isSold());
        mSoldCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mProduct.setSold(mSoldCheckBox.isChecked());
            }
        });

        // Condition Spinner
        mConditionSpinner = (Spinner) v.findViewById(R.id.spinner_condition);
        mConditionSpinner.setDropDownVerticalOffset(uiHeight);
//     creates a new arrayAdapter from external resources

        ArrayAdapter<CharSequence> condtionAdapter = ArrayAdapter.createFromResource(v.getContext(), R.array.condition_array, R.layout.spinner_condition_item_layout);
//      Specify the layout to use when the list of choices appears
        condtionAdapter.setDropDownViewResource(R.layout.spinner_condition_item_layout);

//      Apply the adapter to the spinner
        mConditionSpinner.setAdapter(condtionAdapter);
        // Set the value for ConditionSpinner
        mConditionSpinner.setSelection(Integer.parseInt(mProduct.getCondition()[1]));

        mConditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mProduct.setCondition((String) mConditionSpinner.getSelectedItem().toString(), position);
                Log.d("ProductFragment", (String) mConditionSpinner.getSelectedItem().toString() + " pos: " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Category spinner

        ArrayList<ItemData> list = new ArrayList<ItemData>();
        list.add(new ItemData("Category", R.drawable.category_icon));
        list.add(new ItemData("T - Shirt", R.drawable.t_shirt_50));
        list.add(new ItemData("Shoe", R.drawable.womenshoefilled_50));
        list.add(new ItemData("Bag", R.drawable.briefcase_50));

        mSpinnerCategory = (Spinner) v.findViewById(R.id.product_category);
        mSpinnerCategory.setDropDownVerticalOffset((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65, getResources().getDisplayMetrics()));

        SpinnerAdapter categoryAdapter = new SpinnerAdapter(getActivity(), R.layout.spinner_layout, R.id.txt, list);
        categoryAdapter.setDropDownViewResource(R.layout.spinner_layout);
        mSpinnerCategory.setDropDownWidth(category_dropdown_width);
        mSpinnerCategory.setAdapter(categoryAdapter);
        mSpinnerCategory.setSelection(Integer.parseInt(mProduct.getCategory()[1]));
        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selected = (TextView) view.findViewById(R.id.txt);
                mProduct.setCategory(selected.getText().toString(), position);
                mSpinnerCategory.setPadding(0, 0, 0, 0);

                Log.d("ProductFragment", selected.getText().toString() + " pos: " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Gridlayout
        mImageGridLayout = (GridLayout) v.findViewById(R.id.imageGridLayout);
        mImageGridLayout.setUseDefaultMargins(false);

        // add ImageButton
        mAddImageButton = (ImageButton) v.findViewById(R.id.addImageButton);
        mAddImageButton.setImageResource(R.drawable.dash_plus_final);

        GridLayout.LayoutParams newImageParams = new GridLayout.LayoutParams();
        newImageParams.width = 0;
        newImageParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        if (row == 0)
            newImageParams.topMargin = marginTopBottom;

        newImageParams.bottomMargin = marginTopBottom;
        newImageParams.columnSpec = GridLayout.spec(0, 3f);
        newImageParams.setGravity(Gravity.FILL);
        newImageParams.rowSpec = GridLayout.spec(row);

        mAddImageButton.setLayoutParams(newImageParams);
        mAddImageButton.setBackgroundDrawable(null);
        mAddImageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mAddImageButton.setAdjustViewBounds(true);
        mAddImageButton.setPadding(0, 0, 0, 0);

        // add a space between imageButton
        Space mSpace1 = new Space(getActivity());
        GridLayout.LayoutParams mSpaceParams1 = new GridLayout.LayoutParams();
        mSpaceParams1.width = 0;
        mSpaceParams1.columnSpec = GridLayout.spec(1, 1f);
        mSpaceParams1.rowSpec = GridLayout.spec(row);
        mSpaceParams1.setGravity(Gravity.FILL);
        mSpace1.setLayoutParams(mSpaceParams1);
        mImageGridLayout.addView(mSpace1);

        // Adjustable Space
        mSpaceWeight = (Space) v.findViewById(R.id.spaceWeight);
        GridLayout.LayoutParams mSpaceParams = new GridLayout.LayoutParams();
        mSpaceParams.width = 0;
        mSpaceParams.columnSpec = GridLayout.spec(2, 11f);
        mSpaceParams.rowSpec = GridLayout.spec(row);
        mSpaceParams.setGravity(Gravity.FILL);
        mSpaceWeight.setLayoutParams(mSpaceParams);

        mAddImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CameraActivity.class);
                startActivity(i);
                int marginTopBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());

                if (curNumImg < MAX_NO_OF_IMG) {

                    // Add New ImgButton1
                    ImageButton img_bt = new ImageButton(getActivity());
                    img_bt.setImageResource(R.drawable.product_thumb_nail);

                    GridLayout.LayoutParams newImageParams = new GridLayout.LayoutParams();
                    newImageParams.width = 0;
                    newImageParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    if (row == 0)
                        newImageParams.topMargin = marginTopBottom;
                    newImageParams.bottomMargin = marginTopBottom;
                    newImageParams.columnSpec = GridLayout.spec(col, 3f);
                    newImageParams.rowSpec = GridLayout.spec(row);
                    newImageParams.setGravity(Gravity.FILL);

                    img_bt.setLayoutParams(newImageParams);
                    img_bt.setBackgroundDrawable(null);
                    img_bt.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    img_bt.setAdjustViewBounds(true);
                    img_bt.setPadding(0, 0, 0, 0);
                    mImageGridLayout.addView(img_bt);

                    // Add New Space Between NewImage and Add Button
                    Space mSpace1 = new Space(getActivity());
                    GridLayout.LayoutParams mSpaceParams1 = new GridLayout.LayoutParams();
                    mSpaceParams1.width = 0;
                    mSpaceParams1.columnSpec = GridLayout.spec(col + 1, 1f);
                    mSpaceParams1.rowSpec = GridLayout.spec(row);
                    mSpaceParams1.setGravity(Gravity.FILL);
                    mSpace1.setLayoutParams(mSpaceParams1);
                    mImageGridLayout.addView(mSpace1);

                    //Adjust Add ImageButton
                    GridLayout.LayoutParams adjImageParams1 = new GridLayout.LayoutParams();
                    adjImageParams1.columnSpec = GridLayout.spec(col + 2, 3f);
                    adjImageParams1.rowSpec = GridLayout.spec(row);
                    adjImageParams1.width = 0;
                    adjImageParams1.setGravity(Gravity.FILL);
                    mAddImageButton = (ImageButton) v.findViewById(R.id.addImageButton);
                    mAddImageButton.setLayoutParams(adjImageParams1);

                    // Adjustable Space
                    GridLayout.LayoutParams mSpaceParams = new GridLayout.LayoutParams();
                    mSpaceParams.width = 0;
//                    Log.d("ProductFragment", "Col Count: " + col + 3);
                    mSpaceParams.columnSpec = GridLayout.spec(col + 3, MAX_SPAN - (col + 2), spaceWeight);
                    mSpaceParams.rowSpec = GridLayout.spec(row);
                    mSpaceParams.setGravity(Gravity.FILL);
                    mSpaceWeight.setLayoutParams(mSpaceParams);

                    spaceWeight -= 4f;
                    col += 2;
                    curNumImg += 1;
                } else {

                    Log.d("ProductFragment", "Else case: row:" + row + " col:" + LAST_COL);
                    // Add New ImgButton1
                    ImageButton img_bt = new ImageButton(getActivity());
                    img_bt.setImageResource(R.drawable.product_thumb_nail);

                    GridLayout.LayoutParams newImageParams = new GridLayout.LayoutParams();
                    newImageParams.width = 0;
                    newImageParams.height = GridLayout.LayoutParams.WRAP_CONTENT;

                    if (row == 0)
                        newImageParams.topMargin = marginTopBottom;

                    newImageParams.bottomMargin = marginTopBottom;
                    newImageParams.columnSpec = GridLayout.spec(LAST_COL, 3f);
                    newImageParams.setGravity(Gravity.FILL);
                    newImageParams.rowSpec = GridLayout.spec(row);

                    img_bt.setLayoutParams(newImageParams);
                    img_bt.setBackgroundDrawable(null);
                    img_bt.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    img_bt.setAdjustViewBounds(true);
                    img_bt.setPadding(0, 0, 0, 0);
                    mImageGridLayout.addView(img_bt);

                    // increase row by 1
                    row += 1;
                    // reset col to 0
                    col = 0;

                    //Adjust Add ImageButton
                    GridLayout.LayoutParams adjImageParams1 = new GridLayout.LayoutParams();
                    adjImageParams1.columnSpec = GridLayout.spec(col, 3f);
                    adjImageParams1.rowSpec = GridLayout.spec(row);
                    adjImageParams1.bottomMargin = marginTopBottom;
                    adjImageParams1.width = 0;
                    adjImageParams1.setGravity(Gravity.FILL);
                    mAddImageButton = (ImageButton) v.findViewById(R.id.addImageButton);
                    mAddImageButton.setLayoutParams(adjImageParams1);

                    // Adjustable Space
                    GridLayout.LayoutParams mSpaceParams = new GridLayout.LayoutParams();
                    mSpaceParams.width = 0;
//                    Log.d("ProductFragment", "Col Count: " + col + 3);
                    mSpaceParams.columnSpec = GridLayout.spec(col + 1, 6, INITIAL_SPACE_WEIGHT);
                    mSpaceParams.rowSpec = GridLayout.spec(row);

                    mSpaceParams.setGravity(Gravity.FILL);
                    mSpaceWeight.setLayoutParams(mSpaceParams);

                    // reset num of image
                    curNumImg = 0;
                }

            }
        });

        return v;
    }
}
