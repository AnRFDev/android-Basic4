package com.rustfisher.the3part;

import android.util.Log;

import com.google.gson.Gson;

public class NetUtil {

    private static final String TAG = "rustApp";

    public static void parse(String jsonStr, Object obj) {
        Log.d(TAG, "parse: " + jsonStr);
        Gson gson = new Gson();
        gson.fromJson(jsonStr, obj.getClass());
        Log.d(TAG, "parse: " + gson);
    }
}
