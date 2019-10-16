package com.rustfisher.appdowloadsample.download;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * 控制下载速度
 * 写文件
 * Created on 2019-10-16
 */
public abstract class ControlCallBack {
    private static final String TAG = "rustAppProgressCb";
    public static int defDownloadBytePerMs = 100; // 每毫秒下载的字节数
    private File targetFile;
    private File tmpFile;
    public boolean isCancel;
    private String url;

    public ControlCallBack(String url, File targetFile) {
        this.url = url;
        this.targetFile = targetFile;
        this.tmpFile = new File(targetFile.getAbsolutePath() + ".tmp");
    }

    public abstract void onSuccess();

    public abstract void onError(Throwable e);

    public abstract void onCancel(String url);

    public void onInfo(String log) {
        Log.d(TAG, log);
    }

    public int downloadBytePerMs() {
        return defDownloadBytePerMs;
    }

    public void saveFile(ResponseBody body) {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            Log.d(TAG, "saveFile: body content length: " + body.contentLength());
            is = body.byteStream();
            File dir = tmpFile.getParentFile();
            if (dir == null) {
                throw new FileNotFoundException("target file has no dir.");
            }
            if (!dir.exists()) {
                boolean m = dir.mkdirs();
                onInfo("Create dir " + m + ", " + dir);
            }
            File file = tmpFile;
            if (!file.exists()) {
                boolean c = file.createNewFile();
                onInfo("Create new file " + c);
            }
            fos = new FileOutputStream(file);
            long time = System.currentTimeMillis();
            while ((len = is.read(buf)) != -1 && !isCancel) {
                fos.write(buf, 0, len);
                int duration = (int) (System.currentTimeMillis() - time);

                int overBytes = len - downloadBytePerMs() * duration;
                if (overBytes > 0) {
                    try {
                        Thread.sleep(overBytes / downloadBytePerMs());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                time = System.currentTimeMillis();

            }
            if (!isCancel) {
                fos.flush();
            }
            if (isCancel) {
                onCancel(url);
            } else {
                boolean rename = tmpFile.renameTo(targetFile);
                if (rename) {
                    onSuccess();
                } else {
                    onError(new Exception("Rename file fail. " + tmpFile));
                }
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "saveFile: FileNotFoundException ", e);
            onError(e);
        } catch (Exception e) {
            Log.e(TAG, "saveFile: IOException ", e);
            onError(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "saveFile", e);
            }
        }
    }

    public void cancel() {
        isCancel = true;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public File getTmpFile() {
        return tmpFile;
    }
}
