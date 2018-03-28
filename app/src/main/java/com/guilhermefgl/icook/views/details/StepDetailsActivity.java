package com.guilhermefgl.icook.views.details;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.databinding.ActivityStepDetailBinding;
import com.guilhermefgl.icook.models.Step;
import com.guilhermefgl.icook.views.BaseActivity;

public class StepDetailsActivity extends AppCompatActivity {

    public static final String BUNDLE_STEP =  StepDetailsActivity.class.getName().concat(".BUNDLE_STEP");

    public static void startActivity(BaseActivity activity, Bundle extras) {
        activity.startActivity(new Intent(activity, StepDetailsActivity.class).putExtras(extras));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStepDetailBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_step_detail);
        setSupportActionBar(binding.listToolbar);

        if (getIntent().getExtras() != null && getIntent().hasExtra(BUNDLE_STEP)) {
            Step step = getIntent().getParcelableExtra(BUNDLE_STEP);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(step.getShortDescription());
            }

            Bundle arguments = new Bundle();
            arguments.putParcelable(StepDetailsFragment.BUNDLE_STEP, step);
            StepDetailsFragment fragment = new StepDetailsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            finish();
        }
    }
}
