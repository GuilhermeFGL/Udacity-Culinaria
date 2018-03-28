package com.guilhermefgl.icook.views.details;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.databinding.FragmentDetailsStepBinding;
import com.guilhermefgl.icook.models.Step;
import com.guilhermefgl.icook.viewmodels.StepViewModel;

public class StepDetailsFragment extends Fragment {

    public static final String BUNDLE_STEP = StepDetailsFragment.class.getName().concat(".BUNDLE_STEP");

    private Step mStep;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(BUNDLE_STEP)) {
            mStep = getArguments().getParcelable(BUNDLE_STEP);

            Activity activity = this.getActivity();
            if (activity != null) {
                CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(mStep.getShortDescription());
                }
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDetailsStepBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_details_step, container, false);

        binding.setViewModel(new StepViewModel());
        binding.getViewModel().setModel(mStep);

        return binding.getRoot();
    }
}
