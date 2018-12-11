package com.toolinc.baking.client.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;

import androidx.annotation.NonNull;

/** Specifies an ingredient that is part of a specific recipe. */
@AutoValue
public abstract class Ingredient implements Serializable {

  public abstract float quantity();

  public abstract String measure();

  public abstract String name();

  @NonNull
  public static final Builder builder() {
    return new Builder();
  }

  /**
   * Builder class to instantiate immutable objects of {@link Ingredient} from the RestApi json
   * result.
   */
  public static final class Builder extends TypeAdapter<Ingredient> {
    private static final Gson GSON = new Gson();

    @SerializedName("quantity")
    private float quantity;

    @SerializedName("measure")
    private String measure;

    @SerializedName("ingredient")
    private String name;

    private Builder() {}

    /**
     * Creates a new instance of {@link Ingredient} from the json response:
     *
     * <p><code>
     * "ingredient": {
     *    "quantity": 2,
     *    "measure": "CUP",
     *    "ingredient": "Graham Cracker crumbs"
     * }
     * </code>
     *
     * @return a new instance of {@link Ingredient}.
     */
    public Ingredient build() {
      return new AutoValue_Ingredient(quantity, measure, name);
    }

    @Override
    public void write(JsonWriter out, Ingredient ingredient) {
      throw new UnsupportedOperationException("This operation is not supported yet...");
    }

    @Override
    public Ingredient read(JsonReader in) throws IOException {
      Builder builder = GSON.fromJson(in, Builder.class);
      return builder.build();
    }
  }
}
