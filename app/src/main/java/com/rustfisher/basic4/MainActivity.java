package com.rustfisher.basic4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.rustfisher.basic4.activity.Hts.SAct1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initUI();
    }

    private void initUI() {
        findViewById(R.id.how_to_stop_service_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.how_to_stop_service_btn:
                startActivity(new Intent(this, SAct1.class));
                break;
        }
    }
}
