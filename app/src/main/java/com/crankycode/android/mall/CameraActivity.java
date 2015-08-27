package com.crankycode.android.mall;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by zuyi on 8/25/2015.
 */
public class CameraActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Hide the window title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Hide the status bar and other OS-level chrome
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

    }

    @Override
    protected Fragment createFragment() {
        return new CameraFragment();
    }
}
