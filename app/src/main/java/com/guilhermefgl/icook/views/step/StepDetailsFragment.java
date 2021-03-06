package com.guilhermefgl.icook.views.step;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.databinding.FragmentDetailsStepBinding;
import com.guilhermefgl.icook.models.entitys.Step;
import com.guilhermefgl.icook.viewmodels.StepViewModel;
import com.guilhermefgl.icook.views.BaseActivity;
import com.guilhermefgl.icook.views.BaseFragment;

import java.util.List;

public class StepDetailsFragment extends BaseFragment
        implements StepViewModel.PlayerLifeCycle, StepViewModel.EventHandler {

    public static final String BUNDLE_STEPS = StepDetailsFragment.class.getName().concat(".BUNDLE_STEPS");
    public static final String BUNDLE_STEP_ID = StepDetailsFragment.class.getName().concat(".BUNDLE_STEP_ID");
    private static final String STATE_PLAYER = StepDetailsFragment.class.getName().concat(".STATE_PLAYER");
    private static final String STATE_STEP = StepDetailsFragment.class.getName().concat(".STATE_STEP");

    private FragmentDetailsStepBinding mBinding;
    private StepViewModel mViewModel;
    private List<Step> mSteps;
    private Step mCurrentStep;
    private Integer mCurrentStepId;

    private ExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private Long mPlayerPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey(BUNDLE_STEPS)) {
                mSteps = getArguments().getParcelableArrayList(BUNDLE_STEPS);
            }
            if (getArguments().containsKey(BUNDLE_STEP_ID)) {
                mCurrentStepId = getArguments().getInt(BUNDLE_STEP_ID);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_details_step, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_STEP)) {
                mCurrentStepId = savedInstanceState.getInt(STATE_STEP);
            }
            if (savedInstanceState.containsKey(STATE_PLAYER)) {
                mPlayerPosition = savedInstanceState.getLong(STATE_PLAYER);
            }
        }

        if (mSteps != null && !mSteps.isEmpty() && mCurrentStepId != null) {
            mCurrentStep = Step.getStepById(mSteps, mCurrentStepId);
        }

        if (mCurrentStep != null) {
            mViewModel = new StepViewModel(this);
            mBinding.setViewModel(mViewModel);
            mBinding.setEventHandler(this);
            mViewModel.setSteps(mSteps, mCurrentStep.getId());
        }

        setupToolbar();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_STEP, mCurrentStep.getId());
        if (mExoPlayer != null) {
            outState.putLong(STATE_PLAYER, mExoPlayer.getCurrentPosition());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releasePlayer();
    }

    @Override
    public void onSetupPlayer(SimpleExoPlayer exoPlayer) {
        if (isAdded()) {
            mExoPlayer = exoPlayer;
            mExoPlayer.addListener(new MyPlayerListener());
            if (mMediaSession == null) {
                initializeMediaSession();
            }
            if (mPlayerPosition != null) {
                mExoPlayer.seekTo(mPlayerPosition);
                mPlayerPosition = null;
            }
        }
    }

    @Override
    public void onNextStepClick() {
        mCurrentStep = mViewModel.nextStep();
        setupToolbar();
        releasePlayer();
    }

    @Override
    public void onPrevStepClick() {
        mCurrentStep = mViewModel.prevStep();
        setupToolbar();
        releasePlayer();
    }

    private void setupToolbar() {
        if (mCurrentStep != null) {
            BaseActivity activity = (BaseActivity) this.getActivity();
            if (activity != null) {
                activity.setTitle(mCurrentStep.getShortDescription());
            }
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
        mExoPlayer = null;
    }

    private void initializeMediaSession() {
        if (getContext() != null) {
            mMediaSession = new MediaSessionCompat(getContext(), this.getClass().getSimpleName());
            mMediaSession.setFlags(
                    MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                            MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
            mMediaSession.setMediaButtonReceiver(null);

            mStateBuilder = new PlaybackStateCompat.Builder()
                    .setActions(
                            PlaybackStateCompat.ACTION_PLAY |
                                    PlaybackStateCompat.ACTION_PAUSE |
                                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                    PlaybackStateCompat.ACTION_PLAY_PAUSE);

            mMediaSession.setPlaybackState(mStateBuilder.build());
            mMediaSession.setCallback(new MySessionCallback());
            mMediaSession.setActive(true);
        }
    }

    private class MyPlayerListener implements Player.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) { }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) { }

        @Override
        public void onLoadingChanged(boolean isLoading) { }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (mExoPlayer != null) {
                if ((playbackState == Player.STATE_READY) && playWhenReady) {
                    mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                            mExoPlayer.getCurrentPosition(), 1f);
                } else if ((playbackState == Player.STATE_READY)) {
                    mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                            mExoPlayer.getCurrentPosition(), 1f);
                }
                mMediaSession.setPlaybackState(mStateBuilder.build());
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) { }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) { }

        @Override
        public void onPlayerError(ExoPlaybackException error) { }

        @Override
        public void onPositionDiscontinuity(int reason) {  }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) { }

        @Override
        public void onSeekProcessed() { }
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            if (mExoPlayer != null) {
                mExoPlayer.setPlayWhenReady(true);
            }
        }

        @Override
        public void onPause() {
            if (mExoPlayer != null) {
                mExoPlayer.setPlayWhenReady(false);
            }
        }

        @Override
        public void onSkipToPrevious() {
            if (mExoPlayer != null) {
                mExoPlayer.seekTo(0);
            }
        }
    }
}
