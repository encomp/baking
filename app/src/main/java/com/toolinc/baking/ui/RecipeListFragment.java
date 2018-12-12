package com.toolinc.baking.ui;

import android.os.Bundle;
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
import com.toolinc.baking.ui.widget.RecipeListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
  ContentLoadingProgressBar progressBar;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
    ButterKnife.bind(this, view);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    rvRecipeList.setLayoutManager(linearLayoutManager);
    rvRecipeList.setAdapter(recipeListAdapter);
    fetchMovies(BakingClient.create().recipes());
    return view;
  }

  private void fetchMovies(Call<Recipes> call) {
    progressBar.setVisibility(View.VISIBLE);
    Toast.makeText(getContext(), R.string.recipe_loading_message, Toast.LENGTH_SHORT).show();
    if (Optional.fromNullable(recipesCall).isPresent()) {
      recipesCall.cancel();
    }
    recipesCall = call;
    recipesCall.enqueue(this);
  }

  @Override
  public void onResponse(Call<Recipes> call, Response<Recipes> response) {
    progressBar.setVisibility(View.INVISIBLE);
    recipes = response.body().recipes();
    refreshRecycleView(recipes);
  }

  @Override
  public void onFailure(Call<Recipes> call, Throwable t) {
    progressBar.setVisibility(View.INVISIBLE);
    Toast.makeText(getContext(), R.string.recipe_loading_error_message, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onSelected(Recipe recipe) {}

  private void refreshRecycleView(@Nullable ImmutableList<Recipe> recipes) {
    recipeListAdapter.setRecipes(recipes);
    rvRecipeList.setAdapter(recipeListAdapter);
  }
}
