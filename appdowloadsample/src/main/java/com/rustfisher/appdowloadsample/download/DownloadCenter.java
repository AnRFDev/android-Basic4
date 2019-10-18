package com.rustfisher.appdowloadsample.download;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 下载中心
 * Created on 2019-10-16
 */
public class DownloadCenter {
    private static final String TAG = "rustAppDownloadCenter";

    private static DownloadCenter instance;

    private static Retrofit retrofit;

    private List<ControlCallBack> callBackList = new ArrayList<>();
    private Set<DownloadCenterListener> listeners = new HashSet<>();

    private DownloadCenter() {
        init();
    }

    public static DownloadCenter getInstance() {
        if (instance == null) {
            synchronized (DownloadCenter.class) {
                if (instance == null) {
                    instance = new DownloadCenter();
                }
            }
        }
        return instance;
    }

    public void continueDownload(final String downUrl, File targetFile, final int downloadBytePerMs) {
        ControlCallBack callBack = null;
        for (ControlCallBack c : callBackList) {
            if (c.getUrl().equals(downUrl)) {
                callBack = c;
                break;
            }
        }
        if (callBack == null) {
            return;
        }
        if (!callBack.isPaused()) {
            return;
        }
        Log.d(TAG, "continueDownload: tmpFile: " + callBack.getTmpFile());
        long startByte = 0;
        if (callBack.getTmpFile().exists()) {
            startByte = callBack.getTmpFile().length();
            Log.d(TAG, "continueDownload: tmp file exists.");
        }
        Log.d(TAG, "continueDownload: startByte: " + startByte);
        callBack.setLocalFileStartByteIndex(startByte);
        tellDownloadStart(callBack);
        final ControlCallBack finalCallBack = callBack;
        retrofit.create(ApiService.class)
                .downloadPartial(downUrl, "bytes=" + startByte + "-")
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        finalCallBack.saveFile(responseBody);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept on error: " + downUrl, throwable);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void download(final String downUrl, File targetFile, final int downloadBytePerMs) {
        ControlCallBack callBack = null;
        for (ControlCallBack c : callBackList) {
            if (c.getUrl().equals(downUrl)) {
                callBack = c;
                break;
            }
        }
        if (callBack == null) {
            callBack = new ControlCallBack(downUrl, targetFile, downloadBytePerMs) {
                @Override
                public void onSuccess(String url) {
                    tellDownloadSuccess(url);
                }

                @Override
                public void onPaused(String url) {
                    tellDownloadPaused(url);
                }

                @Override
                public void onError(String url, Throwable e) {
                    tellDownloadError(url, e);
                }

                @Override
                public void onDelete(String url) {
                    for (ControlCallBack c : callBackList) {
                        if (url.equals(c.getUrl())) {
                            callBackList.remove(c);
                            break;
                        }
                    }
                    tellDownloadDelete(url);
                }
            };
            callBackList.add(callBack);
        }
        if (callBack.isDownloading()) {
            Log.d(TAG, "downloading this task.");
            return;
        }

        tellDownloadStart(callBack);
        final ControlCallBack finalCallBack = callBack;
        retrofit.create(ApiService.class)
                .download(downUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        finalCallBack.saveFile(responseBody);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept on error: " + downUrl, throwable);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Download center retrofit onError: ", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void init() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .addInterceptor(new ProgressInterceptor(new ProgressListener() {
                    @Override
                    public void update(String url, long bytesRead, long contentLength, boolean done) {
                        tellProgress(url, bytesRead, contentLength, done);
                    }
                }))
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://yourbaseurl.com")
                .build();
    }

    private interface ApiService {
        @Streaming
        @GET
        Observable<ResponseBody> download(@Url String url);

        @Streaming
        @GET
        Observable<ResponseBody> downloadPartial(@Url String url, @Header("Range") String range);
    }

    public void addListener(DownloadCenterListener l) {
        listeners.add(l);
    }

    public void removeListener(DownloadCenterListener l) {
        listeners.remove(l);
    }

    private void tellDownloadSuccess(String url) {
        for (DownloadCenterListener l : listeners) {
            l.onSuccess(url);
        }
    }

    private void tellDownloadPaused(String url) {
        for (DownloadCenterListener l : listeners) {
            l.onPaused(url);
        }
    }

    private void tellDownloadError(String url, Throwable e) {
        for (DownloadCenterListener l : listeners) {
            l.onError(url, e);
        }
    }

    private void tellProgress(String url, long bytesRead, long contentLength, boolean done) {
        for (DownloadCenterListener l : listeners) {
            l.onProgress(url, bytesRead, contentLength, done);
        }
    }

    private void tellDownloadDelete(String url) {
        for (DownloadCenterListener l : listeners) {
            l.onDeleted(url);
        }
    }

    private void tellDownloadStart(ControlCallBack callBack) {
        for (DownloadCenterListener l : listeners) {
            l.onStart(callBack);
        }
    }
}

