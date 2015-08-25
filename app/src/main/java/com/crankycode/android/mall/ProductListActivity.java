package com.crankycode.android.mall;

import android.support.v4.app.Fragment;
import android.view.Menu;

/**
 * Created by zuyi on 7/6/2015.
 */
public class ProductListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ProductListFragment();
    }


}
