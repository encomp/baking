package com.toolinc.baking.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.base.Optional;
import com.toolinc.baking.R;
import com.toolinc.baking.client.model.Recipe;
import com.toolinc.baking.client.model.Step;
import com.toolinc.baking.lifecycle.StepsViewModel;
import com.toolinc.baking.ui.fragment.RecipeInformationFragment;
import com.toolinc.baking.ui.fragment.RecipeStepByStepFragment;
import com.toolinc.baking.ui.widget.InstructionListAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

/** Activity that displays the recipe details such as ingredients and instructions. */
public final class RecipeDetailActivity extends AppCompatActivity
    implements InstructionListAdapter.OnStepSelected {

  private RecipeInformationFragment recipeInformationFragment;
  private RecipeStepByStepFragment recipeStepByStepFragment;
  private Recipe recipe;
  private StepsViewModel stepsViewModel;
  private CoordinatorLayout cl_Video;
  private boolean tablet;

  @BindView(R.id.fab_back)
  FloatingActionButton goBack;

  @BindView(R.id.bottom_app_bar)
  BottomAppBar bottomAppBar;

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.activity_recipe_detail);
    ButterKnife.bind(this);

    cl_Video = findViewById(R.id.fl_recipe_detail_video);
    tablet = Optional.fromNullable(cl_Video).isPresent();
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
    if (tablet) {
      stepsViewModel.setSteps(recipe.steps(), 0);
      onSelected(stepsViewModel.getCurrentStep(), 0);
    }
  }

  @Override
  public void onSelected(Step step, int position) {
    recipeStepByStepFragment = RecipeStepByStepFragment.create(step);
    stepsViewModel.setSteps(recipe.steps(), position);
    bottomAppBar.replaceMenu(R.menu.menu_navigaton);
    bottomAppBar
        .getMenu()
        .findItem(R.id.mi_next_step)
        .setOnMenuItemClickListener((MenuItem item) -> onItemSelected(item));
    bottomAppBar
        .getMenu()
        .findItem(R.id.mi_prior_step)
        .setOnMenuItemClickListener((MenuItem item) -> onItemSelected(item));
    if (tablet) {
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.fl_recipe_detail_video, recipeStepByStepFragment)
          .commit();
    } else {
      getSupportFragmentManager()
          .beginTransaction()
          .detach(recipeInformationFragment)
          .add(R.id.fl_recipe_detail_information, recipeStepByStepFragment)
          .commit();
    }
  }

  private boolean onItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.mi_next_step:
        stepsViewModel.nextStep();
        updateStepFragment(RecipeStepByStepFragment.create(stepsViewModel.getCurrentStep()));
        return true;

      case R.id.mi_prior_step:
        stepsViewModel.priorStep();
        updateStepFragment(RecipeStepByStepFragment.create(stepsViewModel.getCurrentStep()));
        return true;
    }
    return false;
  }

  private void updateStepFragment(RecipeStepByStepFragment newStep) {
    if (tablet) {
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.fl_recipe_detail_video, newStep)
          .commit();
    } else {
      getSupportFragmentManager()
          .beginTransaction()
          .detach(recipeStepByStepFragment)
          .add(R.id.fl_recipe_detail_information, newStep)
          .commit();
    }
    recipeStepByStepFragment = newStep;
  }
}
