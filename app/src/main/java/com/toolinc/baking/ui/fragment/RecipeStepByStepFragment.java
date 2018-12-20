package com.toolinc.baking.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Optional;
import com.toolinc.baking.R;
import com.toolinc.baking.client.model.Step;
import com.toolinc.baking.databinding.FragmentRecipeStepByStepBinding;
import com.toolinc.baking.lifecycle.VideoPlayerViewModel;
import com.toolinc.baking.ui.RecipeDetailActivity;
import com.toolinc.baking.ui.component.VideoPlayerComponent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

/** Renders a specific a set of instructions with their media information. */
public final class RecipeStepByStepFragment extends Fragment {

  private static final String TAG = RecipeStepByStepFragment.class.getSimpleName();
  private static final String STEP_ARG = "STEP";
  private Step step;
  private StepNavigationHandler stepNavigationHandler;
  private VideoPlayerViewModel mVideoPlayerViewModel;
  private VideoPlayerComponent videoPlayerComponent;
  private RecipeDetailActivity mActivity;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    FragmentRecipeStepByStepBinding fragmentRecipeStepByStepBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_step_by_step, container, false);

    fragmentRecipeStepByStepBinding.tvDescription.setText(step.description());
    fragmentRecipeStepByStepBinding.fabBack.setOnClickListener(
        (view) -> stepNavigationHandler.onPreviousClick());
    fragmentRecipeStepByStepBinding.fabForward.setOnClickListener(
        (view) -> stepNavigationHandler.onNextClick());

    videoPlayerComponent =
        new VideoPlayerComponent(
            getContext(), fragmentRecipeStepByStepBinding, mVideoPlayerViewModel);
    getLifecycle().addObserver(videoPlayerComponent);

    return fragmentRecipeStepByStepBinding.getRoot();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    mActivity = (RecipeDetailActivity) context;
    stepNavigationHandler = (StepNavigationHandler) context;
    mVideoPlayerViewModel = ViewModelProviders.of(mActivity).get(VideoPlayerViewModel.class);
    mVideoPlayerViewModel.setVideoUrl(step.videoUrl());
  }

  @Override
  public void setArguments(@Nullable Bundle bundle) {
    if (Optional.fromNullable(bundle).isPresent()
        && Optional.fromNullable(bundle.get(STEP_ARG)).isPresent()) {
      step = (Step) bundle.getSerializable(STEP_ARG);
    } else {
      throw new IllegalArgumentException("Unable to locate a Step.");
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (Optional.fromNullable(videoPlayerComponent).isPresent()) {
      videoPlayerComponent.releasePlayer();
    }
  }

  public static final RecipeStepByStepFragment create(Step step) {
    RecipeStepByStepFragment recipeStepByStepFragment = new RecipeStepByStepFragment();
    Bundle bundle = new Bundle();
    bundle.putSerializable(STEP_ARG, step);
    recipeStepByStepFragment.setArguments(bundle);
    return recipeStepByStepFragment;
  }

  /** Specifies that the user has selected the prior or the next step for a recipe. */
  public interface StepNavigationHandler {
    void onPreviousClick();

    void onNextClick();
  }
}
