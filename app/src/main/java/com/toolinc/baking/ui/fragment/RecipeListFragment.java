package com.toolinc.baking.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.toolinc.baking.R;
import com.toolinc.baking.client.RecipeClient;
import com.toolinc.baking.client.model.Recipe;
import com.toolinc.baking.test.BakingIdlingResource;
import com.toolinc.baking.ui.RecipeDetailActivity;
import com.toolinc.baking.ui.adapter.RecipeListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/** Renders a specific {@link java.util.List} of recipes. */
public final class RecipeListFragment extends Fragment
    implements RecipeListAdapter.OnRecipeSelected, RecipeClient.RecipeCallback {

  private static final int SCALING_FACTOR = 300;
  private static final int COLUMN_THRESHOLD = 2;
  private static final int MIN_NUM_COLS = 1;
  private final RecipeListAdapter recipeListAdapter = new RecipeListAdapter(this);
  private final RecipeClient recipeClient = new RecipeClient(this);
  private Optional<BakingIdlingResource> bakingIdlingResource = Optional.absent();

  @BindView(R.id.rv_recipe_list)
  RecyclerView rvRecipeList;

  @BindView(R.id.clpb_loading_indicator)
  ContentLoadingProgressBar contentLoadingProgressBar;

  private ImmutableList<Recipe> recipes;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
    ButterKnife.bind(this, view);
    GridLayoutManager layoutManager =
        new GridLayoutManager(getContext(), calculateNoOfColumns(getContext()));
    rvRecipeList.setLayoutManager(layoutManager);
    rvRecipeList.setAdapter(recipeListAdapter);
    fetchMovies();
    return view;
  }

  public void fetchMovies() {
    contentLoadingProgressBar.show();
    Toast.makeText(getContext(), R.string.recipe_loading_message, Toast.LENGTH_SHORT).show();
    recipeClient.fetchMovies(bakingIdlingResource);
  }

  @Override
  public void onSuccess(ImmutableList<Recipe> recipes) {
    contentLoadingProgressBar.hide();
    this.recipes = recipes;
    refreshRecycleView(recipes);
  }

  @Override
  public void onFailure(String message) {
    contentLoadingProgressBar.hide();
    Toast.makeText(getContext(), R.string.recipe_loading_error_message, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onSelected(Recipe recipe) {
    Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
    intent.putExtra(Intent.EXTRA_KEY_EVENT, recipe);
    startActivity(intent);
  }

  private void refreshRecycleView(@Nullable ImmutableList<Recipe> recipes) {
    recipeListAdapter.setRecipes(recipes);
    rvRecipeList.setAdapter(recipeListAdapter);
  }

  private static int calculateNoOfColumns(Context context) {
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
    int noOfColumns = (int) (dpWidth / SCALING_FACTOR);
    if (noOfColumns < COLUMN_THRESHOLD) {
      noOfColumns = MIN_NUM_COLS;
    }
    return noOfColumns;
  }

  @VisibleForTesting
  public void setBakingIdlingResource(BakingIdlingResource bakingIdlingResource) {
    this.bakingIdlingResource = Optional.fromNullable(bakingIdlingResource);
  }
}
