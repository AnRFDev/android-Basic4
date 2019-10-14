package com.rustfisher.basic4.activity;

import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.rustfisher.basic4.R;

/**
 * 枚举类
 */
public class EnumActivity extends AppCompatActivity {
    private static final String TAG = "rustApp";
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Type mType = Type.FIRST;
    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enum);
        Log.d(TAG, "onCreate: mType: " + mType);
        mTv1 = findViewById(R.id.e_tv1);
        mTv2 = findViewById(R.id.e_tv2);
        mTv3 = findViewById(R.id.e_tv3);
        mTv1.setText(mType.toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                publishState(mType);
                mType = Type.SECOND;
            }
        }).start();
    }

    private void publishState(final Type state) {
        final Type sendingState = state;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTv2.setText("dup: " + sendingState.toString());
                mTv3.setText("ori: " + state.toString());
            }
        }, 900);
    }


    enum Type {
        FIRST, SECOND
    }
}
