package com.toolinc.baking.ui;

import android.os.Bundle;
import android.view.View;

import com.toolinc.baking.R;
import com.toolinc.baking.ui.fragment.RecipeListFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/** Main activity that displays a {@link com.toolinc.baking.client.model.Recipes}. */
public final class RecipesActivity extends AppCompatActivity {

  private RecipeListFragment recipeListFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipes);

    recipeListFragment = new RecipeListFragment();
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().add(R.id.recipe_list_fragment, recipeListFragment).commit();
  }

  public void onLoadRecipes(View v) {
    recipeListFragment.fetchMovies();
  }
}
