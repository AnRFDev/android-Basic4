package com.rustfisher.basic4.activity;

import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rustfisher.basic4.R;
import com.rustfisher.basic4.view.LifeView;

import java.util.Locale;

/**
 * For view's lifecycle
 * Created by Rust on 2018/5/31.
 */

public class ViewLifecycleAct extends AppCompatActivity {
    private static final String TAG = "rustAppViewLife";
    LifeView mLifeView;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "Activity生命周期:onCreate");
        setContentView(R.layout.act_view_lifecycle);
        initView();
    }

    private void initView() {
        mLifeView = findViewById(R.id.life_view);
        findViewById(R.id.call_refresh_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 100; i++) {
                    mLifeView.invalidate();
                }
                Log.d(TAG, "[act] 短时间内连续调用 invalidate");
            }
        });
        findViewById(R.id.color_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 100; i++) {
                    mLifeView.setPaintColor(Color.argb(100, i, i, i));
                    mLifeView.invalidate();
                }
                Log.d(TAG, "[act] 设置view并 invalidate");
            }
        });
        findViewById(R.id.time_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 30; i++) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLifeView.invalidate();
                        }
                    }, i * 2);
                }
                Log.d(TAG, "[act] 延时 invalidate");
            }
        });

        final Button setXBtn = findViewById(R.id.set_x_btn);
        setXBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printViewXY(v);
            }
        });
        findViewById(R.id.move_left_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setXBtn.setX(setXBtn.getX() - 10);
                printViewXY(setXBtn);
            }
        });
        findViewById(R.id.move_right_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setXBtn.setX(setXBtn.getX() + 10);
                printViewXY(setXBtn);
            }
        });
        findViewById(R.id.tx_minus_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setXBtn.setTranslationX(setXBtn.getTranslationX() - 10);
                printViewXY(setXBtn);
            }
        });
        findViewById(R.id.tx_plus_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setXBtn.setTranslationX(setXBtn.getTranslationX() + 10);
                printViewXY(setXBtn);
            }
        });
        findViewById(R.id.tx_0_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setXBtn.setTranslationX(0);
                printViewXY(setXBtn);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "Activity生命周期:onStart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "Activity生命周期:onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "Activity生命周期:onRestart");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "Activity生命周期:onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "Activity生命周期:onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Activity生命周期:onDestroy");
    }

    private void printViewXY(View view) {
        Log.d(TAG, String.format(Locale.CHINA, "getX, Y = [%.4f, %.4f]; TranslationX,Y = [%.4f, %.4f]; ", view.getX(), view.getY(),
                view.getTranslationX(), view.getTranslationY()));
    }
}
