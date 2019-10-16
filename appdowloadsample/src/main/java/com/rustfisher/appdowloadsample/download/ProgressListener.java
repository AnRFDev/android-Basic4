package com.rustfisher.appdowloadsample.download;

public interface ProgressListener {
    void update(String url, long bytesRead, long contentLength, boolean done);
}