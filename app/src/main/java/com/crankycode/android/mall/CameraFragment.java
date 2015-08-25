package com.crankycode.android.mall;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by zuyi on 8/25/2015.
 */
public class CameraFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "CameraFragment";

    private Camera mCamera;
    private SurfaceView mSurfaceView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_camera, parent, false);

        Button takePictureButton = (Button)v.findViewById(R.id.camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mSurfaceView = (SurfaceView)v.findViewById(R.id.camera_surfaceView);

        return v;
    }
}
