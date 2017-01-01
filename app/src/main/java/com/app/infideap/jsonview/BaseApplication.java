package com.app.infideap.jsonview;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Shiburagi on 31/12/2016.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

//        getString()
    }
}
