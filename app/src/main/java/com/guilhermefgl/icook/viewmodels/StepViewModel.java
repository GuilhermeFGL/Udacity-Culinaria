package com.guilhermefgl.icook.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.guilhermefgl.icook.models.Step;

public class StepViewModel extends BaseObservable {

    private Step mStep;

    @NonNull
    private final ObservableField<String> oStepTitle = new ObservableField<>();
    @NonNull
    private final ObservableField<String> oStepDescription = new ObservableField<>();

    public void setModel(Step step) {
        mStep = step;
        oStepTitle.set(step.getShortDescription());
        oStepDescription.set(step.getDescription());
    }

    @Bindable
    public Step getModel() {
        return mStep;
    }

    @Bindable
    public ObservableField<String> getTitle() {
        return oStepTitle;
    }

    @Bindable
    public ObservableField<String> getDescription() {
        return oStepDescription;
    }

}
