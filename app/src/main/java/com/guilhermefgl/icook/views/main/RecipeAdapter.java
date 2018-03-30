package com.guilhermefgl.icook.views.main;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.databinding.ItemMainRecipeBinding;
import com.guilhermefgl.icook.models.Recipe;
import com.guilhermefgl.icook.viewmodels.RecipeViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private ArrayList<Recipe> mRecipes;
    private final WeakReference<EventHandler> mEventHandler;

    RecipeAdapter(EventHandler eventHandler) {
        mEventHandler = new WeakReference<>(eventHandler);
    }

    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMainRecipeBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_main_recipe, parent, false);
        binding.setEventHandler(mEventHandler);
        return new RecipeAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder holder, int position) {
        holder.setModel(mRecipes.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecipes != null ? mRecipes.size() : 0;
    }

    void setRecipes(ArrayList<Recipe> recipes) {
        this.mRecipes = recipes;
        notifyDataSetChanged();
    }

    class RecipeAdapterViewHolder extends RecyclerView.ViewHolder {

        private final ItemMainRecipeBinding mBinding;

        RecipeAdapterViewHolder(ItemMainRecipeBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            binding.setViewModel(new RecipeViewModel());
        }

        void setModel(Recipe model) {
            if (mBinding.getViewModel() != null) {
                mBinding.getViewModel().setModel(model);
                mBinding.executePendingBindings();
            }
        }
    }

    public interface EventHandler {
        void onItemClick(Recipe recipe);
    }
}
