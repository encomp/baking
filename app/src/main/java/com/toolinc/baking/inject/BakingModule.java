package com.toolinc.baking.inject;

import com.toolinc.baking.ui.fragment.RecipeListFragment;
import com.toolinc.baking.ui.widget.IngredientsWidgetConfigureActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Specifies the object graph to inject different instances to activities and fragments of the
 * application.
 */
@Module
public abstract class BakingModule {

  @ContributesAndroidInjector
  abstract IngredientsWidgetConfigureActivity contributeIngredientsWidgetConfigureActivity();

  @ContributesAndroidInjector
  abstract RecipeListFragment contributeFragmentInjector();
}
