package com.crankycode.android.mall;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by zuyi on 7/8/2015.
 */
public class ProductPagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ArrayList<Product> mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mProduct = ProductLab.get(this).getProducts();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int pos) {

                Product product = mProduct.get(pos);
                return ProductFragment.newInstance(product.getId());
            }

            @Override
            public int getCount() {
                return mProduct.size();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageSelected(int position) {
                Product product = mProduct.get(position);
                if (product.getProductName() != null)
                    setTitle(product.getProductName());
            }

        });


        UUID productId = (UUID) getIntent()
                .getSerializableExtra(ProductFragment.EXTRA_PRODUCT_ID);
        for (int i = 0; i < mProduct.size(); i++) {
            if (mProduct.get(i).getId().equals(productId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
