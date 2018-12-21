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
import com.toolinc.baking.client.BakingClient;
import com.toolinc.baking.client.model.Recipe;
import com.toolinc.baking.client.model.Recipes;
import com.toolinc.baking.ui.RecipeDetailActivity;
import com.toolinc.baking.ui.widget.RecipeListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Renders a specific {@link java.util.List} of recipes. */
public final class RecipeListFragment extends Fragment
    implements RecipeListAdapter.OnRecipeSelected, Callback<Recipes> {

  private final RecipeListAdapter recipeListAdapter = new RecipeListAdapter(this);
  private Call<Recipes> recipesCall;
  private ImmutableList<Recipe> recipes;

  @BindView(R.id.rv_recipe_list)
  RecyclerView rvRecipeList;

  @BindView(R.id.clpb_loading_indicator)
  ContentLoadingProgressBar contentLoadingProgressBar;

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
    fetchMovies(BakingClient.create().recipes());
    return view;
  }

  public void fetchMovies(Call<Recipes> call) {
    contentLoadingProgressBar.show();
    Toast.makeText(getContext(), R.string.recipe_loading_message, Toast.LENGTH_SHORT).show();
    if (Optional.fromNullable(recipesCall).isPresent()) {
      recipesCall.cancel();
    }
    recipesCall = call;
    recipesCall.enqueue(this);
  }

  @Override
  public void onResponse(Call<Recipes> call, Response<Recipes> response) {
    contentLoadingProgressBar.hide();
    recipes = response.body().recipes();
    refreshRecycleView(recipes);
  }

  @Override
  public void onFailure(Call<Recipes> call, Throwable t) {
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
    int scalingFactor = 400;
    int noOfColumns = (int) (dpWidth / scalingFactor);
    if (noOfColumns < 2) {
      noOfColumns = 1;
    }
    return noOfColumns;
  }
}
