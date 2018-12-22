package com.toolinc.baking.client.model;

import com.google.gson.stream.JsonWriter;
import com.toolinc.baking.util.JsonUnmarshallerHelper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static com.google.common.truth.Truth.assertThat;

/** Tests for {@link Ingredient}. */
public final class IngredientTest {

  private static final String JSON =
      JsonUnmarshallerHelper.toString(IngredientTest.class, "ingredient_1.json");
  private static final JsonWriter JSON_WRITER =
      new JsonWriter(new PrintWriter(new ByteArrayOutputStream(10)));

  @Rule public final ExpectedException expectedException = ExpectedException.none();

  @Test
  public void shouldCreateIngredient() throws IOException {
    Ingredient ingredient = Ingredient.builder().fromJson(JSON);
    assertThat(ingredient).isNotNull();
    assertThat(ingredient.quantity()).isEqualTo(2f);
    assertThat(ingredient.measure()).isEqualTo("CUP");
    assertThat(ingredient.name()).isEqualTo("Graham Cracker crumbs");
  }

  @Test
  public void shouldNotCreateJson() throws IOException {
    expectedException.expect(UnsupportedOperationException.class);
    expectedException.expectMessage("supported");
    Ingredient.builder().write(JSON_WRITER, Ingredient.builder().fromJson(JSON));
  }
}
