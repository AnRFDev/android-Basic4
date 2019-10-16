package com.rustfisher.appdowloadsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rustfisher.appdowloadsample.download.DownloadCenter;
import com.rustfisher.appdowloadsample.download.ControlCallBack;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "rustAppMainPage";

    private static final int REQ_CODE = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_CODE);
        setOnClick(this, R.id.download_1, R.id.download_2, R.id.download_3);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE) {
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请允许应用读写外部存储", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_1:
                final String url1 = "http://dldir1.qq.com/weixin/android/weixin707android1520.apk";
                downloadUrl(url1, new File(Environment.getExternalStorageDirectory(), "weixin707android1520.apk"), 1000000);
                break;
            case R.id.download_2:
                final String url2 = "http://releases.ubuntu.com/18.04.3/ubuntu-18.04.3-desktop-amd64.iso?_ga=2.164765245.385568095.1571216179-1901711613.1571216179";
                downloadUrl(url2, new File(Environment.getExternalStorageDirectory(), "ubuntu-18.04.3-desktop-amd64.iso"), 1000);
                break;
            case R.id.download_3:
                final String url3 = "https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk";
                downloadUrl(url3, new File(Environment.getExternalStorageDirectory(), "alipay_wap_main.apk"), 1000000);
                break;
        }
    }

    private void downloadUrl(final String url, File targetFile, final int downloadBytePerMs) {
        DownloadCenter.getInstance().download(url,
                new ControlCallBack(url, targetFile) {
                    @Override
                    public int downloadBytePerMs() {
                        return downloadBytePerMs;
                    }

                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: " + url);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + url, e);
                    }

                    @Override
                    public void onCancel(String url) {
                        Log.w(TAG, "onCancel: " + url);
                    }
                });
    }

    private void setOnClick(View.OnClickListener l, int... id) {
        for (int i : id) {
            findViewById(i).setOnClickListener(l);
        }
    }
}
