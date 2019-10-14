package com.rustfisher.basic4.activity;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.rustfisher.basic4.R;

/**
 * 事件穿透
 */
public class ThroughDemoActivity extends Activity {
    private static final String TAG = "rustAppClick";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_click_through);
        findViewById(R.id.layer_1).setOnClickListener(mOnClickListener);
        findViewById(R.id.layer_2).setOnClickListener(mOnClickListener);
        findViewById(R.id.layer_tv1).setOnClickListener(mOnClickListener);

        findViewById(R.id.t_layer1).setOnTouchListener(mOnTouchListener);
        findViewById(R.id.t_layer2).setOnTouchListener(mOnTouchListener);
        findViewById(R.id.t_layer3).setOnTouchListener(mOnTouchListener);
        findViewById(R.id.t_layer_tv1).setOnTouchListener(mOnTouchListener);

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layer_1:
                    toast("layer1");
                    Log.d(TAG, "onClick: layer1");
                    break;
                case R.id.layer_2:
                    toast("layer2");
                    Log.d(TAG, "onClick: layer2");
                    break;
                case R.id.layer_tv1:
                    toast("layer tv1");
                    Log.d(TAG, "onClick: layer tv1");
                    break;
            }
        }
    };

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (v.getId()) {
                case R.id.t_layer1:
                    Log.d(TAG, "onTouch: t_layer1, " + event);
                    return false;
                case R.id.t_layer2:
                    Log.d(TAG, "onTouch: t_layer2, " + event);
                    return true; // 消费这个触摸事件后，很快又可以响应下一个触摸事件
                case R.id.t_layer3:
                    Log.d(TAG, "onTouch: t_layer3, " + event);
                    return false;
                case R.id.t_layer_tv1:
                    Log.d(TAG, "onTouch: t_layer_tv1, " + event);
                    return false;
            }
            return false;
        }
    };

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
