package com.toolinc.baking.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.toolinc.baking.R;
import com.toolinc.baking.client.RecipeClient;
import com.toolinc.baking.client.model.Recipe;
import com.toolinc.baking.ui.adapter.RecipeListAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Configures the set of {@link com.toolinc.baking.client.model.Step} that will be displayed on the
 * widget.
 */
public final class IngredientsWidgetConfigureActivity extends AppCompatActivity
    implements RecipeListAdapter.OnRecipeSelected, RecipeClient.RecipeCallback {

  private final RecipeListAdapter recipeListAdapter = new RecipeListAdapter(this);
  private final RecipeClient recipeClient = new RecipeClient(this);
  private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
  private ImmutableList<Recipe> recipes;

  @BindView(R.id.rv_recipe_list)
  RecyclerView rvRecipeList;

  @BindView(R.id.pb_loading_indicator)
  ProgressBar contentLoadingProgressBar;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.widget_configure_recipes);

    ButterKnife.bind(this);

    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if (Optional.fromNullable(extras).isPresent()) {
      appWidgetId =
          extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
      if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
        finish();
      }
    }

    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    rvRecipeList.setLayoutManager(layoutManager);
    rvRecipeList.setAdapter(recipeListAdapter);
    fetchMovies();
  }

  public void fetchMovies() {
    contentLoadingProgressBar.setVisibility(View.VISIBLE);
    Toast.makeText(this, R.string.recipe_loading_message, Toast.LENGTH_SHORT).show();
    recipeClient.fetchMovies(Optional.absent());
  }

  @Override
  public void onSuccess(ImmutableList<Recipe> recipes) {
    contentLoadingProgressBar.setVisibility(View.INVISIBLE);
    this.recipes = recipes;
    recipeListAdapter.setRecipes(recipes);
    rvRecipeList.setAdapter(recipeListAdapter);
  }

  @Override
  public void onFailure(String message) {
    contentLoadingProgressBar.setVisibility(View.INVISIBLE);
    Toast.makeText(this, R.string.recipe_loading_error_message, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onSelected(Recipe recipe) {
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
    IngredientsWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetId, recipe);

    Intent resultValue = new Intent();
    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    setResult(RESULT_OK, resultValue);
    finish();
  }
}
