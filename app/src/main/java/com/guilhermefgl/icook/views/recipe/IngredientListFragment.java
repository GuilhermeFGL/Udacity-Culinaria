package com.guilhermefgl.icook.views.recipe;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.databinding.FragmentListIngredientBinding;
import com.guilhermefgl.icook.models.entitys.Ingredient;
import com.guilhermefgl.icook.views.BaseFragment;

import java.util.List;

public class IngredientListFragment extends BaseFragment {

    public static final String BUNDLE_INGREDIENTS = IngredientListFragment.class.getName().concat(".BUNDLE_INGREDIENTS");

    private List<Ingredient> mIngredients;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(BUNDLE_INGREDIENTS)) {
            mIngredients = getArguments().getParcelableArrayList(BUNDLE_INGREDIENTS);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentListIngredientBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_list_ingredient, container, false);

        if (mIngredients != null) {
            binding.ingredientList.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.ingredientList.setAdapter(new IngredientAdapter(mIngredients));
        }

        return binding.getRoot();
    }
}
