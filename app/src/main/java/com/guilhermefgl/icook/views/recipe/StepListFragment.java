package com.guilhermefgl.icook.views.recipe;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.databinding.FragmentListStepBinding;
import com.guilhermefgl.icook.models.entitys.Step;
import com.guilhermefgl.icook.views.BaseActivity;
import com.guilhermefgl.icook.views.BaseFragment;
import com.guilhermefgl.icook.views.step.StepDetailsActivity;
import com.guilhermefgl.icook.views.step.StepDetailsFragment;

import java.util.ArrayList;

public class StepListFragment extends BaseFragment implements StepAdapter.EventHandler {

    public static final String BUNDLE_STEPS = StepListFragment.class.getName().concat(".BUNDLE_STEP");

    private ArrayList<Step> mSteps;
    private boolean isTabletView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(BUNDLE_STEPS)) {
            mSteps = getArguments().getParcelableArrayList(BUNDLE_STEPS);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentListStepBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_list_step, container, false);

        isTabletView = binding.getRoot().findViewById(R.id.item_detail_container) != null;
        if (mSteps != null && getContext() != null) {
            binding.include.stepList.setLayoutManager(
                    new LinearLayoutManager(getActivity()));
            binding.include.stepList.addItemDecoration(
                    new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            binding.include.stepList.setAdapter(
                    new StepAdapter(mSteps, this));
        }
        return binding.getRoot();
    }

    @Override
    public void onItemClick(Step step) {
        if (isTabletView) {
            Bundle arguments = new Bundle();
            arguments.putParcelableArrayList(StepDetailsFragment.BUNDLE_STEPS, mSteps);
            arguments.putInt(StepDetailsFragment.BUNDLE_STEP_ID, step.getId());
            StepDetailsFragment fragment =  new StepDetailsFragment();
            fragment.setArguments(arguments);
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            }
        } else {
            Bundle extras = new Bundle();
            extras.putParcelableArrayList(StepDetailsActivity.BUNDLE_STEPS, mSteps);
            extras.putInt(StepDetailsActivity.BUNDLE_STEP_ID, step.getId());
            StepDetailsActivity.startActivity(((BaseActivity) getActivity()), extras);
        }
    }
}
