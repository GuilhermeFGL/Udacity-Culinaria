package com.guilhermefgl.icook.views.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.databinding.MainActivityBinding;
import com.guilhermefgl.icook.views.BaseActivity;

public class MainActivity extends BaseActivity {

    MainActivityBinding mBinding;

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(
                new Intent(activity, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        setSupportActionBar(mBinding.mainToolbar);
    }
}
