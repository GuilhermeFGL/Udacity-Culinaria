package com.guilhermefgl.icook;

import android.app.Application;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class ICookApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        setPicasso();
        if (BuildConfig.DEBUG) {
            SQLiteStudioService.instance().start(this);
        }
    }

    public void setPicasso() {
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setLoggingEnabled(BuildConfig.DEBUG);
        Picasso.setSingletonInstance(built);
    }
}
