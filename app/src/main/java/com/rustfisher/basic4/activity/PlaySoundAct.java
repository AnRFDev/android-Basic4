package com.rustfisher.basic4.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.rustfisher.basic4.R;
import com.rustfisher.basic4.utils.DownloadStreamThread;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;


public class PlaySoundAct extends Activity {
    private static final String TAG = "rustAppPlaySound";
    private static final int TOTAL = 100;
    private int mPreparedCount = 0;
    private int mCompletedCount = 0;
    private int mErrorCount = 0;
    private int mExceptionCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_play_sound);
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound();
            }
        });
        findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDownload();
            }
        });
    }

    private void startDownload() {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "Download/aPlay");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        for (int i = 0; i < TOTAL; i++) {
            File target = new File(dir, "s-" + i + ".mp3");

            new DownloadStreamThread("http://api.frdic.com/api/v2/speech/speakweb?langid=n&txt=QYNd2hlbiB0aGUgY29sb25lbCByZXRpcmVkIGhlIGRlY2lkZWQgdG8gc2VsbCB0aGUgd29ybGQgaGlz%0AIGNvb2wgbmV3IHJlY2lwZS4%3D",
                    target.getAbsolutePath(), true, new ConcurrentHashMap<String, Boolean>()).start();
        }
    }

    private void playSound() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < TOTAL; i++) {
                    final int playerIndex = i;
                    try {
                        MediaPlayer mediaPlayer = new MediaPlayer();
//                        mediaPlayer.setDataSource("http://api.frdic.com/api/v2/speech/speakweb?langid=n&txt=QYNd2hlbiB0aGV5IHN0YXJ0IGNsYXNzIHRoZWlyIHRlYWNoZXIgZGVjaWRlZCB0byBnaXZlIHRoZW0g%0AYW4gZXhhbS4%3D");
                        mediaPlayer.setDataSource("http://api.frdic.com/api/v2/speech/speakweb?langid=n&txt=QYNd2hlbiB0aGUgY29sb25lbCByZXRpcmVkIGhlIGRlY2lkZWQgdG8gc2VsbCB0aGUgd29ybGQgaGlz%0AIGNvb2wgbmV3IHJlY2lwZS4%3D");
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                                mPreparedCount++;
                                Log.d(TAG, "onPrepared: [" + playerIndex + "]" + " mPreparedCount: " + mPreparedCount);
                            }
                        });
                        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                                mErrorCount++;
                                Log.e(TAG, "onError: [" + playerIndex + "]  mErrorCount: " + mErrorCount + ", " + i + ", " + i1);
                                return false;
                            }
                        });
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                mCompletedCount++;
                                Log.d(TAG, "onCompletion: [" + playerIndex + "]  mCompletedCount: " + mCompletedCount);
                            }
                        });
                    } catch (Exception e) {
                        mExceptionCount++;
                        Log.e(TAG, "run: error [" + playerIndex + "]  mExceptionCount: " + mExceptionCount, e);
                    }
                }
            }
        }).start();
    }
}
