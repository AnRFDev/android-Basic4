package com.rustfisher.basic4.activity.Hts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rustfisher.basic4.activity.SAct;
import com.rustfisher.basic4.service.HowToStopService;

public class SAct3 extends SAct {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mLogPre = "[act3] ";
        super.onCreate(savedInstanceState);
        bindService(new Intent(getApplicationContext(), HowToStopService.class), mConn, BIND_AUTO_CREATE);
    }
}
