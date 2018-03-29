package com.guilhermefgl.icook.views.step;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.guilhermefgl.icook.models.Step;
import com.guilhermefgl.icook.viewmodels.StepViewModel;
import com.guilhermefgl.icook.views.BaseFragment;

public class StepDetailsFragment extends BaseFragment implements StepViewModel.PlayerLifeCycle, StepViewModel.EventHandler {

    public static final String BUNDLE_STEP = StepDetailsFragment.class.getName().concat(".BUNDLE_STEP");
    public static final String STATE_PLAYER = StepDetailsFragment.class.getName().concat(".STATE_PLAYER");

    private StepViewModel mViewModel;
    private Step mStep;
    private ExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private Long mPlayerPosition;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_PLAYER)) {
            mPlayerPosition = savedInstanceState.getLong(STATE_PLAYER);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(BUNDLE_STEP)) {
            mStep = getArguments().getParcelable(BUNDLE_STEP);
            setupToolbar();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDetailsStepBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_details_step, container, false);

        mViewModel = new StepViewModel(this);
        binding.setViewModel(mViewModel);
        binding.setEventHandler(this);
        mViewModel.setModel(mStep);

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer != null) {
            outState.putLong(STATE_PLAYER, mExoPlayer.getCurrentPosition());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releasePlayer();
        mExoPlayer = null;
    }

    @Override
    public void onSetupPlayer(SimpleExoPlayer exoPlayer) {
        if (isAdded()) {
            mExoPlayer = exoPlayer;
            mExoPlayer.addListener(new MyPlayerListinner());
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
        mStep = mViewModel.nextStep();
        setupToolbar();
    }

    @Override
    public void onPrevStepClick() {
        mStep = mViewModel.prevStep();
        setupToolbar();
    }

    private void setupToolbar() {
        if (mStep != null) {
            Activity activity = this.getActivity();
            if (activity != null) {
                CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(mStep.getShortDescription());
                }
            }
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
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

    private class MyPlayerListinner implements Player.EventListener {

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
