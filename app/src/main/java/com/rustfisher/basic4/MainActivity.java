package com.rustfisher.basic4;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.rustfisher.basic4.activity.AnnotationDemoActivity;
import com.rustfisher.basic4.activity.DeviceBrightnessAct;
import com.rustfisher.basic4.activity.EnumActivity;
import com.rustfisher.basic4.activity.PingAct;
import com.rustfisher.basic4.activity.PlaySoundAct;
import com.rustfisher.basic4.activity.ScreenshotActivity;
import com.rustfisher.basic4.activity.TextViewSpanAct;
import com.rustfisher.basic4.activity.ThroughDemoActivity;
import com.rustfisher.basic4.activity.ForceNetworkActivity;
import com.rustfisher.basic4.activity.HandlerDemoActivity;
import com.rustfisher.basic4.activity.Hts.SAct1;
import com.rustfisher.basic4.activity.IntentTestAct;
import com.rustfisher.basic4.activity.ViewLifecycleAct;
import com.rustfisher.basic4.inputDemo.EditText1Demo;
import com.rustfisher.basic4.wifiscan.WiFiScanActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initUI();
    }

    private void initUI() {
        setUpOnClickListener(this,
                R.id.adjust_brightness_btn,
                R.id.how_to_stop_service_btn,
                R.id.view_lifecycle_btn,
                R.id.intent_size_test_btn,
                R.id.handler_test_btn,
                R.id.annotation_btn,
                R.id.force_network_btn,
                R.id.wifi_scan_btn,
                R.id.click_through_btn,
                R.id.screenshot_btn,
                R.id.go_enum_btn,
                R.id.go_ping_act,
                R.id.go_play_sound_act,
                R.id.edit_text_demo,
                R.id.span_str
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.span_str:
                startActivity(new Intent(this, TextViewSpanAct.class));
                break;
            case R.id.edit_text_demo:
                startActivity(new Intent(this, EditText1Demo.class));
                break;
            case R.id.adjust_brightness_btn:
                startActivity(new Intent(this, DeviceBrightnessAct.class));
                break;
            case R.id.how_to_stop_service_btn:
                startActivity(new Intent(this, SAct1.class));
                break;
            case R.id.view_lifecycle_btn:
                startActivity(new Intent(this, ViewLifecycleAct.class));
                break;
            case R.id.intent_size_test_btn:
                startActivity(new Intent(this, IntentTestAct.class));
                break;
            case R.id.handler_test_btn:
                startActivity(new Intent(this, HandlerDemoActivity.class));
                break;
            case R.id.annotation_btn:
                startActivity(new Intent(this, AnnotationDemoActivity.class));
                break;
            case R.id.force_network_btn:
                startActivity(new Intent(this, ForceNetworkActivity.class));
                break;
            case R.id.wifi_scan_btn:
                startActivity(new Intent(this, WiFiScanActivity.class));
                break;
            case R.id.click_through_btn:
                startActivity(new Intent(this, ThroughDemoActivity.class));
                break;
            case R.id.screenshot_btn:
                startActivity(new Intent(this, ScreenshotActivity.class));
                break;
            case R.id.go_enum_btn:
                startActivity(new Intent(this, EnumActivity.class));
                break;
            case R.id.go_ping_act:
                startActivity(new Intent(this, PingAct.class));
                break;
            case R.id.go_play_sound_act:
                startActivity(new Intent(this, PlaySoundAct.class));
                break;
        }
    }

    private void setUpOnClickListener(View.OnClickListener listener, int... resID) {
        for (int id : resID) {
            findViewById(id).setOnClickListener(listener);
        }
    }
}
