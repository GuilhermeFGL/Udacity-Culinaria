package com.guilhermefgl.icook.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.guilhermefgl.icook.BR;
import com.guilhermefgl.icook.BuildConfig;
import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.helpers.PicassoHelper;
import com.guilhermefgl.icook.models.Step;

import java.util.List;

public class StepViewModel extends BaseObservable {

    private List<Step> mSteps;
    private Step mStep;
    private int mStepsPosition;
    private static PlayerLifeCycle mPlayerLifeCycle;

    @NonNull
    private final ObservableField<String> oStepId = new ObservableField<>();
    @NonNull
    private final ObservableField<String> oStepTitle = new ObservableField<>();
    @NonNull
    private final ObservableField<String> oStepDescription = new ObservableField<>();
    @NonNull
    private final ObservableField<String> oStepThumbnail = new ObservableField<>();
    @NonNull
    private final ObservableField<String> oStepVideo = new ObservableField<>();

    public StepViewModel() { }

    public StepViewModel(PlayerLifeCycle playerLifeCycle) {
        mPlayerLifeCycle = playerLifeCycle;
    }

    public void setSteps(List<Step> steps, Integer stepsId) {
        if (steps != null && !steps.isEmpty() && stepsId != null) {
            mSteps = steps;
            mStepsPosition = Step.getPositionById(steps, stepsId);
            if (mStepsPosition > -1) {
                setModel(steps.get(mStepsPosition));
            }
        }
    }

    public Step nextStep() {
        if (isNextStepEnable()) {
            Step currentStep = mSteps.get(++mStepsPosition);
            setModel(currentStep);
            return currentStep;
        }
        return null;
    }

    public Step prevStep() {
        if (isPrevStepEnable()) {
            Step currentStep = mSteps.get(--mStepsPosition);
            setModel(currentStep);
            return currentStep;
        }
        return null;
    }

    public void setModel(Step step) {
        mStep = step;
        oStepId.set(String.valueOf(step.getId()));
        oStepTitle.set(step.getShortDescription());
        oStepDescription.set(step.getDescription());
        oStepThumbnail.set(step.getThumbnailURL());
        oStepVideo.set(step.getVideoURL());
        notifyPropertyChanged(BR.thumbnailUrl);
        notifyPropertyChanged(BR.videoUrl);
        notifyPropertyChanged(BR.hasThumbnail);
        notifyPropertyChanged(BR.hasVideo);
        notifyPropertyChanged(BR.nextStepEnable);
        notifyPropertyChanged(BR.prevStepEnable);
    }

    @Bindable
    public Step getModel() {
        return mStep;
    }

    @Bindable
    public ObservableField<String> getId() {
        return oStepId;
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
        return !isHasVideo() && oStepThumbnail.get() != null && !oStepThumbnail.get().isEmpty();
    }

    @Bindable
    public boolean isHasVideo() {
        return oStepVideo.get() != null && !oStepVideo.get().isEmpty();
    }

    @Bindable
    public boolean isNextStepEnable() {
        return mSteps != null && mStepsPosition < mSteps.size() - 1;
    }

    @Bindable
    public boolean isPrevStepEnable() {
        return mSteps != null && mStepsPosition  > 0;
    }

    @Bindable
    public String getThumbnailUrl() {
        return oStepThumbnail.get();
    }

    @Bindable
    public String getVideoUrl() {
        return oStepVideo.get();
    }

    @BindingAdapter("bind:thumbnailUrl")
    public static void loadThumbnail(ImageView view, String imageUrl) {
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

    @BindingAdapter("bind:videoUrl")
    public static void loadVideo(PlayerView view, String videoURL) {
        if (videoURL == null || videoURL.isEmpty()) {
            return;
        }

        Context context = view.getContext();
        Uri videoUri = Uri.parse(videoURL);
        DefaultDataSourceFactory sourceFactory = new DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, BuildConfig.APPLICATION_ID),
                new DefaultBandwidthMeter());

        SimpleExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
        exoPlayer.prepare(new ExtractorMediaSource.Factory(sourceFactory).createMediaSource(videoUri));
        exoPlayer.setPlayWhenReady(true);

        view.setDefaultArtwork(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_placeholder));
        view.setPlayer(exoPlayer);

        if (mPlayerLifeCycle != null) {
            mPlayerLifeCycle.onSetupPlayer(exoPlayer);
        }
    }

    public interface PlayerLifeCycle {
        void onSetupPlayer(SimpleExoPlayer exoPlayer);
    }

    public interface EventHandler {
        void onNextStepClick();
        void onPrevStepClick();
    }
}
