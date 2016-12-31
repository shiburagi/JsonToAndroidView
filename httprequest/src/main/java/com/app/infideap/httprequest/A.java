package com.app.infideap.httprequest;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by Shiburagi on 20/10/2016.
 */
public class A {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private long connectTimeout = 1L;
    private long readTimeout = 1L;
    private TimeUnit connectTimeUnit = TimeUnit.MINUTES;
    private TimeUnit readTimeUnit = TimeUnit.MINUTES;


    OkHttpClient client = new OkHttpClient();

    Context context;
    private String url;
    ProgressCallback downlaodProgressCallback;
    private String request;
    public Gson gson;
    ProgressCallback uploadProgressCallback;
    private List<Part> parts = new ArrayList<>();
    boolean isString;
    public Headers headers;

    public A(Context context) {
        this.context = context;
        GsonBuilder builder = new GsonBuilder();
//        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
//        builder.excludeFieldsWithoutExposeAnnotation();
        gson = builder.create();


    }

    public A load(String url) {
        this.url = url;
        return this;
    }

    public A progress(ProgressCallback progressCallback) {
        this.downlaodProgressCallback = progressCallback;
        return this;
    }


    public B write(File file) {
        B b = new B(this, file);
        return b;
    }

    public A setJsonPojoBody(Object request) {
        this.request = gson.toJson(request);
        return this;
    }

    public String getRequest() {
        return request;
    }

    public String getUrl() {
        return url;
    }

    public C<String> asString() {
        isString = true;
        return new C<String>(this, String.class);
    }

    public A uploadProgress(ProgressCallback progressCallback) {
        this.uploadProgressCallback = progressCallback;

        return this;
    }

    public A addMultipartParts(List<Part> parts) {
        this.parts = parts;

        return this;
    }


    public C<JsonElement> asJsonElement() {
        C<JsonElement> c = new C<JsonElement>(this, JsonElement.class);
        isString = false;
        return c;
    }

    public <T> C<T> as(Class<T> t) {
        C<T> c = new C<T>(this, t);
        isString = false;
        return c;
    }

    public List<Part> getParts() {
        return parts;
    }

    public A setMultipartFile(String key, File file) {
        parts.add(new FilePart(key, file));

        return this;
    }

    public A setHeader(Headers headers) {
        this.headers = headers;
        return this;
    }

    public A setConnectTimeout(long timeout, TimeUnit unit){
        connectTimeout = timeout;
        connectTimeUnit = unit;

        return this;
    }

    public A setReadTimeout(long timeout, TimeUnit unit){
        readTimeout = timeout;
        readTimeUnit = unit;

        return this;
    }

    void createClient() {
        client = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, connectTimeUnit)
                .readTimeout(readTimeout, readTimeUnit)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(),
                                        downlaodProgressCallback))
                                .build();
                    }
                })
                .build();
    }

}
