package com.rustfisher.basic4.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 下载音频流
 * Created on 2019-9-18
 */
public class DownloadStreamThread extends Thread {
    private static final String TAG = "rustAppDownloadStream";

    private String urlStr;
    private final String targetFileAbsPath;
    private final boolean forceReplace;
    private ConcurrentHashMap<String, Boolean> map; // May be null

    public DownloadStreamThread(String urlStr, String targetFileAbsPath, boolean forceReplace, ConcurrentHashMap<String, Boolean> map) {
        setName("DownloadStreamThread-url: " + urlStr);
        this.urlStr = urlStr;
        this.targetFileAbsPath = targetFileAbsPath;
        this.forceReplace = forceReplace;
        this.map = map;
    }

    @Override
    public void run() {
        super.run();
        int count;
        File targetFile = new File(targetFileAbsPath);
        try {
            URL url = new URL(urlStr);
            URLConnection connection = url.openConnection();
            connection.connect();
            final int contentLength = connection.getContentLength();
            Log.d(TAG, "Stream content length: " + contentLength);
            if (targetFile.exists()) {
                if (targetFile.length() == contentLength) {
                    Log.d(TAG, "Current file's length is equals to content length. Force replace: " + forceReplace);
                    if (forceReplace) {
                        reCreateFile(targetFile);
                    } else {
                        if (map != null) {
                            map.put(urlStr, false);
                        }
                        Log.d(TAG, "Return.");
                        return;
                    }
                } else {
                    Log.d(TAG, "Current file's length is not equals to content length.");
                    reCreateFile(targetFile);
                }
            }
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(targetFileAbsPath);

            byte[] buffer = new byte[1024];
            long total = 0;
            while ((count = input.read(buffer)) != -1) {
                total += count;
                Log.d(TAG, String.format(Locale.CHINA, "Download [%s] progress: %.2f%%", targetFile.getName(), 100 * (total / (double) contentLength)));
                output.write(buffer, 0, count);
            }
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            Log.e(TAG, "Download fail: ", e);
            try {
                boolean d = targetFile.delete();
                Log.d(TAG, "Download fail, delete file " + d);
            } catch (Exception e1) {
                Log.e(TAG, "Delete fail: ", e);
            }
        }
        if (map != null) {
            map.put(urlStr, false);
        }
    }

    private void reCreateFile(File targetFile) {
        boolean d1 = targetFile.delete();
        Log.d(TAG, "Delete current file " + d1);
        try {
            boolean n = targetFile.createNewFile();
            Log.d(TAG, "Create new file: " + n + ", " + targetFile);
        } catch (IOException e) {
            Log.e(TAG, "run: ", e);
        }
    }
}

