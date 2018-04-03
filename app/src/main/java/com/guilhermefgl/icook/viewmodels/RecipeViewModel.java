package com.guilhermefgl.icook.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.guilhermefgl.icook.BR;
import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.helpers.PicassoHelper;
import com.guilhermefgl.icook.models.entitys.Recipe;

public class RecipeViewModel extends BaseObservable {

    private Recipe mRecipe;

    @NonNull
    private final ObservableField<String> oRecipeTitle = new ObservableField<>();
    @NonNull
    private final ObservableField<Integer> oRecipeServing = new ObservableField<>();
    @NonNull
    private final ObservableField<String> oRecipeImage = new ObservableField<>();

    public void setModel(Recipe recipe) {
        mRecipe = recipe;
        oRecipeTitle.set(recipe.getName());
        oRecipeServing.set(recipe.getServings());
        oRecipeImage.set(recipe.getImage());
        notifyPropertyChanged(BR.serving);
        notifyPropertyChanged(BR.image);
    }

    @Bindable
    public Recipe getModel() {
        return mRecipe;
    }

    @Bindable
    public ObservableField<String> getTitle() {
        return oRecipeTitle;
    }

    @Bindable
    public Integer getServing() {
        return oRecipeServing.get();
    }

    @Bindable
    public String getImage() {
        return oRecipeImage.get();
    }

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            PicassoHelper.loadImage(view.getContext(), imageUrl, view);
        } else {
            view.setImageResource(R.drawable.icon_placeholder);
        }
    }
}
