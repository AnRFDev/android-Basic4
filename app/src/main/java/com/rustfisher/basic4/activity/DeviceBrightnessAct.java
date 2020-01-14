package com.rustfisher.basic4.activity;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.rustfisher.basic4.R;

/**
 * 调整系统亮度
 * Created on 2020-1-14
 */
public class DeviceBrightnessAct extends Activity {
    private static final String TAG = "rustAppDevice";
    private SeekBar mSysSb;
    private SeekBar mPageSb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_device_brightness);

        initUI();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSysSb.setProgress(getSysBrightness());
        mPageSb.setProgress(getPageBrightness());
    }

    private void initUI() {
        mSysSb = findViewById(R.id.sys_sb);
        mPageSb = findViewById(R.id.page_bright_sb);
        mPageSb.setMax(255);
        mSysSb.setMax(255);
        mSysSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    setSysBrightness(progress);
                    mSysSb.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mPageSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    changePageBrightness(progress);
                    mPageSb.setProgress(getPageBrightness());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private int getSysBrightness() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "getSysBrightness: ", e);
        }
        return -1;
    }

    private void setSysBrightness(int b) {
        try {
            // 没有修改系统设置的权限，会报异常
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, b);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "修改系统亮度失败，没有权限", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "set sys brightness: ", e);
        }
    }

    public void changePageBrightness(int brightness) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }

    public int getPageBrightness() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (lp.screenBrightness == -1) {
            return getSysBrightness();
        }
        return (int) (lp.screenBrightness * 255f);
    }
}
