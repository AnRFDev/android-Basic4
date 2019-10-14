package com.rustfisher.basic4.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.rustfisher.basic4.R;
import com.rustfisher.basic4.entity.PingNetEntity;
import com.rustfisher.basic4.utils.PingNetUtil;

/**
 * ping测试
 * Created on 2019-10-14
 */
public class PingAct extends Activity {

    @Override
    protected void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ping);

        findViewById(R.id.ping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ping("www.baidu.com");
                    }
                }).start();
            }
        });
    }

    private void ping(String targetUrl) {
        PingNetEntity pingNetEntity = new PingNetEntity(targetUrl, 3, 5, new StringBuffer());
        pingNetEntity = PingNetUtil.ping(pingNetEntity);

        if (pingNetEntity != null) {
            String timeStr = pingNetEntity.getPingTime();
            if (!TextUtils.isEmpty(timeStr)) {
                int i = timeStr.indexOf(".");
                if (i > 0) {
                    int ms = Integer.parseInt(timeStr.substring(0, i));

                }
            }
        }
    }
}
