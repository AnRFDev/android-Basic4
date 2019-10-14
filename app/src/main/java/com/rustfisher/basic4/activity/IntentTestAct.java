package com.rustfisher.basic4.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rustfisher.basic4.R;

/**
 * Test intent
 * Log JavaBinder: !!! FAILED BINDER TRANSACTION !!!
 * Created by Rust on 2018/6/1.
 */
public class IntentTestAct extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "rustApp";
    private static final String MSG_INTENT = "intent_bi";
    private static final String MSG_ARR = "intent_arr";
    private static final String K_PIC = "key_pic";
    private static final String K_ARR = "key_arr";

    private TextView mSizeTv;
    private TextView mResTv;
    private int mBmpHeight = 1;
    private Bitmap mBmp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_intent_test);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void initView() {
        mSizeTv = findViewById(R.id.size_tv);
        mResTv = findViewById(R.id.res_tv);
        findViewById(R.id.s1_btn).setOnClickListener(this);
        findViewById(R.id.s2_btn).setOnClickListener(this);
        findViewById(R.id.s3_btn).setOnClickListener(this);
        findViewById(R.id.s4_btn).setOnClickListener(this);
        findViewById(R.id.s5_btn).setOnClickListener(this);
        findViewById(R.id.s6_btn).setOnClickListener(this);
        findViewById(R.id.s7_btn).setOnClickListener(this);
        findViewById(R.id.send_arr_btn).setOnClickListener(this);
        SeekBar seekBar = findViewById(R.id.value_sb);
        final TextView valueTv = findViewById(R.id.value_tv);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mBmpHeight = seekBar.getProgress();
                valueTv.setText(String.valueOf(mBmpHeight));
            }
        });
        mBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Log.d(TAG, "initView: mBmp.getByteCount==" + mBmp.getByteCount());
        IntentFilter intentFilter = new IntentFilter(MSG_INTENT);
        intentFilter.addAction(MSG_ARR);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.s1_btn:
                sendBroadcast(createIntent(mBmp, 1024, mBmpHeight));
                break;
            case R.id.s2_btn:
                sendBroadcast(createIntent(mBmp, 1024, 20)); // 80kb
                break;
            case R.id.s3_btn:
                sendBroadcast(createIntent(mBmp, 1024, 100)); // 400 kb
                break;
            case R.id.s4_btn:
                sendBroadcast(createIntent(mBmp, 1024, 200)); // 800kb
                break;
            case R.id.s5_btn:
                sendBroadcast(createIntent(mBmp, 1024, 256)); // 1Mb
                break;
            case R.id.s6_btn:
                sendBroadcast(createIntent(mBmp, 1024, 512)); // 2Mb
                break;
            case R.id.s7_btn:
                sendBroadcast(createIntent(mBmp, 1024, 1024));
                break;
            case R.id.send_arr_btn:
                Intent aIntent = new Intent(MSG_ARR);
                int[] arr = {1, 2, 3, 4, 5, 6, 7};
                Log.d(TAG, "发送数组  " + arr);
                aIntent.putExtra(K_ARR, arr);
                sendBroadcast(aIntent);
                break;
        }
    }

    @NonNull
    private Intent createIntent(Bitmap srcBmp, int dstWid, int dstHeight) {
        Bitmap b1 = Bitmap.createScaledBitmap(srcBmp, dstWid, dstHeight, false);
        int bCount = b1.getByteCount();
        String info = "新建bitmap字节数: " + bCount + ", " + bCount / 1024 + " kb";
        mSizeTv.setText(info);
        Log.d(TAG, info);
        Intent intent = new Intent(MSG_INTENT);
        intent.putExtra(K_PIC, b1);
        return intent;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (MSG_INTENT.equals(action)) {
                Bitmap bitmap = intent.getParcelableExtra(K_PIC);
                if (null != bitmap) {
                    String resInfo = "收到Bitmap: bitmap.getByteCount==" + bitmap.getByteCount();
                    mResTv.setText(resInfo);
                    Log.d(TAG, resInfo);
                }
            } else if (MSG_ARR.equals(action)) {
                int[] arr = intent.getIntArrayExtra(K_ARR);
                Log.d(TAG, "接到数组 " + arr);
            }
        }
    };
}
