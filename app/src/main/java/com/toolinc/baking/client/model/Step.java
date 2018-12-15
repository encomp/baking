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

/** Specifies a specific step of the cooking process for a given recipe. */
@AutoValue
public abstract class Step implements Serializable {

  private static final String REMOVE_STEP = "^[0-9]+\\. ";

  public abstract int id();

  public abstract String shortDescription();

  public abstract String description();

  public abstract String videoUrl();

  public abstract String thumbnailUrl();

  public String getDescriptionWithoutStepNumber() {
    return description().replaceAll(REMOVE_STEP, "");
  }

  @NonNull
  public static final Builder builder() {
    return new Builder();
  }

  /**
   * Builder class to instantiate immutable objects of {@link Step} from the RestApi json result.
   */
  public static final class Builder extends TypeAdapter<Step> {
    private static final Gson GSON = new Gson();

    @SerializedName("id")
    private int id;

    @SerializedName("shortDescription")
    private String shortDescription;

    @SerializedName("description")
    private String description;

    @SerializedName("videoURL")
    private String videoUrl;

    @SerializedName("thumbnailURL")
    private String thumbnailUrl;

    private Builder() {}

    /**
     * Creates a new instance of {@link Step} from the json response:
     *
     * <p><code>
     * "step" : {
     *  "id": 0,
     *  "shortDescription": "Recipe Introduction",
     *  "description": "Recipe Introduction",
     *  "videoURL": "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
     *  "thumbnailURL": ""
     * }
     * </code>
     *
     * @return a new instance of {@link Step}.
     */
    public Step build() {
      return new AutoValue_Step(id, shortDescription, description, videoUrl, thumbnailUrl);
    }

    @Override
    public void write(JsonWriter out, Step value) throws IOException {
      throw new UnsupportedOperationException("This operation is not supported yet...");
    }

    @Override
    public Step read(JsonReader in) throws IOException {
      Builder builder = GSON.fromJson(in, Builder.class);
      return builder.build();
    }
  }
}
