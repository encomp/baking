package com.toolinc.baking.util;

import com.toolinc.baking.client.model.Ingredient;
import com.toolinc.baking.client.model.Recipe;
import com.toolinc.baking.client.model.Step;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Helper class to unrmashall json files. */
public final class JsonUnmarshallerHelper {

  private JsonUnmarshallerHelper() {}

  public static final Ingredient toIngredient(Class clazz, String fileName) {
    try {
      return Ingredient.builder().fromJson(toString(clazz, fileName));
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to unmarshal into an ingredient instance.");
    }
  }

  public static final Step toStep(Class clazz, String fileName) {
    try {
      return Step.builder().fromJson(toString(clazz, fileName));
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to unmarshal into a step instance.");
    }
  }

  public static final Recipe toRecipe(Class clazz, String fileName) {
    try {
      return Recipe.builder().fromJson(toString(clazz, fileName));
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to unmarshal into a recipe instance.");
    }
  }

  public static String toString(Class clazz, String fileName) {
    try {
      URL url = clazz.getResource(fileName);
      Path path = Paths.get(url.toURI());
      return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    } catch (URISyntaxException | IOException exception) {
      throw new IllegalStateException("Unable to unmarshal into a json instance.");
    }
  }
}
