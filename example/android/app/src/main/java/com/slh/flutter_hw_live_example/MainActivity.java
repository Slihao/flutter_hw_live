package com.slh.flutter_hw_live_example;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import io.flutter.embedding.android.FlutterActivity;

public class MainActivity extends FlutterActivity {
    private static final int REQUEST_CAMERA_PERMISSION_CODE = 0;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION_CODE = 1;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)) {

            } else {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION_CODE);
            }
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)) {

            } else {
                requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION_CODE);
            }

            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //  m_permissionGranted = true;
            } else {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
            }
        }

    }
}
