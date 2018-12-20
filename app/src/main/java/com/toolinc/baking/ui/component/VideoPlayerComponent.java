package com.toolinc.baking.ui.component;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.Strings;
import com.toolinc.baking.BakingApplication;
import com.toolinc.baking.R;
import com.toolinc.baking.databinding.FragmentRecipeStepByStepBinding;
import com.toolinc.baking.lifecycle.VideoPlayerViewModel;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import static com.google.common.base.Preconditions.checkNotNull;

public class VideoPlayerComponent implements LifecycleObserver, Player.EventListener {

  private final Context context;
  private final FragmentRecipeStepByStepBinding fragmentBinding;
  private final VideoPlayerViewModel videoPlayerViewModel;

  private SimpleExoPlayer mExoPlayer;
  private DefaultTrackSelector mTrackSelector;

  public VideoPlayerComponent(
      Context context,
      FragmentRecipeStepByStepBinding fragmentRecipeStepByStepBinding,
      VideoPlayerViewModel videoPlayerViewModel) {
    this.context = checkNotNull(context, "Context is missing.");
    this.fragmentBinding =
        checkNotNull(
            fragmentRecipeStepByStepBinding, "FragmentRecipeStepByStepBinding is missing.");
    this.videoPlayerViewModel =
        checkNotNull(videoPlayerViewModel, "VideoPlayerViewModel is missing.");
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  public void onCreate() {
    fragmentBinding.pvVideo.requestFocus();
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  public void onStart() {
    if (Util.SDK_INT > 23) {
      initializeVideo();
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  public void onStop() {
    if (Util.SDK_INT > 23) {
      releasePlayer();
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  public void onPause() {
    if (Util.SDK_INT <= 23) {
      releasePlayer();
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  public void onResume() {
    if ((Util.SDK_INT <= 23)) {
      initializeVideo();
    }
  }

  @Override
  public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    if (playbackState == Player.STATE_BUFFERING) {
      setPlayerState(false, View.VISIBLE);
    } else if (playbackState == Player.STATE_READY) {
      setPlayerState(true, View.INVISIBLE);
    }
  }

  @Override
  public void onPlayerError(ExoPlaybackException e) {
    if (!BakingApplication.isInternetAvailable(context)) {
      showPlayerErrorMessage(context.getString(R.string.error_no_internet));
    } else {
      showPlayerErrorMessage(context.getString(R.string.error_loading_video));
    }
  }

  private void initializeVideo() {
    if (BakingApplication.isInternetAvailable(context)) {
      initializePlayer();
    } else {
      showPlayerErrorMessage(context.getString(R.string.error_no_internet));
    }
  }

  private void initializePlayer() {
    if (mExoPlayer == null && !Strings.isNullOrEmpty(videoPlayerViewModel.getVideoUrl())) {
      mTrackSelector = new DefaultTrackSelector();
      mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, mTrackSelector);
      mExoPlayer.addListener(this);
      String userAgent = Util.getUserAgent(context, context.getString(R.string.app_name));
      MediaSource mediaSource =
          new ExtractorMediaSource.Factory(
                  new DefaultDataSourceFactory(
                      context, null, new DefaultHttpDataSourceFactory(userAgent, null)))
              .createMediaSource(Uri.parse(videoPlayerViewModel.getVideoUrl()));
      fragmentBinding.pvVideo.setPlayer(mExoPlayer);
      mExoPlayer.prepare(mediaSource);
    } else {
      showPlayerErrorMessage(context.getString(R.string.error_no_video));
    }
  }

  public void releasePlayer() {
    if (fragmentBinding.pvVideo != null && mExoPlayer != null) {
      mExoPlayer.stop();
      mExoPlayer.release();
      mExoPlayer = null;
      mTrackSelector = null;
    }
  }

  private void setPlayerState(boolean isPlayerControlEnabled, int loadingIndicatorVisibility) {
    fragmentBinding.pbVideo.setVisibility(loadingIndicatorVisibility);
    fragmentBinding.pcvVideo.setEnabled(isPlayerControlEnabled);
    fragmentBinding.pcvVideo.setEnabled(isPlayerControlEnabled);
  }

  private void setErrorViewsVisibility(boolean isPlayerControlEnabled, int visibility) {
    fragmentBinding.pcvVideo.setEnabled(isPlayerControlEnabled);
  }

  private void showPlayerErrorMessage(String error) {
    Snackbar.make(fragmentBinding.pcvVideo, error, Snackbar.LENGTH_LONG).show();
  }
}
