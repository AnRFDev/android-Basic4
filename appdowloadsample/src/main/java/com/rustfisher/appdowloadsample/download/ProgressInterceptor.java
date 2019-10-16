package com.rustfisher.appdowloadsample.download;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ProgressInterceptor implements Interceptor {

    private ProgressListener progressListener;

    public ProgressInterceptor(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(chain.request().url().url().toString(), originalResponse.body(), progressListener))
                .build();
    }
}
