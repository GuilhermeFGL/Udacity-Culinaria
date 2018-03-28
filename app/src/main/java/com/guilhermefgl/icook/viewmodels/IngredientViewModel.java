package com.guilhermefgl.icook.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.guilhermefgl.icook.BR;
import com.guilhermefgl.icook.models.Ingredient;

public class IngredientViewModel extends BaseObservable {

    @NonNull
    private final ObservableField<String> oIngredientName = new ObservableField<>();
    @NonNull
    private final ObservableField<String> oIngredientQuantity = new ObservableField<>();
    @NonNull
    private final ObservableField<String> oIngredientMeasure = new ObservableField<>();

    public void setModel(Ingredient ingredient) {
        oIngredientName.set(ingredient.getIngredient());
        oIngredientQuantity.set(String.valueOf(ingredient.getQuantity()));
        oIngredientMeasure.set(ingredient.getMeasure());
        notifyPropertyChanged(BR.quantity);
        notifyPropertyChanged(BR.measure);
    }

    @Bindable
    public ObservableField<String> getIngredient() {
        return oIngredientName;
    }

    @Bindable
    public String getQuantity() {
        return oIngredientQuantity.get();
    }

    @Bindable
    public String getMeasure() {
        return oIngredientMeasure.get();
    }
}
