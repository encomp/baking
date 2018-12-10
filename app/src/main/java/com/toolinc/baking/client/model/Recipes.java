package com.toolinc.baking.client.model;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;

/** Specifies a collection of recipes . */
@AutoValue
public abstract class Recipes implements Serializable {

  public abstract ImmutableList<Recipe> recipes();

  @NonNull
  public static final Builder builder() {
    return new Builder();
  }

  /**
   * Builder class to instantiate immutable objects of {@link Recipes} from the RestApi json result.
   */
  public static final class Builder extends TypeAdapter<Recipes> {

    private static final Gson GSON =
        new GsonBuilder().registerTypeAdapter(Recipe.class, Recipe.builder()).create();

    private List<Recipe> recipes;

    private Builder() {}

    /**
     * Creates a collection of {@link Recipe} from the json response.
     *
     * @return a new instance of {@link Recipes}.
     */
    public Recipes build() {
      return new AutoValue_Recipes(ImmutableList.copyOf(recipes));
    }

    @Override
    public void write(JsonWriter out, Recipes value) throws IOException {}

    @Override
    public Recipes read(JsonReader in) throws IOException {
      Builder builder = GSON.fromJson(in, Builder.class);
      return builder.build();
    }
  }
}
