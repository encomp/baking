package com.toolinc.baking.ui;

import android.content.Intent;

import com.toolinc.baking.R;
import com.toolinc.baking.client.model.Recipe;
import com.toolinc.baking.test.BakingIdlingResource;
import com.toolinc.baking.util.JsonUnmarshallerHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

/** Tests for {@link RecipesActivity} */
@LargeTest
@RunWith(AndroidJUnit4ClassRunner.class)
public final class RecipesActivityIdlingTest {

  private static final Recipe RECIPE =
      JsonUnmarshallerHelper.toRecipe("recipe_number_1.json");
  private final BakingIdlingResource mBakingIdlingResource = new BakingIdlingResource();

  @Rule
  public IntentsTestRule<RecipesActivity> recipesActivityRule =
      new IntentsTestRule<>(RecipesActivity.class);

  @Before
  public void registerIdlingResource() {
    recipesActivityRule
        .getActivity()
        .recipeListFragment
        .setBakingIdlingResource(mBakingIdlingResource);

    // To prove that the test fails, omit this call:
    IdlingRegistry.getInstance().register(mBakingIdlingResource);
  }

  @Test
  public void shouldLoadAllRecipes() {
    onView(withId(R.id.rv_recipe_list))
        .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    intended(allOf(hasExtra(Intent.EXTRA_KEY_EVENT, RECIPE)));
  }

  @After
  public void unregisterIdlingResource() {
    IdlingRegistry.getInstance().unregister(mBakingIdlingResource);
  }
}
