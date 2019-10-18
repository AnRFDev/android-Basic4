package com.rustfisher.appdowloadsample.download;

import android.util.Log;

import java.io.File;
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
    private final String url;
    private DownloadTaskState state = DownloadTaskState.CREATED;
    private int downloadBytesPerMs;
    private long progressTotal;
    private long progressCurrent = -1;
    private InputStream srcInputStream;
    private long localFileStartByteIndex = 0;

    public ControlCallBack(String url, File targetFile) {
        this(url, targetFile, defDownloadBytePerMs);
    }

    public ControlCallBack(String url, File targetFile, int downloadBytesPerMs) {
        this.url = url;
        this.targetFile = targetFile;
        this.downloadBytesPerMs = downloadBytesPerMs;
        tmpFile = new File(targetFile.getAbsolutePath() + ".tmp");
    }

    public void onSuccess(String url) {
    }

    public void onError(String url, Throwable e) {
    }

    public void onPaused(String url) {

    }

    public abstract void onDelete(String url);

    public void onInfo(String log) {
        Log.d(TAG, log);
    }

    public int downloadBytePerMs() {
        return downloadBytesPerMs;
    }

    public void saveFile(ResponseBody body) {
        state = DownloadTaskState.DOWNLOADING;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            Log.d(TAG, "saveFile: body content length: " + body.contentLength());
            srcInputStream = body.byteStream();
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
            Log.d(TAG, "saveFile: localFileStartByteIndex: " + localFileStartByteIndex);
            if (localFileStartByteIndex > 0) {
                fos = new FileOutputStream(file, true);
            } else {
                fos = new FileOutputStream(file);
            }
            long time = System.currentTimeMillis();
            while ((len = srcInputStream.read(buf)) != -1 &&
                    !isDeletingState() &&
                    !state.equals(DownloadTaskState.PAUSING)) {
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
                if (isDeletingState()) {
                    state = DownloadTaskState.DELETING;
                    srcInputStream.close();
                    break;
                }
            }
            if (state.equals(DownloadTaskState.PAUSING)) {
                state = DownloadTaskState.PAUSED;
                onPaused(url);
            } else if (!isDeletingState()) {
                fos.flush();
                boolean rename = tmpFile.renameTo(targetFile);
                if (rename) {
                    setState(DownloadTaskState.DONE);
                    onSuccess(url);
                } else {
                    setState(DownloadTaskState.ERROR);
                    onError(url, new Exception("Rename file fail. " + tmpFile));
                }
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "saveFile: FileNotFoundException ", e);
            setState(DownloadTaskState.ERROR);
            onError(url, e);
        } catch (Exception e) {
            Log.e(TAG, "saveFile: IOException ", e);
            setState(DownloadTaskState.ERROR);
            onError(url, e);
        } finally {
            try {
                if (srcInputStream != null) {
                    srcInputStream.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                Log.e(TAG, "saveFile", e);
            }
            if (isDeletingState()) {
                onDelete(url);
            }
        }
    }

    /**
     * 取消任务
     * 需要考虑当前状态
     * (这种状态变换真的是好的设计吗？)
     */
    public void delete() {
        DownloadTaskState old = state;
        state = DownloadTaskState.DELETING;
        switch (old) {
            case CREATED:
            case DONE:
            case ERROR:
            case PAUSED:
                onDelete(url);
                break;
            case DOWNLOADING:
                break;
            case DELETING:
                return;
        }
        if (srcInputStream != null) {
            try {
                srcInputStream.close();
            } catch (Exception e) {
                Log.e(TAG, "delete download: ", e);
            }
        }
    }

    public void pause() {
        switch (state) {
            case CREATED:
                state = DownloadTaskState.PAUSING;
                onPaused(url);
                break;
            case DOWNLOADING:
                state = DownloadTaskState.PAUSING;
                break;
            default:
                return;
        }
    }

    public long getLocalFileStartByteIndex() {
        return localFileStartByteIndex;
    }

    public void setLocalFileStartByteIndex(long localFileStartByteIndex) {
        this.localFileStartByteIndex = localFileStartByteIndex;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public File getTmpFile() {
        return tmpFile;
    }

    public String getUrl() {
        return url;
    }

    public DownloadTaskState getState() {
        return state;
    }

    public boolean isDownloading() {
        return DownloadTaskState.DOWNLOADING.equals(state);
    }

    public boolean isPaused() {
        return DownloadTaskState.PAUSED.equals(state);
    }

    public boolean isError() {
        return DownloadTaskState.ERROR.equals(state);
    }

    public boolean isDeletingState() {
        return state.equals(DownloadTaskState.DELETING);
    }

    public void setState(DownloadTaskState state) {
        this.state = state;
    }

    public long getProgressTotal() {
        return progressTotal;
    }

    public void setProgressTotal(long progressTotal) {
        this.progressTotal = progressTotal;
    }

    public long getProgressCurrent() {
        return progressCurrent;
    }

    public void setProgressCurrent(long progressCurrent) {
        this.progressCurrent = progressCurrent;
    }

    @Override
    public String toString() {
        return "ControlCallBack ["
                + "url: " + url + "\n"
                + "  state: " + state + ", "
                + "targetFile: " + targetFile + "\n"
                + "downloadBytesPerMs: " + downloadBytesPerMs
                + "]";
    }
}
