package com.rustfisher.appdowloadsample.progress;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 如何监听网络下载进度
 * https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/Progress.java
 */
public final class Progress {

    public void run() throws Exception {
        Request request = new Request.Builder()
                .url("https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/Progress.java")
                .build();

        final ProgressListener progressListener = new ProgressListener() {
            boolean firstUpdate = true;

            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                if (done) {
                    System.out.println("completed");
                } else {
                    if (firstUpdate) {
                        firstUpdate = false;
                        if (contentLength == -1) {
                            System.out.println("content-length: unknown");
                        } else {
                            System.out.format("content-length: %d\n", contentLength);
                        }
                    }

                    System.out.println(bytesRead);

                    if (contentLength != -1) {
                        System.out.format("%d%% done\n", (100 * bytesRead) / contentLength);
                    }
                }
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                                .build();
                    }
                })
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
    }

    public static void main(String... args) throws Exception {
        new Progress().run();
    }

    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }

    interface ProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }
}