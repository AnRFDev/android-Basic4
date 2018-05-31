package com.rustfisher.basic4.activity;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rustfisher.basic4.R;
import com.rustfisher.basic4.view.LifeView;

/**
 * For view's lifecycle
 * Created by Rust on 2018/5/31.
 */

public class ViewLifecycleAct extends AppCompatActivity {
    private static final String TAG = "rustAppViewLife";
    LifeView mLifeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "Activity生命周期:onCreate");
        setContentView(R.layout.act_view_lifecycle);
        initView();
    }

    private void initView() {
        mLifeView = findViewById(R.id.life_view);
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
}
