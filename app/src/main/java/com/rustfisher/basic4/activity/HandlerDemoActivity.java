package com.rustfisher.basic4.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import android.util.Log;

import com.rustfisher.basic4.R;

/**
 * 用来测试handler
 * Created by Rust on 2018/6/21.
 */
public class HandlerDemoActivity extends Activity {
    private static final String TAG = "rustAppHandler";
    Handler mMainHandler1;
    Handler mMainHandler2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_handler_demo);
        initUtils();
    }

    private void initUtils() {
        mMainHandler1 = new Handler(Looper.getMainLooper());
        mMainHandler2 = new Handler(Looper.getMainLooper());
        Log.d(TAG, "mMainHandler1 post 任务");
        mMainHandler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "mMainHandler1的演示任务已执行");
            }
        }, 1500);

//        mMainHandler1.removeCallbacksAndMessages(null);
        mMainHandler2.removeCallbacksAndMessages(null);
    }


}
