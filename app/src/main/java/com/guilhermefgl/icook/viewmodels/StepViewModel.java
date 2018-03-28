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
import com.guilhermefgl.icook.models.Step;

public class StepViewModel extends BaseObservable {

    private Step mStep;

    @NonNull
    private final ObservableField<String> oStepTitle = new ObservableField<>();
    @NonNull
    private final ObservableField<String> oStepDescription = new ObservableField<>();
    @NonNull
    private final ObservableField<String> oStepThumbnail = new ObservableField<>();
    @NonNull
    private final ObservableField<String> oStepVideo = new ObservableField<>();

    public void setModel(Step step) {
        mStep = step;
        oStepTitle.set(step.getShortDescription());
        oStepDescription.set(step.getDescription());
        oStepThumbnail.set(step.getThumbnailURL());
        oStepVideo.set(step.getVideoURL());
        notifyPropertyChanged(BR.thumbnailUrl);
        notifyPropertyChanged(BR.videoUrl);
        notifyPropertyChanged(BR.hasThumbnail);
        notifyPropertyChanged(BR.hasVideo);
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

    @Bindable
    public boolean isHasThumbnail() {
        return oStepThumbnail.get() != null && !oStepThumbnail.get().isEmpty();
    }

    @Bindable
    public boolean isHasVideo() {
        return oStepVideo.get() != null && !oStepVideo.get().isEmpty();
    }

    @Bindable
    public String getThumbnailUrl() {
        return oStepThumbnail.get();
    }

    @Bindable
    public String getVideoUrl() {
        return oStepVideo.get();
    }

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            PicassoHelper.loadImage(
                    view.getContext(),
                    imageUrl,
                    view,
                    R.drawable.icon_placeholder,
                    R.drawable.icon_connection_error);
        } else {
            view.setImageResource(R.drawable.icon_placeholder);
        }
    }

}
