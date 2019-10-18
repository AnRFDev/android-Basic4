package com.rustfisher.appdowloadsample.download;

/**
 * 下载任务的状态
 * Created on 2019-10-16
 */
public enum DownloadTaskState {
    CREATED,
    DOWNLOADING,
    PAUSING,
    PAUSED,
    DONE,
    ERROR,
    DELETING
}
