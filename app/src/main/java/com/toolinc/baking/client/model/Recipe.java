package com.toolinc.baking.client.model;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;

/** Specifies the ingredients, cooking steps and serving size for a specific recipe. */
@AutoValue
public abstract class Recipe implements Serializable {

  public abstract int id();

  public abstract String name();

  public abstract ImmutableList<Ingredient> ingredients();

  public abstract ImmutableList<Step> steps();

  public abstract int servingSize();

  public abstract String image();

  @NonNull
  public static final Builder builder() {
    return new Builder();
  }

  /**
   * Builder class to instantiate immutable objects of {@link Recipe} from the RestApi json result.
   */
  public static final class Builder extends TypeAdapter<Recipe> {
    private static final Gson GSON =
        new GsonBuilder()
            .registerTypeAdapter(Ingredient.class, Ingredient.builder())
            .registerTypeAdapter(Step.class, Step.builder())
            .create();

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("ingredients")
    private List<Ingredient> ingredients;

    @SerializedName("steps")
    private List<Step> steps;

    @SerializedName("servings")
    private int servingSize;

    @SerializedName("image")
    private String image;

    private Builder() {}

    /**
     * Creates a new instance of {@link Recipe} from the json response:
     *
     * <p><code>
     * "recipe" : {
     *  "id": 1,
     *  "name": "Nutella Pie",
     *  "ingredients": [
     *    {
     *      "quantity": 2,
     *      "measure": "CUP",
     *      "ingredient": "Graham Cracker crumbs"
     *    },
     *    ...
     *  ],
     *  "steps": [
     *    {
     *      "id": 0,
     *      "shortDescription": "Recipe Introduction",
     *      "description": "Recipe Introduction",
     *      "videoURL": "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
     *      "thumbnailURL": ""
     *    },
     *    ...
     *  ],
     *  "servings": 8,
     *  "image": ""
     * }
     * </code>
     *
     * @return a new instance of {@link Recipe}.
     */
    public Recipe build() {
      return new AutoValue_Recipe(
          id,
          name,
          ImmutableList.copyOf(ingredients),
          ImmutableList.copyOf(steps),
          servingSize,
          image);
    }

    @Override
    public void write(JsonWriter out, Recipe value) throws IOException {
      throw new UnsupportedOperationException("This operation is not supported yet...");
    }

    @Override
    public Recipe read(JsonReader in) throws IOException {
      Builder builder = GSON.fromJson(in, Builder.class);
      return builder.build();
    }
  }
}
