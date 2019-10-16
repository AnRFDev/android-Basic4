package com.rustfisher.appdowloadsample.download;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    private static List<Task> taskList = new ArrayList<>();

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

    public void download(final String downUrl, final ControlCallBack callBack) {
        Task task = null;
        for (Task t : taskList) {
            if (downUrl.equals(t.getUrl())) {
                task = t;
                break;
            }
        }
        if (task == null) {
            task = new Task(downUrl, callBack.getTargetFile() , callBack.getTmpFile());
            taskList.add(task);
        } else if (task.downloading()) {
            Log.d(TAG, "downloading: " + task);
            return;
        }

        final Task finalTask = task;
        retrofit.create(ApiService.class)
                .download(downUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        finalTask.setState(DownloadTaskState.DOWNLOADING);
                        callBack.saveFile(responseBody);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept on error: " + downUrl, throwable);
                        finalTask.setState(DownloadTaskState.ERROR);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void init() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .addInterceptor(new ProgressInterceptor(new ProgressListener() {
                    @Override
                    public void update(String url, long bytesRead, long contentLength, boolean done) {
                        Log.d(TAG, String.format(Locale.CHINA, "downloading %s ; done: %b ; %d/%d byte(s)", url, done, bytesRead, contentLength));
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
        Observable<ResponseBody> downloadPartial(@Url String url, @Header("Content-Range") String range);
    }
}

