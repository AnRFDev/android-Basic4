package com.rustfisher.basic4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.rustfisher.basic4.activity.AnnotationDemoActivity;
import com.rustfisher.basic4.activity.ScreenshotActivity;
import com.rustfisher.basic4.activity.ThroughDemoActivity;
import com.rustfisher.basic4.activity.ForceNetworkActivity;
import com.rustfisher.basic4.activity.HandlerDemoActivity;
import com.rustfisher.basic4.activity.Hts.SAct1;
import com.rustfisher.basic4.activity.IntentTestAct;
import com.rustfisher.basic4.activity.ViewLifecycleAct;
import com.rustfisher.basic4.wifiscan.WiFiScanActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initUI();
    }

    private void initUI() {
        findViewById(R.id.how_to_stop_service_btn).setOnClickListener(this);
        findViewById(R.id.view_lifecycle_btn).setOnClickListener(this);
        findViewById(R.id.intent_size_test_btn).setOnClickListener(this);
        findViewById(R.id.handler_test_btn).setOnClickListener(this);
        findViewById(R.id.annotation_btn).setOnClickListener(this);
        findViewById(R.id.force_network_btn).setOnClickListener(this);
        findViewById(R.id.wifi_scan_btn).setOnClickListener(this);
        findViewById(R.id.click_through_btn).setOnClickListener(this);
        findViewById(R.id.screenshot_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        }
    }
}
