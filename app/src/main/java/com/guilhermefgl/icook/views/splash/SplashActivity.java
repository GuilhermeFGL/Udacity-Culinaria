package com.guilhermefgl.icook.views.splash;

import android.os.Bundle;

import com.guilhermefgl.icook.views.BaseActivity;
import com.guilhermefgl.icook.views.main.MainActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.startActivity(this);
    }
}
