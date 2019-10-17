package com.rustfisher.appdowloadsample.download;

import java.io.File;

/**
 * 下载任务
 * Created on 2019-10-16
 * @deprecated
 */
public class Task {
    private static int autoCode = 0;

    private String url;
    private String name; // todo 暂时没用
    private int code;
    private File targetFile;
    private File tmpFile;

    private DownloadTaskState state;

    public Task(String url, File targetFile, File tmpFile) {
        this(url, url, autoCode++, targetFile, tmpFile);
    }

    public Task(String url, String name, int code, File targetFile, File tmpFile) {
        this.url = url;
        this.name = name;
        this.code = code;
        this.targetFile = targetFile;
        this.tmpFile = tmpFile;
        state = DownloadTaskState.CREATED;
    }

    public static int getAutoCode() {
        return autoCode;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public DownloadTaskState getState() {
        return state;
    }

    public boolean downloading() {
        return DownloadTaskState.DOWNLOADING.equals(state);
    }

    public void setState(DownloadTaskState state) {
        this.state = state;
    }

    public File getTmpFile() {
        return tmpFile;
    }

    @Override
    public String toString() {
        return "Task["
                + "url: " + url + ", code: " + code
                + ", state: " + state + ", target file: " + targetFile
                + "]";
    }
}
