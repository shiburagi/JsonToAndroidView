package com.app.infideap.httprequest;

import java.io.File;
import java.io.IOException;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by Shiburagi on 20/10/2016.
 */
public class B<T> {
    private static final String TAG = B.class.getSimpleName();
    private final A a;
    private final File file;
    private FutureCallback<File> callback;
    private Thread thread;

    public B(A a, File file) {
        this.a = a;
        this.file = file;
        a.createClient();
    }

    public void setCallback(final FutureCallback<File> callback) {
        this.callback = callback;

        thread = new Thread() {
            public void run() {
                Exception exception = null;
                File file = null;
                try {
                    file = get();
                } catch (IOException e) {
                    exception = e;
                } finally {
                    callback.onCompleted(exception, file);
                }
            }
        };

        thread.start();
    }

    public File get() throws IOException {
//        Log.d(TAG,a.getUrl());

        RequestBody requestBody = RequestBody.create(A.JSON, a.getRequest()==null?"":a.getRequest());
        Request.Builder builder = new Request.Builder()
                .url(a.getUrl());

        builder.headers(a.headers);
        Request request = a.getRequest()==null?
                builder.get().build():
                builder.post(requestBody).build();

        Response response = a.client.newCall(request).execute();
        BufferedSink sink = Okio.buffer(Okio.sink(file));
        sink.writeAll(response.body().source());
        sink.close();

        return file;
    }
}
