package com.rustfisher.basic4.activity.Hts;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.rustfisher.basic4.R;
import com.rustfisher.basic4.activity.SAct;
import com.rustfisher.basic4.service.HowToStopService;

public class SAct1 extends SAct {

    Button mBtn1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mLogPre = "[act1] ";
        super.onCreate(savedInstanceState);
        mBtn1 = findViewById(R.id.btn1);
        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SAct2.class));
            }
        });
//        startService(new Intent(getApplicationContext(), HowToStopService.class));
        bindService(new Intent(getApplicationContext(), HowToStopService.class), mConn, BIND_AUTO_CREATE);
        Thread sThread =
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "[Thread in Act] run");
                    }
                });
        Log.d(TAG, mLogPre + "[Thread in Act] " + sThread.toString());
        sThread.start();

        //增加整体布局监听
        ViewTreeObserver vto = mBtn1.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = mBtn1.getHeight();
                int width = mBtn1.getWidth();
                Log.d(TAG, "onGlobalLayout: mBtn1 " + width + ", " + height);
                mBtn1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: mBtn1.getWidth == " + mBtn1.getWidth());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // 在这里我们可以获取到View的真实宽高
        Log.d(TAG, "onWindowFocusChanged: mBtn1.getWidth == " + mBtn1.getWidth());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getApplicationContext(), HowToStopService.class));
    }
}
