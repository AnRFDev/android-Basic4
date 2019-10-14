package com.rustfisher.basic4.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.rustfisher.basic4.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET;
import static android.net.NetworkCapabilities.TRANSPORT_CELLULAR;

/**
 * 强制使用网络的类型
 * Created by Rust on 2018/8/28.
 */
public class ForceNetworkActivity extends Activity {
    private static final String TAG = "rustApp";
    private static final String URL_1 = "https://www.v2ex.com/";
    private static final String MINE_TYPE_1 = "text/html";
    private static final String MINE_TYPE_2 = "text/css";

    private static String[] mPermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_force_network);
        mWebView = findViewById(R.id.web_view);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
//        mWebView.loadUrl(URL_1); // 直接用默认的通道加载网络
        tryMobile();
    }

    private void tryMobile() {
        Log.d(TAG, "reqPermission");
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, mPermissions, 100);
            Log.d(TAG, "requestPermissions ");
        } else {
            forceMobile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                forceMobile();
            }
        }
    }

    @TargetApi(21)
    private void forceMobile() {
        Log.d(TAG, "forceMobile --> ");
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(NET_CAPABILITY_INTERNET);
        builder.addTransportType(TRANSPORT_CELLULAR); // 强制使用蜂窝数据网络-移动数据
        NetworkRequest build = builder.build();
        connectivityManager.requestNetwork(build, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                Log.d(TAG, "network onAvailable: " + network.toString());
                try {
                    URL url = new URL(URL_1);
                    HttpURLConnection connection = (HttpURLConnection) network.openConnection(url);
                    connection.connect();
                    InputStream is = connection.getInputStream();

//                    final File httpFile = prepareCacheFile(getApplicationContext(), "http1.html");
//                    Log.d(TAG, "httpFile: " + httpFile.getAbsolutePath());
//                    OutputStream httpOs = new FileOutputStream(httpFile);
//
//                    byte[] bytes = new byte[1024];
//                    int len;
//                    while ((len = is.read(bytes)) != -1) {
//                        httpOs.write(bytes, 0, len);
//                    }
//                    httpOs.flush();
//                    httpOs.close();
//                    is.close();

                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = br.readLine();
                    final StringBuilder stringBuilder = new StringBuilder();
                    final String htmlEndTag = "</html>";
                    while (!htmlEndTag.equals(line)) {
                        stringBuilder.append(line).append("\n");
                        line = br.readLine();
//                        Log.d(TAG, line + "\n");
                    }
                    stringBuilder.append(htmlEndTag);
                    is.close();

//                    final String htmlString = stringBuilder.toString();
//                    Log.d(TAG, "htmlString: \n" + htmlString);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "web view loading");
//                            mWebView.loadUrl(httpFile.getAbsolutePath());

//                            mWebView.loadDataWithBaseURL("", "", MINE_TYPE_1, "UTF-8", "");

                            mWebView.loadData(stringBuilder.toString(), MINE_TYPE_1, "UTF-8");
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "webView Fail: ", e);
                }

            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                Log.w(TAG, "onUnavailable");
            }
        });
    }


    private File prepareCacheFile(Context context, String filename) {
        File tmpDir = new File(Environment.getExternalStorageDirectory(), "a_tmp");
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }
        File cFile = new File(tmpDir, filename);
        if (cFile.exists()) {
            cFile.delete();
        }
        return cFile;
    }
}
