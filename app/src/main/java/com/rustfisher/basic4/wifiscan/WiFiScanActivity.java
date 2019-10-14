package com.rustfisher.basic4.wifiscan;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.widget.Toast;

import com.rustfisher.basic4.R;

/**
 * 在这里申请权限
 */
public class WiFiScanActivity extends Activity {

    private static final int REQ_PER = 100;
    private static String[] mWiFiPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_container);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, WifiListFragment.newFrag()).commit();
        }
        if (PackageManager.PERMISSION_GRANTED !=
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            ActivityCompat.requestPermissions(this, mWiFiPermissions, REQ_PER);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_PER) {
            if (PackageManager.PERMISSION_GRANTED !=
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast.makeText(this, "获得定位权限才能搜索WiFi", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
