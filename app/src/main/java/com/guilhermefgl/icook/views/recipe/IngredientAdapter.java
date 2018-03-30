package com.guilhermefgl.icook.views.recipe;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.databinding.ItemRecipeIngredientBinding;
import com.guilhermefgl.icook.models.Ingredient;
import com.guilhermefgl.icook.viewmodels.IngredientViewModel;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final List<Ingredient> mIngredients;

    IngredientAdapter(@NonNull List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecipeIngredientBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_recipe_ingredient, parent, false);
        return new IngredientAdapter.IngredientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.setModel(mIngredients.get(position));
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        private final ItemRecipeIngredientBinding mBinding;

        IngredientViewHolder(ItemRecipeIngredientBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            binding.setViewModel(new IngredientViewModel());
        }

        void setModel(Ingredient model) {
            if (mBinding.getViewModel() != null) {
                mBinding.getViewModel().setModel(model);
                mBinding.executePendingBindings();
            }
        }
    }
}
