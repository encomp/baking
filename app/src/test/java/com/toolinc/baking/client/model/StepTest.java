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

/** Tests for {@link Step}. */
public final class StepTest {

  private static final String JSON = JsonUnmarshallerHelper.toString(StepTest.class, "step_0.json");
  private static final JsonWriter JSON_WRITER =
      new JsonWriter(new PrintWriter(new ByteArrayOutputStream(10)));

  @Rule public final ExpectedException expectedException = ExpectedException.none();

  @Test
  public void shouldCreateStep() throws IOException {
    Step step = Step.builder().fromJson(JSON);
    assertThat(step).isNotNull();
    assertThat(step.id()).isEqualTo(0);
    assertThat(step.shortDescription()).isEqualTo("Recipe Introduction");
    assertThat(step.description()).isEqualTo("Recipe Introduction");
    assertThat(step.videoUrl())
        .isEqualTo("https://d17h27t6h515.cloudfront.net/-intro-creampie.mp4");
    assertThat(step.thumbnailUrl()).isEqualTo("");
  }

  @Test
  public void shouldNotCreateJson() throws IOException {
    expectedException.expect(UnsupportedOperationException.class);
    expectedException.expectMessage("supported");
    Step.builder().write(JSON_WRITER, Step.builder().fromJson(JSON));
  }
}
