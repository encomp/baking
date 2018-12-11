package com.toolinc.baking.client.model;

import com.google.common.collect.ImmutableList;
import com.google.gson.stream.JsonWriter;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.truth.Truth.assertThat;

/** Tests for {@link Recipe}. */
public final class RecipeTest {

  private static final String INGREDIENT_1 =
      "      {\n"
          + "        \"quantity\": 2,\n"
          + "        \"measure\": \"CUP\",\n"
          + "        \"ingredient\": \"Graham Cracker crumbs\"\n"
          + "      }";
  private static final String INGREDIENT_2 =
      "      {\n"
          + "        \"quantity\": 6,\n"
          + "        \"measure\": \"TBLSP\",\n"
          + "        \"ingredient\": \"unsalted butter, melted\"\n"
          + "      }";
  private static final String STEP_1 =
      "      {\n"
          + "        \"id\": 0,\n"
          + "        \"shortDescription\": \"Recipe Introduction\",\n"
          + "        \"description\": \"Recipe Introduction\",\n"
          + "        \"videoURL\": \"https://d17h27t6h515.cloudfront.net/-intro-creampie.mp4\",\n"
          + "        \"thumbnailURL\": \"\"\n"
          + "      }";
  private static final String STEP_2 =
      "      {\n"
          + "        \"id\": 1,\n"
          + "        \"shortDescription\": \"Starting prep\",\n"
          + "        \"description\": \"1. Preheat the oven to 350\\u00b0F. Butter a 9\\\" deep "
          + "dish pie pan.\",\n"
          + "        \"videoURL\": \"\",\n"
          + "        \"thumbnailURL\": \"\"\n"
          + "      }";
  private static final String STEP_3 =
      "      {\n"
          + "        \"id\": 2,\n"
          + "        \"shortDescription\": \"Prep the cookie crust.\",\n"
          + "        \"description\": \"2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of "
          + "sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter "
          + "and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly "
          + "mixed.\",\n"
          + "        \"videoURL\": \"https://d17h27t6h515.cloudfront.net/crackers-creampie.mp4\",\n"
          + "        \"thumbnailURL\": \"\"\n"
          + "      }";
  private static final String JSON =
      "{\n"
          + "    \"id\": 1,\n"
          + "    \"name\": \"Nutella Pie\",\n"
          + "    \"ingredients\": [\n"
          + INGREDIENT_1
          + ",\n"
          + INGREDIENT_2
          + "\n"
          + "    ],\n"
          + "    \"steps\": [\n"
          + STEP_1
          + ",\n"
          + STEP_2
          + ",\n"
          + STEP_3
          + "    ],\n"
          + "    \"servings\": 8,\n"
          + "    \"image\": \"\"\n"
          + "  }";
  private static final JsonWriter JSON_WRITER =
      new JsonWriter(new PrintWriter(new ByteArrayOutputStream(10)));
  private static List<Ingredient> ingredients;
  private static List<Step> steps;

  @Rule public final ExpectedException expectedException = ExpectedException.none();

  @BeforeClass
  public static final void setUpTests() throws IOException {
    ingredients =
        ImmutableList.of(INGREDIENT_1, INGREDIENT_2)
            .stream()
            .map(
                json -> {
                  Ingredient ingredient = null;
                  try {
                    ingredient = Ingredient.builder().fromJson(json);
                  } catch (IOException e) {
                    throw new IllegalStateException("Unable to proceed.");
                  }
                  return ingredient;
                })
            .collect(Collectors.toList());
    steps =
        ImmutableList.of(STEP_1, STEP_2, STEP_3)
            .stream()
            .map(
                json -> {
                  Step step = null;
                  try {
                    step = Step.builder().fromJson(json);
                  } catch (IOException exc) {
                    throw new IllegalStateException("Unable to proceed.");
                  }
                  return step;
                })
            .collect(Collectors.toList());
  }

  @Test
  public void shouldCreateRecipe() throws IOException {
    Recipe recipe = Recipe.builder().fromJson(JSON);
    assertThat(recipe).isNotNull();
    assertThat(recipe.id()).isEqualTo(1);
    assertThat(recipe.name()).isEqualTo("Nutella Pie");
    assertThat(recipe.ingredients()).containsExactlyElementsIn(ingredients);
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
