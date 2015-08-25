package com.crankycode.android.mall;

import android.support.v4.app.Fragment;

/**
 * Created by zuyi on 8/25/2015.
 */
public class CameraActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CameraFragment();
    }
}
