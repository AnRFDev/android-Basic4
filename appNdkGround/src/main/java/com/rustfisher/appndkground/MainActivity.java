package com.rustfisher.appndkground;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.rustfisher.appndkground.jni.ContextUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "rustApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        TextView pkgTv = findViewById(R.id.pkg_name_tv);
        TextView appSignTv = findViewById(R.id.app_sign_tv);
        TextView appPkgNameInListTv = findViewById(R.id.pkg_name_in_list);
        pkgTv.setText(ContextUtils.nGetPkgName(getApplicationContext()));

        String javaSignature = "Java";
        try {
            PackageInfo packageInfo =
                    getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            javaSignature = packageInfo.signatures[0].toCharsString();
            Log.d(TAG, String.format("packageInfo.signatures.length == %d", packageInfo.signatures.length));
            for (Signature s : packageInfo.signatures) {
                Log.d(TAG, "signatures: " + s.toCharsString());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        appSignTv.setText(String.format("native and Java get the same signature: %b",
                javaSignature.equals(ContextUtils.nGetApkSignedInfo(getApplicationContext()))));

        appPkgNameInListTv.setText(String.format("Package name in the list: %b",
                ContextUtils.nPkgNameInList(this)));
    }


}
