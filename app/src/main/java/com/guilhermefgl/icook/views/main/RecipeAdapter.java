package com.guilhermefgl.icook.views.main;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.databinding.MainRecipeItemBinding;
import com.guilhermefgl.icook.models.Recipe;
import com.guilhermefgl.icook.viewmodels.ReciperViewModel;

import java.lang.ref.WeakReference;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private List<Recipe> mRecipes;
    private final WeakReference<EventHandler> mEventHandler;

    RecipeAdapter(EventHandler eventHandler) {
        mEventHandler = new WeakReference<>(eventHandler);
    }

    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainRecipeItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.main_recipe_item, parent, false);
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

    void setRecipes(List<Recipe> recipes) {
        this.mRecipes = recipes;
        notifyDataSetChanged();
    }

    class RecipeAdapterViewHolder extends RecyclerView.ViewHolder {

        private MainRecipeItemBinding mBinding;

        RecipeAdapterViewHolder(MainRecipeItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            binding.setViewModel(new ReciperViewModel());
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
