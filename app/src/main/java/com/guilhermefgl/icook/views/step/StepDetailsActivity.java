package com.guilhermefgl.icook.views.step;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.databinding.ActivityStepDetailBinding;
import com.guilhermefgl.icook.models.Step;
import com.guilhermefgl.icook.views.BaseActivity;

public class StepDetailsActivity extends AppCompatActivity {

    public static final String BUNDLE_STEP =  StepDetailsActivity.class.getName().concat(".BUNDLE_STEP");
    public static final String STATE_FRAGMENT = StepDetailsFragment.class.getName();

    private StepDetailsFragment stepDetailsFragment;

    public static void startActivity(BaseActivity activity, Bundle extras) {
        activity.startActivity(new Intent(activity, StepDetailsActivity.class).putExtras(extras));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStepDetailBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_step_detail);
        setSupportActionBar(binding.stepToolbar);

        if (getIntent().getExtras() != null && getIntent().hasExtra(BUNDLE_STEP)) {
            Step step = getIntent().getParcelableExtra(BUNDLE_STEP);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(step.getShortDescription());
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            if (savedInstanceState != null && savedInstanceState.containsKey(STATE_FRAGMENT)) {
                stepDetailsFragment = (StepDetailsFragment) getSupportFragmentManager()
                        .getFragment(savedInstanceState, STATE_FRAGMENT);
            }
            if (stepDetailsFragment == null) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(StepDetailsFragment.BUNDLE_STEP, step);
                stepDetailsFragment = new StepDetailsFragment();
                stepDetailsFragment.setArguments(arguments);
            }
            if (!stepDetailsFragment.isAdded()) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.item_detail_container, stepDetailsFragment)
                        .commit();
            }
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, STATE_FRAGMENT, stepDetailsFragment);
    }
}
