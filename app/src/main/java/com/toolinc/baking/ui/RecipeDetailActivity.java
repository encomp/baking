package com.toolinc.baking.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.toolinc.baking.R;
import com.toolinc.baking.client.model.Recipe;
import com.toolinc.baking.ui.fragment.RecipeInformationFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;

/** Activity that displays the recipe details such as ingredients and instructions. */
public final class RecipeDetailActivity extends AppCompatActivity {

  private RecipeInformationFragment recipeInformationFragment;
  private Recipe recipe;

  @BindView(R.id.fab_back)
  FloatingActionButton goBack;

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.activity_recipe_detail);
    ButterKnife.bind(this);

    goBack.setOnClickListener((view) -> finish());

    Bundle fragmentArg = new Bundle();
    if (getIntent().hasExtra(Intent.EXTRA_KEY_EVENT)) {
      recipe = (Recipe) getIntent().getSerializableExtra(Intent.EXTRA_KEY_EVENT);
      fragmentArg.putSerializable(Intent.EXTRA_KEY_EVENT, recipe);
    }

    recipeInformationFragment = new RecipeInformationFragment();
    recipeInformationFragment.setArguments(fragmentArg);
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager
        .beginTransaction()
        .add(R.id.fl_recipe_detail_information, recipeInformationFragment)
        .commit();
  }
}
