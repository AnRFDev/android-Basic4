package com.rustfisher.basic4.activity;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.rustfisher.basic4.R;
import com.rustfisher.basic4.service.HowToStopService;

public abstract class SAct extends AppCompatActivity {

    protected String TAG = "rustApp";
    protected String mLogPre = "[sAct] ";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_how_to_stop_service);
        TextView tv1 = findViewById(R.id.tv1);
        tv1.setText(mLogPre);
        Log.d(TAG, mLogPre + "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, mLogPre + "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, mLogPre + "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, mLogPre + "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, mLogPre + "onDestroy");
    }

    protected ServiceConnection mConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
            Log.d(TAG, mLogPre + "onServiceConnected: " + name);
            HowToStopService.HtsBinder htsBinder = (HowToStopService.HtsBinder) serviceBinder;
            htsBinder.getService().publicFunction(mLogPre);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, mLogPre + "onServiceDisconnected: " + name);
        }
    };
}
