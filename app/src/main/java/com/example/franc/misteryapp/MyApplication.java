package com.example.franc.misteryapp;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by franc on 08/11/2017.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());

    }
}
