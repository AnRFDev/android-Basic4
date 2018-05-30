package com.rustfisher.basic4.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 用来演示如何停止
 */
public class HowToStopService extends Service {

    private static final String TAG = "rustApp";
    private static final String LOG_PRE = "[Service1] ";

    private HtsBinder mBinder = new HtsBinder(); // Hts: How-to-stop

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, LOG_PRE + "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, LOG_PRE + "onStartCommand");
//        return super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, LOG_PRE + "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, LOG_PRE + "onDestroy: bye bye");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, LOG_PRE + "onBind");
        return mBinder;
    }

    public class HtsBinder extends Binder {
        public HowToStopService getService() {
            return HowToStopService.this;
        }
    }

    public void publicFunction(String clientName) {
        Log.d(TAG, LOG_PRE + "公开方法被调用  -  " + clientName);
    }
}
