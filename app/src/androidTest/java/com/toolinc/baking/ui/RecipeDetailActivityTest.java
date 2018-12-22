package com.toolinc.baking.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.toolinc.baking.R;
import com.toolinc.baking.client.model.Recipe;
import com.toolinc.baking.util.JsonUnmarshallerHelper;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4ClassRunner.class)
public class RecipeDetailActivityTest {

  private static final Recipe RECIPE = JsonUnmarshallerHelper.toRecipe("recipe_number_1.json");
  private Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

  @Rule
  public ActivityTestRule<RecipeDetailActivity> mActivityTestRule =
      new ActivityTestRule<RecipeDetailActivity>(RecipeDetailActivity.class) {
        @Override
        protected Intent getActivityIntent() {
          Intent intent = new Intent(context, RecipeDetailActivity.class);
          intent.putExtra(Intent.EXTRA_KEY_EVENT, RECIPE);
          return intent;
        }
      };

  @Test
  public void shouldDisplayIngredients() {
    onView(
            allOf(
                withId(R.id.rv_ingredients),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class), 0),
                    2),
                isDisplayed()))
        .check(matches(isDisplayed()));
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
