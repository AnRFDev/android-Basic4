package com.rustfisher.basic4.activity.Hts;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rustfisher.basic4.R;
import com.rustfisher.basic4.activity.SAct;
import com.rustfisher.basic4.service.HowToStopService;

public class SAct2 extends SAct {

    Button mBtn1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mLogPre = "[act2] ";
        super.onCreate(savedInstanceState);
        mBtn1 = findViewById(R.id.btn1);
        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SAct3.class));
            }
        });
        bindService(new Intent(getApplicationContext(), HowToStopService.class), mConn, BIND_AUTO_CREATE);
        Log.d(TAG, "mBtn1 post runnable");
        mBtn1.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "mBtn1: " + mBtn1.getWidth() + ", " + mBtn1.getHeight());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getApplicationContext(), HowToStopService.class));
    }
}
