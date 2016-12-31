package com.app.infideap.httprequest;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Shiburagi on 20/10/2016.
 */
public class C<T> {
    private static final String TAG = C.class.getSimpleName();
    private final A a;
    private final Class<?> aClass;
    private Thread thread;

    private T t = null;

    public C(A a, Class<T> aClass) {
        this.a = a;
        this.aClass = aClass;

        a.createClient();
    }


    public void setCallback(final FutureCallback<T> callback) {

        thread = new Thread() {
            public void run() {
                Exception exception = null;
                try {
                    t = get();
                } catch (Exception e) {
                    exception = e;
                } finally {
                    final Exception finalException = exception;
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onCompleted(finalException, t);
                        }
                    });
                }
            }
        };

        thread.start();

    }

    public T get() throws Exception {

        Log.e(TAG, a.getUrl() + ": " + a.getRequest());
//        Log.d(TAG, "GET");
//        Log.d(TAG, a.getUrl());
        RequestBody requestBody = RequestBody.create(A.JSON, a.getRequest() == null ? "" : a.getRequest());
        if (a.getParts() != null) {
            if (a.getParts().size() > 0) {
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);
                for (Part part : a.getParts()) {

//                    MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
                    MediaType mediaType = MediaType.parse("application/octet-stream");
                    if (part.getFile() == null)
                        continue;
                    RequestBody body = RequestBody.create(mediaType, part.getFile());
                    builder = builder.addFormDataPart(part.getKey(), part.getFile().getName(), body);
                }
                builder.addPart(requestBody);
                requestBody = builder.build();
            }
        }
        Request.Builder builder = new Request.Builder()
                .url(a.getUrl());
        if (a.headers != null)
            builder.headers(a.headers);

        Request request = a.getRequest() == null ?
                builder.get().build() :
                builder.post(requestBody).build();

        Response response = a.client.newCall(request).execute();
//        BufferedSink sink = Okio.buffer(Okio.sink(a.context.getFileStreamPath("temp-response")));
//        Log.e(TAG, response.body().string());
        try {
            if (a.isString)
                t = (T) response.body().string();
            else {
                t = (T) a.gson.fromJson(response.body().charStream(), aClass);
            }
        } catch (Exception e) {
            throw e;
        }
        return t;
    }
}
