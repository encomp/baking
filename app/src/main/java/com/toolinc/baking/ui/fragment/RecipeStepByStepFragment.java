package com.toolinc.baking.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Optional;
import com.toolinc.baking.R;
import com.toolinc.baking.client.model.Recipe;
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
  private static final String RECIPE_ARG = "RECIPE";
  private static final String STEP_NUMBER_ARG = "STEP_NUMBER";
  private static final String VIDEO_POSITION = "VIDEO_POSITION";
  private Recipe recipe;
  private Step step;
  private VideoPlayerViewModel videoPlayerViewModel;
  private VideoPlayerComponent videoPlayerComponent;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    recipe = (Recipe) getArguments().getSerializable(RECIPE_ARG);
    int stepNumber = getArguments().getInt(STEP_NUMBER_ARG);
    step = recipe.steps().get(stepNumber);
    videoPlayerViewModel.setVideoUrl(step.videoUrl());
    if (Optional.fromNullable(savedInstanceState).isPresent()
        && Optional.fromNullable(savedInstanceState.getLong(VIDEO_POSITION)).isPresent()) {
      videoPlayerViewModel.setPosition(savedInstanceState.getLong(VIDEO_POSITION));
    }
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    FragmentRecipeStepByStepBinding fragmentRecipeStepByStepBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_step_by_step, container, false);

    fragmentRecipeStepByStepBinding.tvDescription.setText(step.getDescriptionWithoutStepNumber());
    fragmentRecipeStepByStepBinding.chipStepNumber.setText(step.id() + "");

    videoPlayerComponent =
        new VideoPlayerComponent(
            getContext(), fragmentRecipeStepByStepBinding, videoPlayerViewModel);
    getLifecycle().addObserver(videoPlayerComponent);

    return fragmentRecipeStepByStepBinding.getRoot();
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    if (Optional.fromNullable(videoPlayerViewModel.getPosition()).isPresent()) {
      outState.putLong(VIDEO_POSITION, videoPlayerViewModel.getPosition());
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    videoPlayerViewModel =
        ViewModelProviders.of((RecipeDetailActivity) context).get(VideoPlayerViewModel.class);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (Optional.fromNullable(videoPlayerComponent).isPresent()) {
      videoPlayerComponent.releasePlayer();
    }
  }

  public static final RecipeStepByStepFragment create(Recipe recipe, int stepNumber) {
    RecipeStepByStepFragment recipeStepByStepFragment = new RecipeStepByStepFragment();
    Bundle bundle = new Bundle();
    bundle.putSerializable(RECIPE_ARG, recipe);
    bundle.putInt(STEP_NUMBER_ARG, stepNumber);
    recipeStepByStepFragment.setArguments(bundle);
    return recipeStepByStepFragment;
  }
}
