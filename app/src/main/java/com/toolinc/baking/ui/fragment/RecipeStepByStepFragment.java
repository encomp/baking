package com.toolinc.baking.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.common.base.Optional;
import com.toolinc.baking.R;
import com.toolinc.baking.client.model.Recipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/** Renders a specific a set of instructions with their media information. */
public final class RecipeStepByStepFragment extends Fragment implements Player.EventListener {

  private static final String TAG = RecipeStepByStepFragment.class.getSimpleName();
  private Recipe recipe;
  private MediaSessionCompat mediaSession;
  private PlaybackStateCompat.Builder builder;
  private SimpleExoPlayer mExoPlayer;

  @BindView(R.id.tv_description)
  TextView tvDescription;

  @BindView(R.id.pv_recipe)
  PlayerView pvRecipe;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_recipe_step_by_step, container, false);
    ButterKnife.bind(this, view);
    mediaSession = new MediaSessionCompat(getContext(), TAG);
    mediaSession.setFlags(
        MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
            | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
    builder =
        new PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY
                    | PlaybackStateCompat.ACTION_PAUSE
                    | PlaybackStateCompat.ACTION_PLAY_PAUSE
                    | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
    mediaSession.setPlaybackState(builder.build());
    mediaSession.setCallback(
        new MediaSessionCompat.Callback() {

          @Override
          public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
          }

          @Override
          public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
          }

          @Override
          public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
          }
        });
    mediaSession.setActive(true);
    return view;
  }

  @Override
  public void setArguments(@Nullable Bundle bundle) {
    if (Optional.fromNullable(bundle).isPresent()) {
      recipe = (Recipe) bundle.getSerializable(Intent.EXTRA_KEY_EVENT);
    }
  }

  /**
   * Initialize ExoPlayer.
   *
   * @param mediaUri The URI of the sample to play.
   */
  private void initializePlayer(Uri mediaUri) {
    if (mExoPlayer == null) {

      mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());
      // Set the ExoPlayer.EventListener to this activity.
      mExoPlayer.addListener(this);
      pvRecipe.setPlayer(mExoPlayer);

      // Play
      mExoPlayer.setPlayWhenReady(true);
    }
  }

  /** Release the player when the activity is destroyed. */
  @Override
  public void onDestroy() {
    super.onDestroy();
    mediaSession.setActive(false);
    releasePlayer();
  }

  /** Release ExoPlayer. */
  private void releasePlayer() {
    mExoPlayer.stop();
    mExoPlayer.release();
    mExoPlayer = null;
  }

  // ExoPlayer Event Listeners
  @Override
  public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    if ((playbackState == Player.STATE_READY) && playWhenReady) {
      builder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1f);
      mediaSession.setPlaybackState(builder.build());
      Log.d(TAG, "onPlayerStateChanged: PLAYING");
    } else if ((playbackState == Player.STATE_READY)) {
      builder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 1f);
      mediaSession.setPlaybackState(builder.build());
      Log.d(TAG, "onPlayerStateChanged: PAUSED");
    }
  }

  @Override
  public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {}

  @Override
  public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

  @Override
  public void onLoadingChanged(boolean isLoading) {}

  @Override
  public void onRepeatModeChanged(int repeatMode) {}

  @Override
  public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {}

  @Override
  public void onPlayerError(ExoPlaybackException error) {}

  @Override
  public void onPositionDiscontinuity(int reason) {}

  @Override
  public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}

  @Override
  public void onSeekProcessed() {}
}
