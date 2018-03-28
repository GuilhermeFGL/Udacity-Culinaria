package com.guilhermefgl.icook.views.details;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.databinding.ActivityDetailsBinding;
import com.guilhermefgl.icook.models.Recipe;
import com.guilhermefgl.icook.models.Step;
import com.guilhermefgl.icook.views.BaseActivity;

public class DetailsActivity extends BaseActivity implements StepAdapter.EventHandler {

    public static final String BUNDLE_RECIPE =  DetailsActivity.class.getName().concat(".BUNDLE_RECIPE");

    private boolean isTabletView;

    public static void startActivity(BaseActivity activity, Bundle extras) {
        activity.startActivity(new Intent(activity, DetailsActivity.class).putExtras(extras));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailsBinding mBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_details);
        setSupportActionBar(mBinding.listToolbar);

        isTabletView = mBinding.getRoot().findViewById(R.id.item_detail_container) != null;
        if (getIntent().getExtras() != null && getIntent().hasExtra(BUNDLE_RECIPE)) {
            Recipe recipe = getIntent().getParcelableExtra(BUNDLE_RECIPE);
            mBinding.listToolbar.setTitle(recipe.getName());
            mBinding.include.itemList.setLayoutManager(new LinearLayoutManager(this));
            mBinding.include.itemList.setAdapter(new StepAdapter(recipe.getSteps(), this));
        } else {
            finish();
        }
    }

    @Override
    public void onItemClick(Step step) {
        if (isTabletView) {
//            Bundle arguments = new Bundle();
//            arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
//            ItemDetailFragment fragment = new ItemDetailFragment();
//            fragment.setArguments(arguments);
//            mParentActivity.getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.item_detail_container, fragment)
//                    .commit();
        } else {
//            Intent intent = new Intent(context, ItemDetailActivity.class);
//            intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);
//            context.startActivity(intent);
        }
    }
}
