package com.toolinc.baking.ui;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.toolinc.baking.R;
import com.toolinc.baking.test.BakingIdlingResource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4ClassRunner.class)
public class RecipesActivityContentLoadedTest {

  private static final String BROWNIES = "Brownies";
  private final BakingIdlingResource mBakingIdlingResource = new BakingIdlingResource();

  @Rule
  public ActivityTestRule<RecipesActivity> mActivityTestRule =
      new ActivityTestRule<>(RecipesActivity.class);

  @Before
  public void registerIdlingResource() {
    mActivityTestRule
        .getActivity()
        .recipeListFragment
        .setBakingIdlingResource(mBakingIdlingResource);
    IdlingRegistry.getInstance().register(mBakingIdlingResource);
  }

  @Test
  public void recipesActivityTest() {
    onView(
            allOf(
                withId(R.id.tv_recipe_name),
                withText(BROWNIES),
                childAtPosition(childAtPosition(withId(R.id.mcv_recipe), 0), 1),
                isDisplayed()))
        .check(matches(withText(BROWNIES)));
  }

  @After
  public void unregisterIdlingResource() {
    IdlingRegistry.getInstance().unregister(mBakingIdlingResource);
  }

  private static Matcher<View> childAtPosition(
      final Matcher<View> parentMatcher, final int position) {

    return new TypeSafeMatcher<View>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("Child at position " + position + " in parent ");
        parentMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view) {
        ViewParent parent = view.getParent();
        return parent instanceof ViewGroup
            && parentMatcher.matches(parent)
            && view.equals(((ViewGroup) parent).getChildAt(position));
      }
    };
  }
}
