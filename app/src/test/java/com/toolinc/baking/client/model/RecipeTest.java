package com.toolinc.baking.client.model;

import com.google.common.collect.ImmutableList;
import com.google.gson.stream.JsonWriter;
import com.toolinc.baking.util.JsonUnmarshallerHelper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static com.google.common.truth.Truth.assertThat;

/** Tests for {@link Recipe}. */
public final class RecipeTest {

  private static final String JSON =
      JsonUnmarshallerHelper.toString(RecipeTest.class, "recipe_1.json");
  private static final JsonWriter JSON_WRITER =
      new JsonWriter(new PrintWriter(new ByteArrayOutputStream(10)));
  private static final ImmutableList<Ingredient> INGREDIENTS =
      ImmutableList.of(
          JsonUnmarshallerHelper.toIngredient(RecipeTest.class, "ingredient_1.json"),
          JsonUnmarshallerHelper.toIngredient(RecipeTest.class, "ingredient_2.json"));
  private static ImmutableList<Step> steps =
      ImmutableList.of(
          JsonUnmarshallerHelper.toStep(RecipeTest.class, "step_0.json"),
          JsonUnmarshallerHelper.toStep(RecipeTest.class, "step_1.json"),
          JsonUnmarshallerHelper.toStep(RecipeTest.class, "step_2.json"));

  @Rule public final ExpectedException expectedException = ExpectedException.none();

  @Test
  public void shouldCreateRecipe() throws IOException {
    Recipe recipe = Recipe.builder().fromJson(JSON);
    assertThat(recipe).isNotNull();
    assertThat(recipe.id()).isEqualTo(1);
    assertThat(recipe.name()).isEqualTo("Nutella Pie");
    assertThat(recipe.ingredients()).containsExactlyElementsIn(INGREDIENTS);
    assertThat(recipe.steps()).containsExactlyElementsIn(steps);
    assertThat(recipe.servingSize()).isEqualTo(8);
    assertThat(recipe.image()).isEqualTo("");
  }

  @Test
  public void shouldNotCreateJson() throws IOException {
    expectedException.expect(UnsupportedOperationException.class);
    expectedException.expectMessage("supported");
    Recipe.builder().write(JSON_WRITER, Recipe.builder().fromJson(JSON));
  }
}
