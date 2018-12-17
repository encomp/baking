package com.toolinc.baking.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.toolinc.baking.R;
import com.toolinc.baking.client.model.Recipe;
import com.toolinc.baking.client.model.Step;
import com.toolinc.baking.lifecycle.StepsViewModel;
import com.toolinc.baking.ui.fragment.RecipeInformationFragment;
import com.toolinc.baking.ui.fragment.RecipeStepByStepFragment;
import com.toolinc.baking.ui.widget.InstructionListAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

/** Activity that displays the recipe details such as ingredients and instructions. */
public final class RecipeDetailActivity extends AppCompatActivity
    implements InstructionListAdapter.OnStepSelected,
        RecipeStepByStepFragment.StepNavigationHandler {

  private RecipeInformationFragment recipeInformationFragment;
  private RecipeStepByStepFragment recipeStepByStepFragment;
  private Recipe recipe;
  private StepsViewModel stepsViewModel;

  @BindView(R.id.fab_back)
  FloatingActionButton goBack;

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.activity_recipe_detail);
    ButterKnife.bind(this);

    goBack.setOnClickListener((view) -> finish());

    if (getIntent().hasExtra(Intent.EXTRA_KEY_EVENT)) {
      recipe = (Recipe) getIntent().getSerializableExtra(Intent.EXTRA_KEY_EVENT);
    }
    stepsViewModel = ViewModelProviders.of(this).get(StepsViewModel.class);

    recipeInformationFragment = RecipeInformationFragment.create(recipe);
    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.fl_recipe_detail_information, recipeInformationFragment)
        .commit();
  }

  @Override
  public void onSelected(Step step, int position) {
    recipeStepByStepFragment = RecipeStepByStepFragment.create(step);
    stepsViewModel.setSteps(recipe.steps(), position);
    updateStepFragment();
  }

  @Override
  public void onPreviousClick() {
    stepsViewModel.priorStep();
    updateStepFragment();
  }

  @Override
  public void onNextClick() {
    stepsViewModel.nextStep();
    updateStepFragment();
  }

  private void updateStepFragment() {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fl_recipe_detail_information, recipeStepByStepFragment)
        .commit();
  }
}
