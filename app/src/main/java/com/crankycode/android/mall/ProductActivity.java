package com.crankycode.android.mall;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import java.util.UUID;


public class ProductActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        UUID productId = (UUID)getIntent()
                .getSerializableExtra(ProductFragment.EXTRA_PRODUCT_ID);
        this.setTitle("Mall");

        return ProductFragment.newInstance(productId);

    }


}
